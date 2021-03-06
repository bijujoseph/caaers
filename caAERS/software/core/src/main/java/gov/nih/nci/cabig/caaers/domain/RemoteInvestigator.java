/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.domain;

import gov.nih.nci.cabig.caaers.resolver.InvestigatorResolver;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.semanticbits.coppa.domain.annotations.RemoteEntity;
import com.semanticbits.coppa.domain.annotations.RemoteProperty;
import com.semanticbits.coppa.domain.annotations.RemoteUniqueId;

 
/**
 * The Class RemoteInvestigator.
 */
@Entity
@RemoteEntity(entityResolver=InvestigatorResolver.class)
@DiscriminatorValue("REMOTE")
public class RemoteInvestigator extends Investigator{
	
	/* (non-Javadoc)
	 * @see gov.nih.nci.cabig.caaers.domain.Investigator#getNciIdentifier()
	 */
	@RemoteProperty
    public String getNciIdentifier() {
        return nciIdentifier;
    }
	
	/* (non-Javadoc)
	 * @see gov.nih.nci.cabig.caaers.domain.Investigator#getExternalId()
	 */
	@RemoteUniqueId
	@RemoteProperty
	public String getExternalId() {
		return externalId;
	}
	
	/* (non-Javadoc)
	 * @see gov.nih.nci.cabig.caaers.domain.Person#getFirstName()
	 */
	@RemoteProperty
	public String getFirstName() {
        return firstName;
    }
	
	/* (non-Javadoc)
	 * @see gov.nih.nci.cabig.caaers.domain.Person#getLastName()
	 */
	@RemoteProperty
    public String getLastName() {
        return lastName;
    }
	
	/* (non-Javadoc)
	 * @see gov.nih.nci.cabig.caaers.domain.Person#getMiddleName()
	 */
	@RemoteProperty
    public String getMiddleName() {
        return middleName;
    }
	
	/* (non-Javadoc)
	 * @see gov.nih.nci.cabig.caaers.domain.Person#getEmailAddress()
	 */
	@RemoteProperty
	public String getEmailAddress() {
		return emailAddress;
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cabig.caaers.domain.Person#getFaxNumber()
	 */
	@Override
	public String getFaxNumber() {
		return faxNumber;
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cabig.caaers.domain.Person#getPhoneNumber()
	 */
	@Override
	public String getPhoneNumber() {
		return phoneNumber;
	}
}
