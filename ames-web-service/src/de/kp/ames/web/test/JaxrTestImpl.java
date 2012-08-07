package de.kp.ames.web.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import de.kp.ames.web.core.regrep.JaxrHandle;

public class JaxrTestImpl extends TestCase implements JaxrTest {

	protected JaxrHandle jaxrHandle;

	public JaxrTestImpl() {
	};

	public Test suite(JaxrHandle jaxrHandle, String className) throws Exception {
		
		TestSuite suite = new TestSuite();

		Class<?> clazz = Class.forName(className);
		Method[] methods = clazz.getMethods();
		
		Class<?>[] paramTypes = { String.class, JaxrHandle.class };
		
		for (Method method : methods) {
			
			if (method.getName().startsWith("test")) {

				Constructor<?> constructor = clazz.getConstructor(paramTypes);

				Object[] params = { method.getName(), jaxrHandle };
				suite.addTest((Test) constructor.newInstance(params));

			}
		}

		return suite;

	}
	
}
