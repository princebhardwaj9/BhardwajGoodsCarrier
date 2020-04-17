package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Customer;
import utility.ConnectionManager;

public class CustomerDAO {
	List<String> truckavailable=new ArrayList<String>();
	List<String> allcities=new ArrayList<String>();
	List<String> gettruck=new ArrayList<String>();
	Connection con;
	
	public void customerSignUp(Customer customer) throws Exception {
		con=ConnectionManager.getConnection();
		Statement s1=con.createStatement();
		ResultSet emailexist=s1.executeQuery("SELECT email FROM customerdetail WHERE email='"+customer.getEmail()+"'");
		Statement s2=con.createStatement();
		ResultSet usernameexist=s2.executeQuery("SELECT username FROM customerdetail WHERE username='"+customer.getUsername()+"'");
		PreparedStatement ps=con.prepareStatement("INSERT INTO customerdetail VALUES(?,?,?,?,?)");
		if(emailexist.next()) {
			System.out.println("--------------------Email already exist Please Login--------------------");
		}
		else if(usernameexist.next()) {
			System.out.println("--------------------Username exist choose another--------------------");
		}
		else {
			ps.setString(1, customer.getName());
			ps.setString(2, customer.getEmail());
			ps.setString(3, customer.getUsername());
			ps.setString(4, customer.getMobilenumber());
			ps.setString(5, customer.getPassword());
			ps.execute();
			con.close();
			System.out.println("--------------------SignUp Successfully--------------------");
		}
	}
	
	public List<String> getAllType() throws Exception {
		con=ConnectionManager.getConnection();
		Statement statement=con.createStatement();
		ResultSet rs=statement.executeQuery("SELECT DISTINCT type FROM truck");
		while(rs.next()) {
			gettruck.add(rs.getString("type"));
		}
		return gettruck;
	}
	
	public List<String> getAllCities() throws Exception {
		con=ConnectionManager.getConnection();
		Statement statement=con.createStatement();
		ResultSet rs=statement.executeQuery("SELECT * FROM locationtable");
		while(rs.next()) {
			allcities.add(rs.getString(1));
		}
		return allcities;
	}
	
	public List<String> getAvailableTruck() throws Exception {
		con=ConnectionManager.getConnection();
		Statement statement=con.createStatement();
		ResultSet rs=statement.executeQuery("SELECT DISTINCT type FROM truck WHERE truckname IN (SELECT truckname FROM truckstatus WHERE availability='Available')");
			while(rs.next()) {
				truckavailable.add(rs.getString("type"));
			}
		return truckavailable;
	}
	
	public long getFare(String source,String destination) throws Exception {
		long fare=0;
		con=ConnectionManager.getConnection();
		Statement statement=con.createStatement();
		ResultSet rs=statement.executeQuery("SELECT fare FROM fare WHERE picklocation='"+source+"' AND droplocation='"+destination+"'");
		if(rs.next()) {
			System.out.println("Fare for this route is "+rs.getString("fare")+" rupees.");
			fare=Integer.parseInt(rs.getString("fare"));
		}
		else if(source.equals(destination)) {
			System.out.println("Fare for same city is 5000 rupees.");
			fare=5000;
		}
		else {
			System.out.println("Enter route from above available cities");
		}
		return fare;
	}
	
	public void yesRequest(String email,String source,String destination,String type,String bookingid) throws Exception {
		con=ConnectionManager.getConnection();
		PreparedStatement pscheck=con.prepareStatement("SELECT email,sourcer,destination,bookingid FROM requests WHERE email=? AND sourcer=? AND destination=? AND bookingid=?");
		pscheck.setString(1, email);
		pscheck.setString(2, source);
		pscheck.setString(3, destination);
		pscheck.setString(4, bookingid);
		ResultSet rs=pscheck.executeQuery();
		if(rs.next()) {
			System.out.println("BOOKiNG ALREADY EXiST");
			System.out.println("CHECK WiTH BOOKiNG iD: "+bookingid);
		}
		else {
			PreparedStatement ps=con.prepareStatement("INSERT INTO requests VALUES(?,?,?,?,?,?,?,?,?,?)");
			ps.setString(1, email);
			ps.setString(2, source);
			ps.setString(3, destination);
			ps.setString(4, type);
			ps.setString(5, "NULL"); //truckno
			ps.setString(6, "NULL"); //drivername
			ps.setString(7, "NULL"); //driverno
			ps.setString(8, "NULL"); //ownerno
			ps.setString(9, bookingid); //uid
			ps.setString(10, "NO"); //delivered
			ps.execute();
			con.close();
			System.out.println("YOUR REQUEST HAS BEEN SENT");
			System.out.println("PLEASE NOTE YOUR BOOKiNG iD FOR ALL REFERENCES CASE SENSiTiVE: "+bookingid);
		}
	}
	
