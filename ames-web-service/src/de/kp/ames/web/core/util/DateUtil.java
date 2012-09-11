package de.kp.ames.web.core.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {

	public static String createTimeStamp(String format) {
		// "yyyyMMdd-HHmm"
		Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat(format);
	    String timestamp = sdf.format(cal.getTime());
		return timestamp;
	}

}
