package de.kp.ames.web.test;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.test
 *  Module: TestServiceImpl
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


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tools.ant.taskdefs.optional.junit.JUnitTest;
import org.apache.tools.ant.taskdefs.optional.junit.XMLJUnitResultFormatter;

import junit.framework.Test;
import junit.framework.TestResult;

import de.kp.ames.web.core.regrep.JaxrHandle;

public class TestServiceImpl implements TestService {
	
	private String JUNIT_XML_DIR = getBasePath();
	
	/*
	 * JaxrHandle is a temporary data structure
	 * that holds all connection specific information
	 */
	protected JaxrHandle jaxrHandle;

	public void setJaxrHandle(JaxrHandle jaxrHandle) {
		this.jaxrHandle = jaxrHandle;
	}
	
	public JaxrHandle getJaxrHandle() {
		return this.jaxrHandle;
	}

	/*
	 * This method repesents the entry point to request parameter based
	 * processing
	 */
	public void execute(HttpServletRequest request, HttpServletResponse response, ServletContext context) {

		// prepare response
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

		String clazzName = request.getParameter("name");
		if (clazzName == null) return;

		try {
			/*
			 * Prepare test result
			 */
			TestResult result = new TestResult();
			XMLJUnitResultFormatter resultFormatter = new XMLJUnitResultFormatter();

			JUnitTest dummyJUnit = new JUnitTest(clazzName);
			resultFormatter.startTestSuite(dummyJUnit);

			String reportFile = JUNIT_XML_DIR + "/" + clazzName + ".xml";

			OutputStream writer = new FileOutputStream(new File(reportFile));
			
			resultFormatter.setOutput(writer);
			result.addListener(resultFormatter);

						
			Class<?> clazz = Class.forName(clazzName);
			Test suite = ((JaxrTest) clazz.newInstance()).suite(jaxrHandle, clazzName);

			long startTime = new Date().getTime();
			suite.run(result);
			long endTime = new Date().getTime();

			dummyJUnit.setCounts(result.runCount(), result.failureCount(), result.errorCount());
			dummyJUnit.setRunTime(endTime - startTime);

			resultFormatter.endTestSuite(dummyJUnit);
			writer.close();

			buildResponse("TestRunner: " + clazzName, "text/plain", response);

		} catch (Exception e) {
			// catch all
			e.printStackTrace();
		}

	}

	private void buildResponse(String content, String mimetype, HttpServletResponse response) throws IOException {

		// set http status
		response.setStatus(HttpServletResponse.SC_OK);
		response.setCharacterEncoding("UTF-8");

		response.setContentType(mimetype);

		byte[] bytes = content.getBytes("UTF-8");
		response.setContentLength(bytes.length);

		OutputStream os = response.getOutputStream();

		os.write(bytes);
		os.close();

	}

	/**
	 * A helper method to retrieve the base
	 * path to the resource files
	 * 
	 * @return
	 */
	private static String getBasePath() {
		
		File f = new File("");
		return f.getAbsolutePath() + "/test-result";		
	
	}


}
