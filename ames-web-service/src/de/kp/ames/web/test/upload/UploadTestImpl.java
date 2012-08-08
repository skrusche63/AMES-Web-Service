package de.kp.ames.web.test.upload;

import java.util.HashMap;

import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.service.Service;
import de.kp.ames.web.function.upload.UploadServiceImpl;
import de.kp.ames.web.shared.constants.ClassificationConstants;
import de.kp.ames.web.shared.constants.FormatConstants;
import de.kp.ames.web.shared.constants.MethodConstants;
import de.kp.ames.web.test.JaxrTestImpl;
import de.kp.ames.web.test.TestData;

public class UploadTestImpl extends JaxrTestImpl {

	// TODO: Set Request
	
	public UploadTestImpl() {
		super();
	}

	public UploadTestImpl(JaxrHandle jaxrHandle, String methodName) {
		super(jaxrHandle, methodName);
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getService()
	 */
	public Service getService() {
		return new UploadServiceImpl();
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getDeleteAttributes()
	 */
	public HashMap<String,String> getDeleteAttributes() {

		HashMap<String,String> attributes = new HashMap<String,String>();
		attributes.put(MethodConstants.ATTR_TYPE, ClassificationConstants.FNC_ID_Document);

		/*
		 * Test Document
		 */
		String item = TestData.getInstance().getIdentifier(ClassificationConstants.FNC_ID_Document);
		attributes.put(MethodConstants.ATTR_ITEM, item);

		return attributes;
		
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getGetAttributes()
	 */
	public HashMap<String,String> getGetAttributes() {

		/*
		 * This test case retrieves a single existing document
		 */
		HashMap<String,String> attributes = new HashMap<String,String>();
		attributes.put(MethodConstants.ATTR_TYPE, ClassificationConstants.FNC_ID_Document);

		/*
		 * Test Document
		 */
		attributes.put(MethodConstants.ATTR_FORMAT, FormatConstants.FNC_FORMAT_ID_File);

		String item = TestData.getInstance().getIdentifier(ClassificationConstants.FNC_ID_Document);
		attributes.put(MethodConstants.ATTR_ITEM, item);

		return attributes;
		
	}

}
