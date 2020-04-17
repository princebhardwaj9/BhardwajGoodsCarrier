package businesslogic;

import java.util.regex.Pattern;

public class TruckNameValidation {
	public static boolean validName(String truckname) {
		String namepattern="[a-zA-Z]{2}"+"[0-9]{2}"+"[a-zA-Z]?"+"[0-9]{4}";
		Pattern name=Pattern.compile(namepattern);
		return name.matcher(truckname).matches();
	}
	
	public static boolean checkTruckName(String truckname) {
		if(validName(truckname))
			return true;
		else
			return false;
	}
}
