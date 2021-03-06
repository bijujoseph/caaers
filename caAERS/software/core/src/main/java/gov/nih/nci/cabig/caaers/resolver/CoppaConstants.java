/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.resolver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoppaConstants {
	
	/** The function codes for StudyParticipation */
	public static final String LEAD_ORGANIZATION = "Lead Organization";
	public static final String SPONSOR = "Sponsor";
	public static final String TREATING_SITE= "Treating Site";
	
	/** Investigator Role Codes */
	public static final String PRINCIPAL_INVESTIGATOR = "Principal Investigator";
	public static final String STUDY_PRINCIPAL_INVESTIGATOR = "Study Principal Investigator";
	
	/* List of values for studySiteContact. The studySiteContact has to have one of these to be eligible for fetching as an Investigator. */
	public static final List<String> SITE_INVESTIGATOR_LIST = Arrays.asList("Principal Investigator", "Coordinating Investigator", "Sub Investigator");
	
	/*List of values Document Workflow Status*/
	public static final List<String> DOCUMENT_WORKFLOW_STATUS_LIST = Arrays.asList("Abstracted", "Verification Pending", "Abstraction Verified Response", "Abstraction Verified No Response");
	
	public static final String ABSTRACTED = "Abstracted";
	public static final String VERIFICATION_PENDING = "Verification Pending";
	public static final String ABSTRACTION_VERIFIED_RESPONSE = "Abstraction Verified Response";
	public static final String ABSTRACTION_VERIFIED_NO_RESPONSE = "Abstraction Verified No Response";
	
	public static final String CAXCHANGE_URL = "https://ncias-c278-v.nci.nih.gov:58445/wsrf-caxchange/services/cagrid/CaXchangeRequestProcessor";
	
	/*Identifier types*/
	public static final String NCI_ASSIGNED_IDENTIFIER = "NCI Assigned Identifier";
	public static final String COORDINATING_CENTER_IDENTIFER = "Coordinating Center Identifier";
	public static final String PROTOCOL_AUTHORITY_IDENTIFIER = "Protocol Authority Identifier";
	
	
	/* Ctep identifier name and root*/
	public static final String CTEP_IDENTIFIER_NAME="CTEP ID";
	public static final String CTEP_ROOT = "Cancer Therapy Evaluation Program Organization Identifier";
	
	public static final String NULLIFIED_RECORD = "Nullified";
	
	public static final Map<String,String> coppaMap = new HashMap<String,String>();
	static{
		//PhaseCode Mappings
		coppaMap.put("0", "Phase 0 Trial");
		coppaMap.put("I", "Phase I Trial");
		coppaMap.put("II", "Phase II Trial");
		coppaMap.put("I/II", "Phase I/II Trial");
		coppaMap.put("III", "Phase III Trial");
		coppaMap.put("II/III", "Phase II/III Trial");
		coppaMap.put("IV", "Phase IV Trial");
		coppaMap.put("Pilot", "Pilot");
		coppaMap.put("N/A", "N/A");
		coppaMap.put("Other", "Other");
		
		//StatusCode mappings
		coppaMap.put("Active", "Active - Trial is open to accrual");
		coppaMap.put("Approved", "Approved - Trial has official CTEP approval");
		coppaMap.put("Closed to Accrual", "Closed to Accrual");
		coppaMap.put("Closed to Accrual and Intervention", "Closed to Accrual & Treatment");
		coppaMap.put("Temporary Closed to Accrual", "Temporarily Closed to Accrual");
		coppaMap.put("Temporary Closed to Accrual and Intervention", "Temporarily Closed to Accrual & Treatment");
		coppaMap.put("Administratively Complete", "Administratively Complete");
		coppaMap.put("In Review", "In Review");
		coppaMap.put("Disapproved", "Disapproved");
		coppaMap.put("Complete", "Complete");
	}
	
	public static final List<String> HOLDER_TYPE_LIST = Arrays.asList("NIH", "NCI", "Industry", "Organization");
	public static final String HOLDER_TYPE_NCI = "NCI";
	public static final String HOLDER_TYPE_NIH = "NIH";
	public static final List<String> HOLDER_TYPE_INVESTIGATOR_OR_ORGANIZATION = Arrays.asList("Industry", "Organization");
	public static final String HOLDER_TYPE_INVESTIGATOR = "Investigator";
	public static final String DUMMY_ORGANIZATION_IDENTIFIER = "DUMMY";
	public static final String DUMMY_INVESTIGATOR_IDENTIFIER = "-1111";
	
}
