/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.web.search;

public class AdvancedSearchColumn{
	private String columnHeader;
	
	private Object value;
	
	private Object lengthyValue;
	
	public void setValue(Object value){
		this.value = value;
	}
	
	public Object getValue(){
		return value;
	}
	
	public void setLengthyValue(Object lengthyValue){
		this.lengthyValue = lengthyValue;
	}
	
	public Object getLengthyValue(){
		return lengthyValue;
	}
	
	public void setColumnHeader(String columnHeader){
		this.columnHeader = columnHeader;
	}
	
	public String getColumnHeader(){
		return columnHeader;
	}
}
