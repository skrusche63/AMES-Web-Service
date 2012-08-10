package de.kp.ames.web.test;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.test
 *  Module: TestService
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #service #test #web
 * </SemanticAssist>
 *
 */


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.kp.ames.web.core.regrep.JaxrHandle;

public interface TestService {

	public void setJaxrHandle(JaxrHandle jaxrHandle);
	
	public JaxrHandle getJaxrHandle();

	public void execute(HttpServletRequest request, HttpServletResponse response, ServletContext context);

}
