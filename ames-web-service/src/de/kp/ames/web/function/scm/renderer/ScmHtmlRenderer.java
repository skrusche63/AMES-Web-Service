package de.kp.ames.web.function.scm.renderer;

import java.io.StringWriter;

import org.json.JSONException;
import org.json.JSONObject;

import com.googlecode.jatl.Html;

import de.kp.ames.web.function.scm.model.ResultObject;
import de.kp.ames.web.function.scm.model.SuggestObject;

public class ScmHtmlRenderer {

	/*
	 * Suggest render methods
	 */
	
	public static  String getSuggestHtmlResult(SuggestObject scm) {
		StringWriter htmlWriter = new StringWriter();
		Html html = new Html(htmlWriter);
		html
			.div().classAttr("sg")
				// raw highlight injection, because it contains tags from Solr
				.span().classAttr("sg-t").raw(scm.getHighlightTextSuggest()).end()
				.p().classAttr("sg-dg")
					.span().classAttr("sg-dl").text("Related annotations:").end()
					.span().classAttr("sg-s").text(scm.getSynonyms()).end()
				.end()
			.endAll();
		String result = htmlWriter.getBuffer().toString();
		return result;
	}
	
	public static  String getSuggestHtmlGroupHeader(JSONObject jDoc) throws JSONException {
		StringWriter htmlWriter = new StringWriter();
		Html html = new Html(htmlWriter);
		html
			.div().classAttr("sgh")
				.span().classAttr("sgh-s")
					.text(jDoc.getString("hypernym"))
			.endAll();
		
		String htmlOut = htmlWriter.getBuffer().toString();
		return htmlOut;
	}


	/*
	 * Search result render methods
	 */
	public static  String getResultHtml(ResultObject scm) {
		StringWriter htmlWriter = new StringWriter();
		Html html = new Html(htmlWriter);
		html
			.div().classAttr("sg")
				// raw highlight injection, because it contains tags from Solr
				.span().classAttr("sgh-t").raw(scm.getHighlightTitle()).end()
				.p().classAttr("sg-dg")
					.span().classAttr("sg-dl").text("Description:").end()
					.span().classAttr("sg-d").text(scm.getHighlightDescription()).end()
			.endAll();
		return htmlWriter.getBuffer().toString();
	}


	public static JSONObject getFlagForMetric(String metricName, String metricLevel) throws Exception {
		JSONObject jMetric = new JSONObject();
		
		if (metricLevel.equals("low")) {
			jMetric.put("flag", "images/silk/flag_green.png");
			jMetric.put("flag_name", "flag_green");
			jMetric.put("alt", "green flag");
			if (metricName == "loc") {
				jMetric.put("range", "1-100"); 
			} else {
				jMetric.put("range", "1-5");
			}
		} else if (metricLevel.equals("medium")) {
			jMetric.put("flag", "images/silk/flag_yellow.png");
			jMetric.put("flag_name", "flag_yellow");
			jMetric.put("alt", "yellow flag");
			if (metricName == "loc") {
				jMetric.put("range", "100-200"); 
			} else {
				jMetric.put("range", "5-10");
			}

		} else if (metricLevel.equals("high")) {
			jMetric.put("flag", "images/silk/flag_red.png");
			jMetric.put("flag_name", "flag_red");
			jMetric.put("alt", "red flag");
			if (metricName == "loc") {
				jMetric.put("range", ">200"); 
			} else {
				jMetric.put("range", ">10");
			}

		}
		
		return jMetric;
	}
	public static String getResultHtmlDescription(final ResultObject scm, final boolean detailed) throws Exception {
		StringWriter htmlWriter;
		htmlWriter = new StringWriter();
		new Html(htmlWriter) {{
			div().classAttr("sg")
				.p().classAttr("sg-dg")
					.span().classAttr("sg-dl").text("SCM  Project:").end()
					.span().classAttr("sg-d").text(scm.getSource()).end()
				.end()
				.p().classAttr("sg-dg")
					.span().classAttr("sg-dl").text("Annotations:").end()
					.span().classAttr("sg-d").text(scm.getAnnotations()).end()
				.end()
				.p().classAttr("sg-dg")
					.span().classAttr("sg-dl").text("Metrics: Imported by others(").end()
					.span().classAttr("sg-d").text(scm.getMetricBacklinks().toUpperCase())
						.text(" " + getFlagForMetric("method", scm.getMetricBacklinks()).getString("range"))
						.img()
							.src(getFlagForMetric("backlink", scm.getMetricBacklinks()).getString("flag"))
							.height("16")
							.alt(getFlagForMetric("loc", scm.getMetricBacklinks()).getString("alt"))
						.end()
					.end()
					.span().classAttr("sg-dl").text(") / Method count(").end()
					.span().classAttr("sg-d").text(scm.getMetricMethodCount().toUpperCase())
						.text(" " + getFlagForMetric("method", scm.getMetricMethodCount()).getString("range"))
						.img()
							.src(getFlagForMetric("method", scm.getMetricMethodCount()).getString("flag"))
							.height("16")
							.alt(getFlagForMetric("loc", scm.getMetricMethodCount()).getString("alt"))
						.end()
					.end()	
					.span().classAttr("sg-dl").text(") / Lines of Code(").end()
					.span().classAttr("sg-d").text(scm.getMetricLOC().toUpperCase())
						.text(" " + getFlagForMetric("loc", scm.getMetricLOC()).getString("range"))
						.img()
							.src(getFlagForMetric("loc", scm.getMetricLOC()).getString("flag"))
							.height("16")
							.alt(getFlagForMetric("loc", scm.getMetricLOC()).getString("alt"))
							.end()		
					.end()
					.span().classAttr("sg-dl").text(")").end()
				.end();
			if (detailed) makeMethodList();
			endAll();
			done();
		}
			Html makeMethodList() throws Exception {
				p().classAttr("sg-dg")
					.span().classAttr("sg-dl").text("Methodnames:").end();
					if (scm.getMethodNames().size() > 0) {
			            ul();
			            for (String methodName : scm.getMethodNames()) {
		            		li().text(methodName).end();
						}
			            end();
					} else {
						span().classAttr("sg-d").text(" n/a").end();
					}
	            return end();
	        }
		};
		return htmlWriter.getBuffer().toString();
	}
	
}
