package gov.nih.nci.cabig.caaers.service.security;

import gov.nih.nci.cabig.caaers.CaaersNoSuchUserException;
import gov.nih.nci.cabig.caaers.CaaersSystemException;
import gov.nih.nci.cabig.caaers.dao.UserDao;
import gov.nih.nci.cabig.caaers.domain.User;
import gov.nih.nci.cabig.caaers.domain.repository.CSMUserRepository;
import gov.nih.nci.cabig.caaers.service.security.passwordpolicy.PasswordPolicyService;
import gov.nih.nci.cabig.caaers.service.security.user.Credential;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * @author Jared Flatow
 */
public class PasswordManagerServiceImpl implements PasswordManagerService {
	
    private PasswordPolicyService passwordPolicyService;

    private CSMUserRepository csmUserRepository;
    
    private UserDao userDao;

    public User requestToken(String userName) throws CaaersSystemException {
    	User user = userDao.getByLoginId(userName);
    	if(user == null){
    		throw new CaaersNoSuchUserException("User with login Id :" + userName + " unknown");
    	}
        //get the token
    	csmUserRepository.userCreateToken(user);
    	userDao.save(user);
    	return user;
    }

    public void setPassword(String userName, String password, String token)
            throws CaaersSystemException {
    	User user = userDao.getByLoginId(userName);
    	
    	if(user == null){
    		throw new CaaersNoSuchUserException("User with login Id :" + userName + " unknown");
    	}
        validateToken(user, token);
        validateAndSetPassword(user, password);
        userDao.save(user);
        
    }

    private boolean validateToken(User user, String token) throws CaaersSystemException {
        if (user.getTokenTime().after(
                new Timestamp(new Date().getTime()
                        - passwordPolicyService.getPasswordPolicy()
                        .getTokenTimeout())) 
                &&  StringUtils.equals(user.getToken(), token)) return true;
        throw new CaaersSystemException("Invalid token.");
    }

    private boolean validateAndSetPassword(User user, String password)
            throws CaaersSystemException {
        passwordPolicyService.validatePasswordAgainstCreationPolicy(new Credential(user.getLoginId(),
                password));
        csmUserRepository.userChangePassword(user, password, passwordPolicyService
                .getPasswordPolicy().getPasswordCreationPolicy().getPasswordHistorySize());
        return true;
    }

    @Required
    public void setPasswordPolicyService(PasswordPolicyService passwordPolicyService) {
        this.passwordPolicyService = passwordPolicyService;
    }

    @Required
    public void setCsmUserRepository(final CSMUserRepository csmUserRepository) {
        this.csmUserRepository = csmUserRepository;
    }
    @Required
    public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
