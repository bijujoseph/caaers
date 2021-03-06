/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.domain.dto;

 
/**
 * The Class ReviewCommentsDTO.
 */
public class ReviewCommentsDTO {
	
	/** The comment. */
	private String comment;
	
	/**
	 * Instantiates a new review comments dto.
	 *
	 * @param comment the comment
	 */
	public ReviewCommentsDTO(String comment) {
		this.comment = comment;
	}
	
	/**
	 * Gets the comment.
	 *
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	
	/**
	 * Sets the comment.
	 *
	 * @param comment the new comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
}
