/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.audit;

import gov.nih.nci.cabig.caaers.CaaersTestCase;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
/**
 * 
 * @author Biju Joseph
 *
 */
public class AuditInfoPopulatorInterceptorTest extends CaaersTestCase {
	
	AuditInfoPopulatorInterceptor auditInterceptor;
	
	protected void setUp() throws Exception {
		super.setUp();
		auditInterceptor = (AuditInfoPopulatorInterceptor) getDeployedApplicationContext().getBean("auditInfoPopulatorInterceptor");
	}

	public void testInvoke() throws Throwable {

		MethodInvocation method = new MethodInvocation(){

			public Method getMethod() {
				// TODO Auto-generated method stub
				return null;
			}

			public Object[] getArguments() {
				// TODO Auto-generated method stub
				return null;
			}

			public AccessibleObject getStaticPart() {
				// TODO Auto-generated method stub
				return null;
			}

			public Object getThis() {
				// TODO Auto-generated method stub
				return null;
			}

			public Object proceed() throws Throwable {
				return gov.nih.nci.cabig.ctms.audit.DataAuditInfo.getLocal() != null;
			}
			
		};
		
		
		Object retVal = auditInterceptor.invoke(method);
		assertNotNull(retVal);
		assertEquals(Boolean.TRUE, retVal);
	}
	
	public void testInvoke_ThrowingException() throws Throwable {
		final Exception e = new RuntimeException("test");
		MethodInvocation method = new MethodInvocation(){

			public Method getMethod() {
				// TODO Auto-generated method stub
				return null;
			}

			public Object[] getArguments() {
				// TODO Auto-generated method stub
				return null;
			}

			public AccessibleObject getStaticPart() {
				// TODO Auto-generated method stub
				return null;
			}

			public Object getThis() {
				// TODO Auto-generated method stub
				return null;
			}

			public Object proceed() throws Throwable {
				if(true) throw e;
				return gov.nih.nci.cabig.ctms.audit.DataAuditInfo.getLocal() != null;
			}
			
		};
		try {
			auditInterceptor.invoke(method);
			fail("must throw exception");
		} catch (Exception e1) {
			assertSame(e, e1);
		}
		
	}

}
