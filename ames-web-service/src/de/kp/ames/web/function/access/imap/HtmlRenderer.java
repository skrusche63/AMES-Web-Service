package de.kp.ames.web.function.access.imap;

public class HtmlRenderer {

	public static String mail2HTML(String from, String date, String subject, String body) {
    	 
		StringBuffer htmlDocument = new StringBuffer();
		htmlDocument.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
    	
		// html head
		htmlDocument.append("<html>\n");
		htmlDocument.append("<head><title>" + from + "</title>\n");
		
		htmlDocument.append("<style type=\"text/css\">");

		htmlDocument.append(".xchat-h {font:bold 16px tahoma, arial, helvetica, sans-serif;margin-top:16px;padding:8px 8px 16px 8px;white-space:normal;color:#2B477F;text-align:center;}\n");
		htmlDocument.append(".xchat-content {font:normal 11px tahoma, arial, helvetica, sans-serif;padding:8px;white-space:normal;color:#2B477F;}\n");
		htmlDocument.append("</style>\n");
		htmlDocument.append("</head>\n");

		// html body
		htmlDocument.append("<body>\n");
		htmlDocument.append("<div class=\"xchat-h\">From: " + from + "</div>");
		htmlDocument.append("<div class=\"xchat-h\">Date: " + date + "</div>");
		
		htmlDocument.append("<div class=\"xchat-content\"><b>Subject:</b><p>" + subject + "</p></div>");
		htmlDocument.append("<div style=\"margin:8px;border-bottom:1px solid #cccccc;\">&nbsp;</div>\n");
			
		htmlDocument.append("<div style=\"margin:8px;margin-left:0px;\">\n");
		htmlDocument.append("<div class=\"xchat-content\"><b>Message:</b><p>" + body + "</p></div>");
		
		htmlDocument.append("</div>\n");		
		htmlDocument.append("</body>\n");
		htmlDocument.append("</html>");
	
		return htmlDocument.toString();

	}
}