	public void deliverSuccessfully(String email,String trucknum,String bookingid) throws Exception {
		con=ConnectionManager.getConnection();
		Statement s1=con.createStatement();
		ResultSet rs=s1.executeQuery("SELECT email,sourcer,destination,trucknum,drivername,bookingid FROM requests WHERE trucknum='"+trucknum+"'");
		if(rs.next()) {
			long fare=getFare(rs.getString("sourcer"),rs.getString("destination"));
			PreparedStatement ps1=con.prepareStatement("UPDATE requests SET delivered=? WHERE trucknum=?");
			ps1.setString(1, "YES");
			ps1.setString(2, trucknum);
			ps1.execute();
			PreparedStatement ps2=con.prepareStatement("UPDATE truckstatus SET availability=? WHERE truckname=?");
			ps2.setString(1, "Available");
			ps2.setString(2, trucknum);
			ps2.execute();
			PreparedStatement ps3=con.prepareStatement("UPDATE truckstatus SET location=? WHERE truckname=?");
			ps3.setString(1, rs.getString("destination"));
			ps3.setString(2, trucknum);
			ps3.execute();
			PreparedStatement ps4=con.prepareStatement("INSERT INTO alldeliveries VALUES(?,?,?,?,?,?,?)");
			ps4.setString(1, rs.getString("email"));
			ps4.setString(2, rs.getString("trucknum"));
			ps4.setString(3, rs.getString("drivername"));
			ps4.setString(4, rs.getString("sourcer"));
			ps4.setString(5, rs.getString("destination"));
			ps4.setLong(6, fare);
			ps4.setDate(7, Date.valueOf(LocalDate.now()));
			ps4.execute();
			PreparedStatement ps5=con.prepareStatement("DELETE FROM requests WHERE trucknum=?");
			ps5.setString(1, rs.getString("trucknum"));
			ps5.execute();
			System.out.println("THANK YOU!");
			con.close();
		}
		else {
			System.out.println("NO SUCH DATA EXIST");
			con.close();
		}
	}
	
//	public void seeRequestStatus(String bookingid) throws Exception {
//		con=ConnectionManager.getConnection();
//		Statement statement=con.createStatement();
//		ResultSet rs=statement.executeQuery("SELECT sourcer,destination,trucknum,drivername,drivernum,ownernum FROM requests WHERE bookingid='"+bookingid+"'");
//		while(rs.next()) {
//			if(rs.getString("trucknum").equals("NULL")) {
//				System.out.println("YOUR REQUEST iS STiLL PENDiNG");
//			}
//		else {	
//				System.out.println("The vehicle no:["+rs.getString("trucknum")+"] from "+rs.getString("sourcer")+" to "+rs.getString("destination")+" has alloted.");
//				System.out.println("Driver Name: "+rs.getString("drivername")+" contact no:"+rs.getString("drivernum"));
//				System.out.println("For any other information feel free to call on "+rs.getString("ownernum"));
//			}
//		}
//		con.close();
//	}
	
	public void seePendingRequest(String email) throws Exception{
		int i=0;
		con=ConnectionManager.getConnection();
		PreparedStatement statement=con.prepareStatement("SELECT sourcer,destination,bookingid FROM requests WHERE email=? AND trucknum='NULL' AND delivered='NO'");
		statement.setString(1, email);
		ResultSet rs=statement.executeQuery();
		System.out.println("PENDiNG REQUESTS:");
		while(rs.next()) {
			System.out.println(++i+") SOURCE:"+rs.getString("sourcer")+"   DESTiNATiON:"+rs.getString("destination")+"   BOOKiNGiD:"+rs.getString("bookingid"));
		}
	}
	
	public void seeAllRequest(String email) throws Exception{
		con=ConnectionManager.getConnection();
		PreparedStatement statement1=con.prepareStatement("SELECT sourcer,destination,bookingid FROM requests WHERE email=? AND trucknum='NULL'");
		statement1.setString(1, email);
		ResultSet rs1=statement1.executeQuery();
		while(rs1.next()) {
			System.out.println("PENDiNG>>> SOURCE:"+rs1.getString("sourcer")+"    DESTiNATiON:"+rs1.getString("destination")+"    BOOKiNGiD:"+rs1.getString("bookingid"));
		}
		System.out.println();
		PreparedStatement statement2=con.prepareStatement("SELECT sourcer,destination,trucktype,trucknum,drivername,drivernum,bookingid FROM requests WHERE trucknum!='NULL' AND email=?");
		statement2.setString(1, email);
		ResultSet rs2=statement2.executeQuery();
		while(rs2.next()) {
			System.out.println("ON THE WAY: Vehicle no:["+rs2.getString("trucknum")+"] from "+rs2.getString("sourcer")+" to "+rs2.getString("destination")+" has alloted.");
			System.out.println("Driver Name: "+rs2.getString("drivername")+" contact no:"+rs2.getString("drivernum"));
		}
	}

}
