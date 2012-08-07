package de.kp.ames.web.test;

import junit.framework.Test;
import de.kp.ames.web.core.regrep.JaxrHandle;

public interface JaxrTest {

	public Test suite(JaxrHandle jaxrHandle, String className) throws Exception;

}
