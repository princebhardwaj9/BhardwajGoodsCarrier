package dao;

/**
 * This class is used to performs various database operations by Admin.
 */

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Admin;
import model.Truck;
import utility.ConnectionManager;

public class AdminDAO {
	List<String> name=new ArrayList<String>();
	List<Truck> type=new ArrayList<Truck>();
	Connection con;
	
	//Admin SignUp function.
	public void adminSignUp(Admin admin) throws Exception {
		con=ConnectionManager.getConnection();
		Statement s1=con.createStatement();
		Statement s2=con.createStatement();
		//check that email already exist or not.
		ResultSet emailexist=s1.executeQuery("SELECT email from admindetail WHERE email='"+admin.getEmail()+"'");
		
		//check that username already exist or not.
		ResultSet userexist=s2.executeQuery("SELECT username from admindetail WHERE username='"+admin.getUsername()+"'");
		
		if(emailexist.next()) {
			System.out.println("--------------------Email already exists Please Login--------------------");
		}
		else if(userexist.next()) {
			System.out.println("--------------------Username already exists choose another--------------------");
		}
		else {
			PreparedStatement psinsert=con.prepareStatement("INSERT INTO admindetail VALUES(?,?,?)");
			psinsert.setString(1, admin.getEmail());
			psinsert.setString(2, admin.getUsername());
			psinsert.setString(3, admin.getPassword());
			psinsert.execute();
			con.close();
			System.out.println("------------------SignUp Successfully------------------");
		}
	}
	
	//used to assign the truck which customer request for & assign driver.
	public void updateRequests(String email,String sourcer,String destination,String trucknum,String drivername,String drivernum,String ownernum,String bookingid) throws Exception {
		con=ConnectionManager.getConnection();
		PreparedStatement st1=con.prepareStatement("SELECT email FROM requests WHERE email=?");
		PreparedStatement st2=con.prepareStatement("SELECT email,trucknum FROM requests WHERE email=? AND trucknum!='NULL'");
		st1.setString(1, email);
		st2.setString(1, email);
		ResultSet rs1=st1.executeQuery();
		ResultSet rs2=st2.executeQuery();
		if(rs2.next()) {
			System.out.println("REQUEST ALREADY UPDATED");
			con.close();
		}
		else if(rs1.next()) {
			PreparedStatement st=con.prepareStatement("UPDATE requests SET trucknum=? , drivername=? , drivernum=? , ownernum=? , bookingid=? WHERE email=? AND sourcer=? AND destination=?");
			st.setString(1, trucknum);
			st.setString(2, drivername);
			st.setString(3, drivernum);
			st.setString(4, ownernum);
			st.setString(5, bookingid);
			st.setString(6, email);
			st.setString(7, sourcer);
			st.setString(8, destination);
			st.execute();
			updateTruckAvailability(trucknum, "NotAvailable");
			updateLocationOnRequest(trucknum, sourcer);
			con.close();
		}
		else {
			System.out.println("NO SUCH EMAIL EXIST");
		}
	}
	
	//Stores new truck.
	public void insertNewTruck(Truck truck) throws Exception {
		con=ConnectionManager.getConnection();
		PreparedStatement checktruck=con.prepareStatement("SELECT truckname FROM truck WHERE truckname=?");
		checktruck.setString(1, truck.getTruckname());
		ResultSet rs=checktruck.executeQuery();
		if(rs.next()) {
			System.out.println("THE TRUCK NUMBER ("+truck.getTruckname()+") ALREADY EXiST");
			con.close();
		}
		else {
			PreparedStatement inserttruck=con.prepareStatement("INSERT INTO truck VALUES(?,?)");
			inserttruck.setString(1, truck.getTruckname());
			inserttruck.setString(2, truck.getType());
			inserttruck.execute();
			PreparedStatement inserttruckstatus=con.prepareStatement("INSERT INTO truckstatus VALUES(?,?,?)");
			inserttruckstatus.setString(1, truck.getTruckname());
			inserttruckstatus.setString(2, "Available");
			inserttruckstatus.setString(3, "Gurgaon");
			inserttruckstatus.execute();
			System.out.println("New Truck Inserted");
			con.close();
		}
	}
	
	//Store new route.
	public void insertNewRoute(String picklocation,String droplocation,int fare) throws Exception {
		con=ConnectionManager.getConnection();
		PreparedStatement checkroute=con.prepareStatement("SELECT picklocation,droplocation FROM fare WHERE picklocation=? AND droplocation=?");
		checkroute.setString(1, picklocation);
		checkroute.setString(2, droplocation);
		ResultSet rs=checkroute.executeQuery();
		if(rs.next()) {
			System.out.println("THiS ROUTE ALREADY EXiST");
			System.out.println("YOU CAN GO TO UPDATE FARE");
			con.close();
		}
		else {
			PreparedStatement ps=con.prepareStatement("INSERT INTO fare VALUES(?,?,?)");
			ps.setString(1, picklocation);
			ps.setString(2, droplocation);
			ps.setInt(3, fare);
			ps.execute();
			con.close();
			System.out.println("New route with fare inserted");
		}
	}
	
	//Remove route.
	public void deleteRoute(String picklocation,String droplocation) throws Exception {
		con=ConnectionManager.getConnection();
		PreparedStatement ps=con.prepareStatement("DELETE FROM fare WHERE picklocation=? AND droplocation=?");
		ps.setString(1, picklocation);
		ps.setString(2, droplocation);
		ps.execute();
		con.close();
		System.out.println("Route from "+picklocation+" to "+droplocation+" deleted.");
	}
	
