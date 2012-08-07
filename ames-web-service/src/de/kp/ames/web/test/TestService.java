package de.kp.ames.web.test;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.kp.ames.web.core.regrep.JaxrHandle;

public interface TestService {

	public void setJaxrHandle(JaxrHandle jaxrHandle);
	
	public JaxrHandle getJaxrHandle();

	public void execute(HttpServletRequest request, HttpServletResponse response, ServletContext context);

}
