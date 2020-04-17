package service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import businesslogic.LoginValidation;
import dao.AdminDAO;

public class AdminServices {
	LoginValidation login=new LoginValidation();
	AdminDAO adminDAO=new AdminDAO();
	int choice;
	boolean bool;
	BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
	public void services() throws Exception {
		do {
			System.out.println();
			System.out.println("ALL PENDING REQUESTS:");
			adminDAO.seeRequests();
			System.out.println();
			System.out.print("DO YOU WANT TO UPDATE REQUEST: ");
			System.out.println("TYPE Y/N");
			String doyou=br.readLine();
			if(doyou.equalsIgnoreCase("YES")||doyou.equalsIgnoreCase("Y")){
				System.out.print("Enter Email of Customer: ");
				String reqemail=br.readLine();
				System.out.print("Enter Source: ");
				String reqsource=br.readLine();
				System.out.print("Enter Destination: ");
				String reqdestination=br.readLine();
				System.out.print("Enter Truck Number: ");
				String reqtrucknumber=br.readLine();
				System.out.print("Enter Driver Name: ");
				String reqdrivername=br.readLine();
				System.out.print("Enter Driver Contact Number: ");
				String reqdrivernumber=br.readLine();
				System.out.print("Enter Owner Contact Number: ");
				String reqownernumber=br.readLine();
				System.out.print("Enter Booking ID: ");
				String reqbookingid=br.readLine();
				adminDAO.updateRequests(reqemail,reqsource,reqdestination,reqtrucknumber, reqdrivername, reqdrivernumber, reqownernumber, reqbookingid);
				bool=true;
			}
			else
				bool=false;
		}while(bool);
	}
	
	public void routeUpdateServices() throws Exception {
		boolean bool=false;
		do {
			System.out.println();
			System.out.println("1: FOR ADD NEW ROUTE");
			System.out.println("2: FOR DELETE ROUTE");
			System.out.println("3: FOR EXiT");
			int choice=Integer.parseInt(br.readLine());
			if(choice==1) {
				System.out.print("Enter Pick Location: ");
				String picklocation=br.readLine();
				System.out.print("Enter Drop Location: ");
				String droplocation=br.readLine();
				System.out.print("Enter Fare For This Route:");
				int fare=Integer.parseInt(br.readLine());
				adminDAO.insertNewRoute(picklocation, droplocation, fare);
				bool=true;
			}
			else if(choice==2) {
				System.out.print("Enter Pick Location: ");
				String picklocation=br.readLine();
				System.out.print("Enter Drop Location: ");
				String droplocation=br.readLine();
				adminDAO.deleteRoute(picklocation, droplocation);
				bool=true;
			}
			else {
				bool=false;
			}
		}while(bool);
	}
}