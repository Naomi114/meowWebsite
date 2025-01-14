package tw.com.ispan.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatetimeConverter {

	// 將 LocalDateTime 格式化為字串
	public static String toString(LocalDateTime datetime, String format) {
		String result = "";
		try {
			if (datetime != null) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
				result = datetime.format(formatter);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// 將字串解析為 LocalDateTime
	public static LocalDateTime parse(String datetime, String format) {
		LocalDateTime result = null;
		try {
			if (datetime != null && !datetime.isEmpty()) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
				result = LocalDateTime.parse(datetime, formatter);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
