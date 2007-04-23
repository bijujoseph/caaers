package gov.nih.nci.cabig.caaers.domain;

import org.apache.commons.collections.list.LazyList;
import org.apache.commons.collections.functors.InstantiateFactory;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Transient;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.persistence.Embedded;
import javax.persistence.AttributeOverrides;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import java.util.List;
import java.util.Date;
import java.util.LinkedList;

/**
 * @author Rhett Sutphin
 */
@Entity
@Table(name = "treatments")
@GenericGenerator(name="id-generator", strategy = "native",
    parameters = {
        @Parameter(name="sequence", value="seq_treatments_id")
    }
)
public class TreatmentInformation extends AbstractDomainObject implements AdverseEventReportChild {
    private AdverseEventReport report;

    private List<CourseAgent> courseAgentsInternal;
    private List<CourseAgent> courseAgents;

    private Date firstCourseDate;
    private CourseDate adverseEventCourse;

    public TreatmentInformation() {
        setCourseAgentsInternal(new LinkedList<CourseAgent>());
    }

    ////// LOGIC

    @Transient
    public boolean isInvestigationalAgentAdministered() {
        for (CourseAgent courseAgent : getCourseAgents()) {
            Boolean indicator = courseAgent.getStudyAgent().getInvestigationalNewDrugIndicator();
            if (indicator != null && indicator) return true;
        }
        return false;
    }

    public void addCourseAgent(CourseAgent courseAgent) {
        courseAgent.setTreatmentInformation(this);
        getCourseAgents().add(courseAgent);
    }

    /** @return a wrapped list which will never throw an {@link IndexOutOfBoundsException} */
    @Transient
    public List<CourseAgent> getCourseAgents() {
        return courseAgents;
    }

    @SuppressWarnings("unchecked")
    private void createLazyCourseAgents() {
        this.courseAgents = LazyList.decorate(getCourseAgentsInternal(),
            new InstantiateFactory(CourseAgent.class));
    }

    ////// BEAN PROPERTIES

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    public AdverseEventReport getReport() {
        return report;
    }

    public void setReport(AdverseEventReport report) {
        this.report = report;
    }

    public Date getFirstCourseDate() {
        return firstCourseDate;
    }

    public void setFirstCourseDate(Date firstCourse) {
        this.firstCourseDate = firstCourse;
    }

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "date", column = @Column(name = "adverse_event_course_date")),
        @AttributeOverride(name = "number", column = @Column(name = "adverse_event_course_number"))
    })
    public CourseDate getAdverseEventCourse() {
        if (adverseEventCourse == null) adverseEventCourse = new CourseDate();
        return adverseEventCourse;
    }

    public void setAdverseEventCourse(CourseDate adverseEventCourse) {
        this.adverseEventCourse = adverseEventCourse;
    }

    // This is annotated this way so that the IndexColumn will work with
    // the bidirectional mapping.  See section 2.4.6.2.3 of the hibernate annotations docs.
    @OneToMany
    @JoinColumn(name="treatment_id", nullable=false)
    @IndexColumn(name="list_index")
    @Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
    public List<CourseAgent> getCourseAgentsInternal() {
        return courseAgentsInternal;
    }

    public void setCourseAgentsInternal(List<CourseAgent> courseAgentsInternal) {
        this.courseAgentsInternal = courseAgentsInternal;
        createLazyCourseAgents();
    }
}