	//Update fare of existing route.
	public void updateFare(String picklocation,String droplocation,String fare) throws Exception {
		con=ConnectionManager.getConnection();
		PreparedStatement ps1=con.prepareStatement("SELECT picklocation,droplocation FROM fare WHERE picklocation=? AND droplocation=?");
		ps1.setString(1, picklocation);
		ps1.setString(2, droplocation);
		ResultSet rs=ps1.executeQuery();
		if(rs.next()) {
			PreparedStatement ps2=con.prepareStatement("UPDATE fare SET fare=? WHERE picklocation=? AND droplocation=?");
			ps2.setString(1, fare);
			ps2.setString(2, picklocation);
			ps2.setString(3, droplocation);
			System.out.println("Fare Updated");
			con.close();
		}
		else {
			System.out.println("ROUTE NOT EXiST");
			System.out.println("GOTO ADD ROUTE");
			con.close();
		}
	}
	
	//Show all the trucks with their types & availabiity.
	public void getTruckStatusWithType() throws Exception {
		con=ConnectionManager.getConnection();
		Statement s1=con.createStatement();
		ResultSet rs=s1.executeQuery("SELECT truck.truckname,truck.type,truckstatus.availability,truckstatus.location FROM truck FULL OUTER JOIN truckstatus ON truck.truckname=truckstatus.truckname");
		System.out.println("TRUCK-NUMBER                TYPE                  STATUS                  LOCATION");
		System.out.println("----------------------------------------------------------------------------------");
		while(rs.next()) {
				System.out.println("  "+rs.getString(1)+"               "+rs.getString(2)+"               "+rs.getString(3)+"               "+rs.getString(4));
		}
		con.close();
	}
	
	//Remove the truck.
	public void deleteTruck(String trucknumber) throws Exception {
		con=ConnectionManager.getConnection();
		PreparedStatement checktruck=con.prepareStatement("SELECT truckname FROM truckstatus WHERE truckname=?");
		checktruck.setString(1, trucknumber);
		ResultSet rs=checktruck.executeQuery();
		if(rs.next()) {
			PreparedStatement deletetruckstatus=con.prepareStatement("DELETE FROM truckstatus WHERE truckname=?");
			deletetruckstatus.setString(1, trucknumber);
			deletetruckstatus.execute();
			PreparedStatement deletetruck=con.prepareStatement("DELETE FROM truck WHERE truckname=?");
			deletetruck.setString(1, trucknumber);
			deletetruck.execute();
			System.out.println("Truck Deleted");
			con.close();
		}
		else {
			System.out.println("PLEASE CHOOSE FROM ABOVE AVAiLABLE TRUCK");
			con.close();
		}
	}
	
	//Show all trucks registration number calling before removing the truck.
	public void getTruck() throws Exception {
		con=ConnectionManager.getConnection();
		Statement statement=con.createStatement();
		ResultSet rs=statement.executeQuery("SELECT truckname FROM truck");
		System.out.print("ALL TRUCK (  ");
		while(rs.next()) {
			System.out.print(rs.getString("truckname")+"  ");
		}
		System.out.println(")");
		con.close();
	}
	
	//Show all pending requests which are not updated yet.
	public void seeRequests() throws Exception {
		con=ConnectionManager.getConnection();
		Statement st=con.createStatement();
		ResultSet rs=st.executeQuery("SELECT * FROM requests WHERE trucknum='NULL'");
		while(rs.next()) {
			System.out.println("EMAiL:"+rs.getString("email")+"  SOURCE:"+rs.getString("sourcer")+"  DESTiNATiON:"+rs.getString("destination")+"  TYPE:"+rs.getString("trucktype")+"  BOOKiNGiD:"+rs.getString("bookingid"));
		}
		con.close();
	}
	
	//Set the truck availability status.
	public void updateTruckAvailability(String trucknumber,String status) throws Exception{
		con=ConnectionManager.getConnection();
		PreparedStatement ps=con.prepareStatement("UPDATE truckstatus SET availability=? WHERE truckname=?");
		ps.setString(1, status);
		ps.setString(2, trucknumber);
		ps.execute();
		con.close();
		System.out.println("("+trucknumber+") iS BOOKED.");
	}
	
	//Update the truck location automatically on update request.
		public void updateLocationOnRequest(String trucknumber, String location) throws Exception {
			con=ConnectionManager.getConnection();
			PreparedStatement ps2=con.prepareStatement("UPDATE truckstatus SET location=? WHERE truckname=?");
			ps2.setString(1, location);
			ps2.setString(2, trucknumber);
			ps2.execute();
			con.close();
			System.out.println(trucknumber+" iS NOW LOCATED iN "+location);
		}
	
	//Update the truck location Separately.
	public void updateLocation(String trucknumber, String location) throws Exception {
		con=ConnectionManager.getConnection();
		PreparedStatement ps1=con.prepareStatement("SELECT trucknum FROM requests WHERE trucknum=? AND delivered='NO'");
		ps1.setString(1, trucknumber);
		ResultSet rs=ps1.executeQuery();
		if(rs.next()) {
			System.out.println("YOU CAN NOT UPDATE LOCATiON");
			con.close();
		}
		else {
			PreparedStatement ps2=con.prepareStatement("UPDATE truckstatus SET location=? WHERE truckname=?");
			ps2.setString(1, location);
			ps2.setString(2, trucknumber);
			ps2.execute();
			con.close();
			System.out.println(trucknumber+" iS NOW LOCATED iN "+location);
		}
	}
}
