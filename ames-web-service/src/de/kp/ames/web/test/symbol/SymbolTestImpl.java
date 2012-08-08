package de.kp.ames.web.test.symbol;

import java.util.HashMap;

import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.service.Service;
import de.kp.ames.web.function.symbol.SymbolServiceImpl;
import de.kp.ames.web.shared.constants.ClassificationConstants;
import de.kp.ames.web.shared.constants.FormatConstants;
import de.kp.ames.web.shared.constants.MethodConstants;
import de.kp.ames.web.test.JaxrTestImpl;
import de.kp.ames.web.test.TestData;

public class SymbolTestImpl extends JaxrTestImpl {

	public SymbolTestImpl() {
		super();
	}

	public SymbolTestImpl(JaxrHandle jaxrHandle, String methodName) {
		super(jaxrHandle, methodName);
	}

	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getService()
	 */
	public Service getService() {
		return new SymbolServiceImpl();
	}
	
	/* (non-Javadoc)
	 * @see de.kp.ames.web.test.JaxrTestImpl#getGetAttributes()
	 */
	public HashMap<String,String> getGetAttributes() {

		HashMap<String,String> attributes = new HashMap<String,String>();
		attributes.put(MethodConstants.ATTR_TYPE, ClassificationConstants.FNC_SYMBOL_ID_APP6B);

		attributes.put(MethodConstants.ATTR_FORMAT, FormatConstants.FNC_FORMAT_ID_Tree);

		String parent = TestData.getInstance().getIdentifier(ClassificationConstants.FNC_SYMBOL_ID_APP6B);
		attributes.put(MethodConstants.ATTR_PARENT, parent);

		return attributes;
		
	}

}
