package de.kp.ames.web.function.scm;

import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;

/**
 * ScmDQM supports discovery of source code
 * modules that are registered as ExternalLinks
 * 
 * @author Stefan Krusche (krusche@dr-kruscheundpartner.de)
 *
 */
public class ScmDQM extends JaxrDQM {

	public ScmDQM(JaxrHandle requestHandle) {
		super(requestHandle);
	}

	public String explore(String parent) {
		return null;
	}
}
