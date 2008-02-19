package gov.nih.nci.cabig.caaers.validation.validator;

import gov.nih.nci.cabig.caaers.validation.PropertyUtil;
import gov.nih.nci.cabig.caaers.validation.annotation.Validator;
import gov.nih.nci.cabig.caaers.validation.annotation.ValidatorClass;

import java.lang.annotation.Annotation;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.validation.BindException;


/**
 * Controller validation logic for validating objects.
 *
 * @author Biju Joseph, Created on December,7th, 2007
 */
public class WebControllerValidatorImpl implements ApplicationContextAware, WebControllerValidator {
    private final static Logger logger = Logger.getLogger(WebControllerValidatorImpl.class.getName());

    public void validate(final HttpServletRequest request, final Object command, final BindException errors) {

        if (request != null && command != null && errors != null) {
            BeanWrapperImpl beanWrapperImpl = new BeanWrapperImpl(command);

            Enumeration<String> propertyNames = request.getParameterNames();
            Map<String, String> propertyMap = new LinkedHashMap<String, String>();
            Map<String, String> collectionPropertyMap = new LinkedHashMap<String, String>();
            
            while (propertyNames.hasMoreElements()) {
                String propertyName = propertyNames.nextElement();
                if (beanWrapperImpl.isReadableProperty(propertyName))
                    validateProperty(propertyName, beanWrapperImpl, propertyName, errors, propertyName);

                //now check for collection properties
                String collectionPropertyName = PropertyUtil.getColletionPropertyName(propertyName);

                if (collectionPropertyName != null && beanWrapperImpl.isReadableProperty(collectionPropertyName) && propertyMap.get(propertyName) == null) {
                	//individual properties (items in collection)
                    propertyMap.put(collectionPropertyName, propertyName);
                    //collection properties.
                    collectionPropertyMap.put(PropertyUtil.getCollectionMethodName(collectionPropertyName), propertyName);
                }

            }

            //now validate the collection
            for (String collectionPropertyName : propertyMap.keySet()) {

                String propertyNameWhereErrorWillBeDisplayed = propertyMap.get(collectionPropertyName);
                //logger.info("Found collection property:" + collectionPropertyName + " and property name where error messages(if any) will be displayed:" + propertyNameWhereErrorWillBeDisplayed);
                String readMethodName = PropertyUtil.getCollectionMethodName(propertyNameWhereErrorWillBeDisplayed);
                validateCollectionProperty(readMethodName, beanWrapperImpl, collectionPropertyName, errors, propertyNameWhereErrorWillBeDisplayed);

            }
            
            //for UniqueObjectInCollection validator
            for(String collectionProperty : collectionPropertyMap.keySet()){
            	validateCollectionProperty(collectionProperty, beanWrapperImpl, collectionProperty, errors, collectionPropertyMap.get(collectionProperty));
            }
            
        }

    }

    private void validateCollectionProperty(String readMethodName, BeanWrapperImpl beanWrapperImpl, String propertyName, BindException errors, String propertyNameWhereErrorWillBeDisplayed) {
        if (readMethodName != null) {

            Annotation[] annotationsArray = beanWrapperImpl.getPropertyDescriptor(readMethodName).getReadMethod()
                    .getAnnotations();
            Object objectToValidate = beanWrapperImpl.getPropertyValue(propertyName);
            validate(annotationsArray, objectToValidate, propertyNameWhereErrorWillBeDisplayed, errors);
//            //validate for UniqueObjectInCollection validator also
//            validate(annotationsArray, beanWrapperImpl.getPropertyValue(readMethodName), propertyNameWhereErrorWillBeDisplayed, errors);
        }

    }

    /**
     *
     * @param readMethodName method name which has the annotation present. it will be the same as property name for normal (non collection) properties. 
     * @param beanWrapperImpl
     * @param propertyName name of the property
     * @param errors
     * @param propertyNameWhereErrorWillBeDisplayed name of the proeprty where errros should be displayed.
     */
    private void validateProperty(String readMethodName, BeanWrapperImpl beanWrapperImpl, String propertyName, BindException errors, String propertyNameWhereErrorWillBeDisplayed) {
        if (readMethodName != null) {

            Annotation[] annotationsArray = beanWrapperImpl.getPropertyDescriptor(readMethodName).getReadMethod()
                    .getAnnotations();
            Object objectToValidate = beanWrapperImpl.getPropertyValue(propertyName);
            validate(annotationsArray, objectToValidate, propertyNameWhereErrorWillBeDisplayed, errors);
        }

    }


    private void validate(Annotation[] annotationsArray, Object propertyValue, String errorPropertyName, BindException errors) {
        for (Annotation validatorAnnotation : annotationsArray) {
            Validator<Annotation> validator = createValidator(validatorAnnotation);
            //Object propertyValue = beanWrapperImpl.getPropertyValue(propertyName);

            if (validator != null) {
                logger.info("Found read property   property-value:" + propertyValue + " with validator :" + validator.getClass().getName() + ". Errors (if any) will be displayed on property-name:" + errorPropertyName);
                if (!validator.validate(propertyValue)) {
                    String errorCode = validator.message();
                    logger.info("Found error code:" + errorCode + " for property:" + errorPropertyName + " value:"
                            + propertyValue + " & validator:" + validator.getClass().getName());

                    errors.rejectValue(errorPropertyName, "REQUIRED", errorCode);
                }
            }
        }
    }

    /**
     * Creates the validator for a given annotation type.
     *
     * @param annotation the annotation
     * @return the validator
     */
    @SuppressWarnings("unchecked")
    private Validator<Annotation> createValidator(final Annotation annotation) {
        try {
            ValidatorClass validatorClass = annotation.annotationType().getAnnotation(ValidatorClass.class);
            if (validatorClass == null) {
                return null;
            }

            Map<String, Validator> validatorClasses = applicationContext.getBeansOfType(validatorClass.value());
            if (validatorClasses != null && !validatorClasses.isEmpty()) {
                Validator validator = validatorClasses.get(validatorClasses.keySet().iterator().next());
                validator.initialize(annotation);
                logger.info("found validator " + validator.getClass() + " for annotation:" + annotation.toString());
                return validator;
            }

        }
        catch (Exception e) {
            throw new IllegalArgumentException("could not instantiate ClassValidator", e);
        }
        logger.info("No validator found for annotation:" + annotation.toString());
        return null;
    }

    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private ApplicationContext applicationContext;

}
