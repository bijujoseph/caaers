package gov.nih.nci.cabig.caaers.domain.report;

import gov.nih.nci.cabig.ctms.domain.AbstractMutableDomainObject;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
/**
 * This class represent the details that which is to be used
 * while creating the actual ScheduledNotification. 
 * @author Biju Joseph
 */
@Entity
@Table(name = "planned_notifications")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
    name = "dtype",
    discriminatorType = DiscriminatorType.STRING
)
@DiscriminatorValue("dtype")
@GenericGenerator(name = "id-generator", strategy = "native",
    parameters = {
        @Parameter(name = "sequence", value = "seq_planned_notifications_id")
    }
)
public abstract class PlannedNotification extends AbstractMutableDomainObject implements Serializable{

	/** The actual mark selected on the time scale*/
	@Column(name="index_on_time_scale")
	private int indexOnTimeScale;
	
	/** The recipients of this content */
	private List<Recipient> recipients;
	
	private NotificationBodyContent bodyContent;
	
	private List<NotificationAttachment> attachments;

    // TODO: this signature may be insufficient
    public abstract ScheduledNotification createScheduledNotification(String to);

    @Embedded
	public NotificationBodyContent getNotificationBodyContent(){
		return bodyContent;
	}
	public void setNotificationBodyContent(NotificationBodyContent content){
		this.bodyContent = content;
	}
	public int getIndexOnTimeScale() {
		return indexOnTimeScale;
	}

	public void setIndexOnTimeScale(int indexOnTimeScale) {
		this.indexOnTimeScale = indexOnTimeScale;
	}
	
	@OneToMany
	@JoinColumn(name="plnf_id", nullable=false)
	@Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	public List<Recipient> getRecipients() {
		
		return recipients;
	}
	
	public void setRecipients(List<Recipient> recipients) {
		this.recipients = recipients;
	}
	/**
	 * @return the attachments
	 */
	@OneToMany
	@JoinColumn(name="plnf_id", nullable=false)
	@Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	public List<NotificationAttachment> getAttachments() {
		return attachments;
	}
	/**
	 * @param attachments the attachments to set
	 */
	public void setAttachments(List<NotificationAttachment> attachments) {
		this.attachments = attachments;
	}

}
