package businesslogic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import utility.ConnectionManager;

public class LoginValidation {
	boolean allowed;
	Connection con;
	public boolean getLogin(String who,String email,String password) throws Exception {
		con=ConnectionManager.getConnection();
		Statement statement=con.createStatement();
		ResultSet rs=statement.executeQuery("SELECT email,password FROM "+who+"detail");
		while(rs.next()) {
			if(rs.getString("email").equals(email)) {
				if(rs.getString("password").equals(password)) {
					allowed=true;
					}
//				else {
//					System.out.println("Invalid Password");
//				}
			}
//			else {
//				System.out.println("Invalid Email");
//			}
		}
		con.close();
		return allowed;
	}
}
