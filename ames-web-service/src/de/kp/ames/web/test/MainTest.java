package de.kp.ames.web.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.kp.ames.http.HttpClient;

import junit.framework.TestCase;

/*
 * This test case invokes all existing functional test cases
 * or suites from a predefined https request that uses a certain
 * end user certificate.
 * 
 * Note, that the test user must be a registered user and
 * known to the user management
 */
public class MainTest extends TestCase {
	
	private String root = "de.kp.ames.web.test.";
	
	/*
	 * List of test package names
	 */
	private String[] names = {
		"access",
		"bulletin",
		"comm",
		"dms",
		"group",
		"map",
		"ns",
		"product",
		"role",
		"rule",
		"symbol",
		"transform",
		"upload",
		"user"		
	};
	
	
	private static String BASE_PATH = getBasePath();
	private static String BASE_URL = "https://localhost:8443/ames/test/unit?name=";
	
	
	/**
	 * This method determines the main entry point to
	 * all ADF related test cases or suites
	 * 
	 * @throws Exception
	 */
	public void test() throws Exception {
		
		for (String name:names) {
			
			/*
			 * Build package name
			 */
			String packageName = root + name;
			doPackageTest(packageName);
			
		}
		
	}

	/**
	 * A helper method to invoke all package related test cases
	 * and suites
	 * 
	 * @param packageName
	 * @throws Exception
	 */
	private void doPackageTest(String packageName) throws Exception {


		/*
		 * Determine all test classes of a certain test package
		 */
		List<String> clazzNames = findTestClasses(packageName);
		for (String clazzName : clazzNames) {

			System.out.println("Test Class: " + clazzName);

			/* 
			 * Invoke HttpsClient
			 */
			HttpClient client = new HttpClient();
			client.doGet(BASE_URL + clazzName);
		
		}

	}

	/**
	 * A helper method to find all Test Classes in a certain test package
	 * 
	 * @param packageName
	 * @return
	 * @throws ClassNotFoundException
	 */
	private List<String> findTestClasses(String packageName) throws ClassNotFoundException {

		List<String> classes = new ArrayList<String>();

		String relativePath = packageName.replace(".", "/");

		File directory = new File(BASE_PATH + "/" + relativePath);
		if (!directory.exists()) return classes;

		File[] files = directory.listFiles();

		for (File file : files) {

			if (file.isDirectory()) continue;

			String relativeClassName = file.getName().substring(0, file.getName().length() - 5);
			classes.add(packageName + '.' + relativeClassName);

		}

		return classes;
	
	}
	
	/**
	 * A helper method to retrieve the base
	 * path to the resource files
	 * 
	 * @return
	 */
	private static String getBasePath() {
		
	  File f = new File("");
      return f.getAbsolutePath() + "/src";		
	
	}

}
