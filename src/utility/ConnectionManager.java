package utility;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectionManager {
	
	public static Connection getConnection() throws Exception {
		Properties p=loadPropertiesFile();
		Class.forName(p.getProperty("driver"));
		Connection con=null;
		con=DriverManager.getConnection(p.getProperty("url"),p.getProperty("username"),p.getProperty("password"));
		if(con!=null)
			return con;
		else
			return null;
	}
	
	public static Properties loadPropertiesFile() throws Exception {
		Properties p=new Properties();
		InputStream in=ConnectionManager.class.getClassLoader().getResourceAsStream("jdbc.properties");
		p.load(in);
		in.close();
		return p;
	}
}
