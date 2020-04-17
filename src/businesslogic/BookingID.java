package businesslogic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookingID {
	public static String generateBookingID(String email,String source,String destination) {
		String result="";
		String regex="(\\w+)@";
		Pattern p=Pattern.compile(regex);
		Matcher m=p.matcher(email);
		if(m.find()) {
			result=m.group(1).toLowerCase()+source.substring(0, 3).toUpperCase()+destination.substring(0, 3).toUpperCase();
		}
		return result;
		
	}

}
