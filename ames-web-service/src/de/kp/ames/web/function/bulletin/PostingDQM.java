package de.kp.ames.web.function.bulletin;
/**
 *	Copyright 2012 Dr. Krusche & Partner PartG
 *
 *	AMES-Web-Service is free software: you can redistribute it and/or 
 *	modify it under the terms of the GNU General Public License 
 *	as published by the Free Software Foundation, either version 3 of 
 *	the License, or (at your option) any later version.
 *
 *	AMES- Web-Service is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * 
 *  See the GNU General Public License for more details. 
 *
 *	You should have received a copy of the GNU General Public License
 *	along with this software. If not, see <http://www.gnu.org/licenses/>.
 *
 */

import java.util.Date;
import java.util.List;

import org.freebxml.omar.client.xml.registry.infomodel.ExtrinsicObjectImpl;
import org.json.JSONArray;
import org.json.JSONObject;

import de.kp.ames.web.core.format.json.DateCollector;
import de.kp.ames.web.core.regrep.JaxrConstants;
import de.kp.ames.web.core.regrep.JaxrHandle;
import de.kp.ames.web.core.regrep.dqm.JaxrDQM;
import de.kp.ames.web.core.util.FileUtil;
import de.kp.ames.web.function.FncSQL;
import de.kp.ames.web.function.GuiFactory;
import de.kp.ames.web.function.GuiRenderer;
import de.kp.ames.web.function.IconCls;

public class PostingDQM extends JaxrDQM {

	/*
	 * Reference to default renderer
	 */
	private GuiRenderer renderer;
	
	public PostingDQM(JaxrHandle jaxrHandle) {
		super(jaxrHandle);
		
		/*
		 * Initialize renderer
		 */
		renderer = GuiFactory.getInstance().getRenderer();
		
	}
	
	/**
	 * Retrieve sorted list of postings that refere to a 
	 * specific recipient
	 * 
	 * @param recipient
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public String getPostings(String recipient, String start, String limit) throws Exception {
		
		/*
		 * Sort result by datetime
		 */
		DateCollector collector = new DateCollector();

		/*
		 * Retrieve SQL statement
		 */
		String sqlString = FncSQL.getSQLPostings_All(recipient);
		List<ExtrinsicObjectImpl> postings = getExtrinsicObjectsByQuery(sqlString);
		
		for (ExtrinsicObjectImpl posting:postings) {

			/*
			 * The raw posting is registered as the repository item
			 * of a certain extrinsic object
			 */
			FileUtil file = getRepositoryItem(posting);
			JSONObject jPosting = new JSONObject(new String(file.getFile()));

			/*
			 * Add metadata to JSON representation of
			 * a certain posting
			 */
			String uid = posting.getId();
			jPosting.put(JaxrConstants.RIM_ID, uid);

			Date lastModified = getLastModified(posting);
			jPosting.put(JaxrConstants.RIM_TIMESTAMP, lastModified);

			String author = getAuthor(posting);
			jPosting.put(JaxrConstants.RIM_AUTHOR, author);

			/*
			 * Add rendering information
			 */
			String iconParam = renderer.getIconParam();
			jPosting.put(iconParam, IconCls.POST);
				
			/*
			 * Sort postings by datetime
			 */
			collector.put(lastModified, jPosting);

		}
		
		/*
		 * Render result
		 */
		
		JSONArray jArray = new JSONArray(collector.values());
		return renderer.createGrid(jArray, start, limit);

	}
	
}