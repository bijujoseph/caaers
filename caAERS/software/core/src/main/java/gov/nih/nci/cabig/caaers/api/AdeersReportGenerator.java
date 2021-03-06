/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.api;

import gov.nih.nci.cabig.caaers.domain.ExpeditedAdverseEventReport;
import gov.nih.nci.cabig.caaers.domain.ReportFormatType;
import gov.nih.nci.cabig.caaers.domain.expeditedfields.ExpeditedReportTree;
import gov.nih.nci.cabig.caaers.domain.report.Report;
import gov.nih.nci.cabig.caaers.service.EvaluationService;
import gov.nih.nci.cabig.caaers.utils.XsltTransformer;

import java.io.BufferedReader;
import java.io.FileReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.*;

/**
 * @author Ion C. Olaru
 *
 * */
public class AdeersReportGenerator extends BasePDFGenerator {

    
    protected final Log log = LogFactory.getLog(getClass());

    // TO-DO set in spring config
    private String xmlXsltFile = "xslt/Caaers2Adeers-xml-AEReport.xslt";
    private String xslFOXsltFile = "xslt/Caaers2Adeers-pdf-AEReport.xslt";
    private String xslFOMedWatchXsltFile = "xslt/Caaers2Medwatch-pdf-AEReport.xslt";
    private String xslFODCPXsltFile = "xslt/Caaers2DCP-pdf-SAEForm.xslt";
    private String xslFOCIOMSTypeFormXsltFile = "xslt/Caaers2CIOMS-pdf-TypeForm.xslt";
    private String xslFOCIOMSXsltFile = "xslt/Caaers2CIOMS-pdf.xslt";
    private String xslFOCustomXsltFile = "xslt/CaaersCustom.xslt";
    private String xslE2BXsltFile = "xslt/CaaersToE2b.xslt";
//    private String xslFOCustomXsltFile = "/SB/caAERS/trunk/caAERS/software/core/src/main/resources/xslt/CaaersCustom.xslt";

    protected  AdverseEventReportSerializer adverseEventReportSerializer;
    protected EvaluationService evaluationService;



    public void generatePdf(String adverseEventReportXml, String pdfOutFileName) throws Exception {
        XsltTransformer xsltTrans = new XsltTransformer();
        xsltTrans.toPdf(adverseEventReportXml, pdfOutFileName, xslFOXsltFile);
    }

    public List<String> generateImage(String adverseEventReportXml, String pngOutFileName) throws Exception {
        XsltTransformer xsltTrans = new XsltTransformer();
        return xsltTrans.toImage(adverseEventReportXml, pngOutFileName, xslFOXsltFile);
    }
    
    public List<String> generateImage(String adverseEventReportXml, String pngOutFileName, Report report) throws Exception {
        XsltTransformer xsltTrans = new XsltTransformer();
        String xsltFile = getXSLTForReportFormatType(report);
        return xsltTrans.toImage(adverseEventReportXml, pngOutFileName, xsltFile);
    }

    public void generateDcpSaeForm(String adverseEventReportXml, String pdfOutFileName) throws Exception {
        XsltTransformer xsltTrans = new XsltTransformer();
        xsltTrans.toPdf(adverseEventReportXml, pdfOutFileName, xslFODCPXsltFile);
    }

    public void generateCIOMSTypeForm(String adverseEventReportXml, String pdfOutFileName) throws Exception {
        XsltTransformer xsltTrans = new XsltTransformer();
        xsltTrans.toPdf(adverseEventReportXml, pdfOutFileName, xslFOCIOMSTypeFormXsltFile);
    }

/*
* This method generated the PDF file based on the given XML & XSL
*
* @author   Ion C . Olaru
* @param    adverseEventReportXml   Serialized xml content
* @param    pdfOutFileName          The generated PDF file path     
*
* */
    public void generateCustomPDF(String adverseEventReportXml, String pdfOutFileName) throws Exception {
        generatePdf(adverseEventReportXml, pdfOutFileName, xslFOCustomXsltFile);
    }

    public void generateCIOMS(String adverseEventReportXml, String pdfOutFileName) throws Exception {
        XsltTransformer xsltTrans = new XsltTransformer();
        xsltTrans.toPdf(adverseEventReportXml, pdfOutFileName, xslFOCIOMSXsltFile);
    }

    public void generateMedwatchPdf(String adverseEventReportXml, String pdfOutFileName) throws Exception {
        XsltTransformer xsltTrans = new XsltTransformer();
        xsltTrans.toPdf(adverseEventReportXml, pdfOutFileName, xslFOMedWatchXsltFile);
    }
    
    public String generateE2BXml(String adverseEventReportXml) throws Exception {
    	XsltTransformer xsltTrans = new XsltTransformer();
        return xsltTrans.toText(adverseEventReportXml, xslE2BXsltFile);
    }
    
    /**
     * This method will generate the caAERS internal xml representation of the report.
     * @param aeReport - A data collection
     * @param report - A report
     */
    public String generateCaaersXml(ExpeditedAdverseEventReport aeReport,Report report) throws Exception{
        evaluationService.evaluateMandatoryness(aeReport, report);
    	return adverseEventReportSerializer.serialize(aeReport, report);
    }

