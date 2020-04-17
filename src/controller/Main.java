package controller;

import java.io.BufferedReader;


import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import businesslogic.LoginValidation;
import businesslogic.RegistrationValidation;
import businesslogic.TruckNameValidation;
import dao.AdminDAO;
import dao.CustomerDAO;
import model.Customer;
import model.Truck;
import service.AdminServices;
import service.CustomerServices;

public class Main {

	public static void main(String[] args) throws Exception {
		RegistrationValidation registration=new RegistrationValidation();
		//Console console=System.console();
		AdminDAO adminDAO=new AdminDAO();
		CustomerDAO customerDAO=new CustomerDAO();
		LoginValidation login=new LoginValidation();
		AdminServices adminServices=new AdminServices();
		CustomerServices customerservices=new CustomerServices();
		List<String> alltruck=new ArrayList<String>();
		List<String> getallcities=new ArrayList<String>();
		int choice;
		boolean adminbool,bool;
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		System.out.println("***************************BHARDWAj GOODS CARRiER******************************");
		System.out.println();
		System.out.println("1: ADMiN");
		System.out.println("2: CUSTOMER");
		choice=Integer.parseInt(br.readLine());
		switch(choice) {
		case 1:
			System.out.print("Enter Email: ");
			String adminemail=br.readLine();
			System.out.print("Enter Password: ");
			String adminpassword=br.readLine();
			if(login.getLogin("admin", adminemail, adminpassword)) {
				System.out.println();
				adminDAO.getTruckStatusWithType();
				System.out.println();
				do {
					System.out.println();
					System.out.println("1: SEE PENDiNG REQUESTS");
					System.out.println("2: Add New Truck");
					System.out.println("3: Add/Delete Route");
					System.out.println("4: Update Fare");
					System.out.println("5: Check All Truck Status");
					System.out.println("6: Delete Truck");
					System.out.println("7: TO EXiT/LOGOUT");
					choice=Integer.parseInt(br.readLine());
					System.out.println();
					if(choice==1) {
						adminServices.services();
						adminbool=true;
					}
					else if(choice==2) {
						System.out.print("Enter Truck Number: ");
						String trucknumber=br.readLine();
						System.out.print("Enter Truck Type: ");
						String trucktype=br.readLine();
						if(TruckNameValidation.checkTruckName(trucknumber)) {
							adminDAO.insertNewTruck(new Truck(trucknumber,trucktype));
						}
						else {
							System.out.println("Enter Valid Truck number");
						}
						adminbool=true;
					}
					else if(choice==3) {
						adminServices.routeUpdateServices();
						adminbool=true;
					}
					else if(choice==4) {
						System.out.println("For FARE Updation");
						System.out.print("Enter Pick Location: ");
						String farepicklocation=br.readLine();
						System.out.print("Enter Drop Location: ");
						String faredroplocation=br.readLine();
						System.out.print("Enter Updated Fare: ");
						String updatedfare=br.readLine();
						adminDAO.updateFare(farepicklocation, faredroplocation, updatedfare);
						adminbool=true;
					}
					else if(choice==5) {
						adminDAO.getTruckStatusWithType();
						adminbool=true;
					}
					else if(choice==6) {
						adminDAO.getTruck();
						System.out.println();
						System.out.print("Enter Truck Number You Want To Removed: ");
						String removetruck=br.readLine();
						adminDAO.deleteTruck(removetruck);
						adminbool=true;
					}
					else {
						System.out.println("LOGOUT");
						adminbool=false;
					}
				}while(adminbool);
			}
			break;
		//CUSTOMER
		case 2:
			System.out.println("1: Sign Up");
			System.out.println("2: Login");
			choice=Integer.parseInt(br.readLine());
			switch(choice) {
			case 1:
				System.out.print("Your Name");
				String name=br.readLine();
				System.out.print("Enter Email: ");
				String email=br.readLine();
				System.out.print("Enter Username: ");
				String username=br.readLine();
				System.out.print("Enter Mobile Number");
				String mobilenumber=br.readLine();
				System.out.print("Enter Password: ");
				String password=br.readLine();
				System.out.print("Confirm Password: ");
				String confirmpassword=br.readLine();
				if(registration.checkUserDetails(email, mobilenumber, password, confirmpassword)) 
					customerDAO.customerSignUp(new Customer(name,email,username,mobilenumber,password));
				else {
					System.out.println("Enter Valid Details");
					break;
				}
			case 2:
				System.out.print("Enter Email: ");
				String loginemail=br.readLine();
				System.out.print("Enter Password: ");
				String loginpassword=br.readLine();
				System.out.println();
				if(login.getLogin("customer", loginemail, loginpassword)) {
					alltruck=customerDAO.getAllType();
					System.out.println();
					System.out.print("TRUCK TYPES iN OUR SERViCES: [");
					String alltrucktype=String.join(",",alltruck);
					System.out.print(alltrucktype);
					System.out.println("]");
					System.out.println();
					getallcities=customerDAO.getAllCities();
					System.out.print("OUR SERViCES ARE iN FOLLOWiNG CiTiES: [");
					String allcities=String.join(",",getallcities);
					System.out.print(allcities);
					System.out.println("]");
					do {
						System.out.println();
						System.out.println("1: TO HIRE TRUCK");
						System.out.println("2: TO SEE PENDING REQUEST");
						System.out.println("3: TO SEE ALL REQUEST");
						System.out.println("4: FOR SUCCESSFULL DELiVERY");
						System.out.println("5: TO LOGOUT/EXIT");
						int decision=Integer.parseInt(br.readLine());
						System.out.println();
						if(decision==1) {
							customerservices.services(loginemail);
							bool=true;
						}
						else if(decision==2) {
							customerDAO.seePendingRequest(loginemail);
							bool=true;
						}
						else if(decision==3) {
							customerDAO.seeAllRequest(loginemail);
							bool=true;
						}
						else if(decision==4) {
							System.out.print("ENTER YOUR EMAiL: ");
							String okemail=br.readLine();
							System.out.print("ENTER VEHICLE REGiSTRATiON NUMBER: ");
							String oktrucknum=br.readLine();
							System.out.print("ENTER YOUR BOOKiNG iD: ");
							String okbookingid=br.readLine();
							customerDAO.deliverSuccessfully(okemail, oktrucknum, okbookingid);
							bool=true;
						}
						else {
							System.out.println("LOGOUT");
							bool=false;
						}
					}while(bool);
					break;
				}
			}
		}
	}
}