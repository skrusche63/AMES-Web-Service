package de.kp.ames.web.http;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.http
 *  Module: RequestContext
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #context #http #request #web
 * </SemanticAssist>
 *
 */


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestContext {

	/*
	 * The HttpRequest associated with the actual request
	 */
	private HttpServletRequest request;

	/*
	 * The HttpResponse associated with the actual request
	 */
	private HttpServletResponse response;
	
	/*
	 * The ServletContext associated with the actual request
	 */
	private ServletContext context;

	public RequestContext(HttpServletRequest request, HttpServletResponse response) {
		this.request  = request;
		this.response = response;
	}
	
	/**
	 * @return
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * @param request
	 */
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * @return
	 */
	public HttpServletResponse getResponse() {
		return response;
	}

	/**
	 * @param response
	 */
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	/**
	 * @return
	 */
	public ServletContext getContext() {
		return context;
	}

	/**
	 * @param context
	 */
	public void setContext(ServletContext context) {
		this.context = context;
	}

}