    public String generateCaaersWithdrawXml(ExpeditedAdverseEventReport aeReport,Report report) throws Exception{
    	return adverseEventReportSerializer.serializeWithdrawXML(aeReport,report );
    }
    
    /**
     * This method will generate the PDF file and store it in the file system and return its path.
     * @param report
     * @param caaersXml
     * @return
     * @throws Exception
     */
    public String[] generateExternalReports(Report report, String caaersXml, int reportIdOrReportVersionId) throws Exception {
    	assert report != null;
    	ReportFormatType formatType = report.getReportDefinition().getReportFormatType();
    	
    	String pdfOutFile = System.getProperty("java.io.tmpdir");
    	switch (formatType) {
			case DCPSAEFORM:
				pdfOutFile += "/dcpSAEForm-" + reportIdOrReportVersionId + ".pdf";
	        	this.generateDcpSaeForm(caaersXml, pdfOutFile);
				break;
			case MEDWATCHPDF:
				pdfOutFile += "/medWatchReport-" + reportIdOrReportVersionId + ".pdf";
	        	this.generateMedwatchPdf(caaersXml, pdfOutFile);
				break;
			case CIOMSFORM:
				pdfOutFile += "/CIOMSForm-" + reportIdOrReportVersionId + ".pdf";
	        	this.generateCIOMS(caaersXml, pdfOutFile);
				break;
			case CIOMSSAEFORM:
				pdfOutFile += "/CIOMS-SAE-Form-" + reportIdOrReportVersionId + ".pdf";
	        	this.generateCIOMSTypeForm(caaersXml, pdfOutFile);
				break;
			case CUSTOM_REPORT:
				pdfOutFile += "/CustomReport-" + reportIdOrReportVersionId + ".pdf";
	        	this.generateCustomPDF(caaersXml, pdfOutFile);
				break;
			default: //adders
				pdfOutFile  += "/expeditedAdverseEventReport-" + reportIdOrReportVersionId + ".pdf";
				generatePdf(caaersXml, pdfOutFile);
				break;
		}
    	return new String[] { pdfOutFile };
    	
    }
    
    public String getXSLTForReportFormatType(Report report) throws Exception {
    	assert report != null;
    	ReportFormatType formatType = report.getReportDefinition().getReportFormatType();
    	String xslTFile = xslFOXsltFile;
    	switch (formatType) {
			case DCPSAEFORM:
				xslTFile = xslFODCPXsltFile;
				break;
			case MEDWATCHPDF:
				xslTFile = xslFOMedWatchXsltFile;
				break;
			case CIOMSFORM:
				xslTFile = xslFOCIOMSXsltFile;
				break;
			case CIOMSSAEFORM:
				xslTFile = xslFOCIOMSTypeFormXsltFile;
				break;
			case CUSTOM_REPORT:
				xslTFile = xslFOCustomXsltFile;
				break;
			case E2BXML:
				xslTFile = xslE2BXsltFile;
				
			default: //adders
				xslTFile = xslFOXsltFile;
				break;
		}
    	return xslTFile;
    	
    }
  


    ///OBJECT PROPERTIES
	public void setAdverseEventReportSerializer(AdverseEventReportSerializer adverseEventReportSerializer) {
		this.adverseEventReportSerializer = adverseEventReportSerializer;
	}

    public void setEvaluationService(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    /**
     * This method is testting the PDF generation for the given XML & XSL file
     *
     * @author  Ion C. Olaru
     * @return  generate the File
     */
    public static void createCustomPDFTest() {

        String XMLFile = "/home/dell/Downloads/expeditedAdverseEventReport-335.xml";
        String PDFFile = "/home/dell/Desktop/testAEReport.pdf";

        AdeersReportGenerator g = new AdeersReportGenerator();
        StringBuffer s = new StringBuffer("");
        try {
            FileReader input = new FileReader(XMLFile);
            BufferedReader bufRead = new BufferedReader(input);
            String line = bufRead.readLine();

            while (line != null) {
                s.append(line);
                line = bufRead.readLine();
            }

            String xml = s.toString();
            g.generateCustomPDF(xml, PDFFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        createMedwatchPDFTest();
    }

    public static void createMedwatchPDFTest() {
        String str1 = "";
        try {
//        	String file = "C:\\vin\\caAERS\\caAERS\\software\\expeditedAdverseEventReport-416.xml";
//        	String pdf = "C:\\vin\\caAERS\\caAERS\\software\\expeditedAdverseEventReport-416.pdf";
//        	String file = "C:\\vin\\caAERS\\caAERS\\software\\med1.xml";
//        	String pdf = "C:\\vin\\caAERS\\caAERS\\software\\med1.pdf";
        	String file = "C:\\vin\\caAERS\\tmp\\sample-msgs\\expeditedAdverseEventReport-374.xml";
        	String pdf = "C:\\vin\\caAERS\\tmp\\sample-msgs\\expeditedAdverseEventReport-374.pdf";
        	
            AdeersReportGenerator aeg = new AdeersReportGenerator();
            FileReader input = new FileReader(file);
            BufferedReader bufRead = new BufferedReader(input);
            String line = bufRead.readLine();

            while (line != null) {
                str1 = str1 + line;
                line = bufRead.readLine();
            }

            aeg.generateMedwatchPdf(str1, pdf);
            // aeg.generateMedwatchPdf(str1, "C:\\medwatch-2.pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
