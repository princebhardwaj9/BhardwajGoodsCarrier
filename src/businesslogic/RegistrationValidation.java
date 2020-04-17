package businesslogic;

import java.util.regex.Pattern;

public class RegistrationValidation {
	private boolean validPassword(String password, String confirmPassword) {
		String passwordPattern="((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,20})";
		Pattern pass=Pattern.compile(passwordPattern);
		if(password!=null && password.equals(confirmPassword) && pass.matcher(password).matches())
			return true;
		else
			return false;
	}
	
	private boolean validEmail(String email) {
		String emailContainer="[a-zA-Z0-9_.]*"+"@"+"[a-zA-Z0-9.]*";
		Pattern ePat=Pattern.compile(emailContainer);
		if(email!=null && ePat.matcher(email).matches())
			return true;
		else
			return false;
	}
	
	private boolean validmobileNumber(String mobilenumber) {
		String mobilenumberregex="[0-9]{10}";
		Pattern mobilepattern=Pattern.compile(mobilenumberregex);
		if(mobilenumber!=null && mobilepattern.matcher(mobilenumber).matches())
			return true;
		else
			return false;
	}
	
	public boolean checkUserDetails(String email,String mobilenumber, String password, String confirmPassword) {
		if(validEmail(email) && validPassword(password,confirmPassword) && validmobileNumber(mobilenumber))
			return true;
		else
			return false;
	}

}
