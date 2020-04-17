package service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import businesslogic.BookingID;
import dao.CustomerDAO;

public class CustomerServices {
	CustomerDAO customerDAO=new CustomerDAO();
	List<String> getavailabletype=new ArrayList<String>();
	BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
	boolean customerbool;
	public void services(String email) throws Exception {
		System.out.print("Enter Type: ");
		String type=br.readLine();
		getavailabletype=customerDAO.getAvailableTruck();
		if(getavailabletype.contains(type)) {
			do {
				System.out.print("Enter Source: ");
				String source=br.readLine();
				System.out.print("Enter Destination: ");
				String destination=br.readLine();
				customerDAO.getFare(source, destination);
				System.out.println("DO YOU WANT TO BOOK OR SET SOURCE::DESTINATION AGAIN");
				System.out.println("Y/YES FOR BOOK");
				System.out.println("A/AGAIN FOR SET AGAIN");
				String againbook=br.readLine();
				if(againbook.equalsIgnoreCase("Y")||againbook.equalsIgnoreCase("YES")) {
					System.out.print("CONFiRM YOUR MAiL: ");
					String bookingemail=br.readLine();
					if(email.equals(bookingemail)) {
						String bookingid=BookingID.generateBookingID(bookingemail, source, destination);
						customerDAO.yesRequest(email, source, destination, type, bookingid);
						customerbool=false;
					}
				}
				else if(againbook.equalsIgnoreCase("A")||againbook.equalsIgnoreCase("AGAIN")) {
					customerbool=true;
				}
				else {
					System.out.println("EXIT!");
					customerbool=false;
				}
			}while(customerbool);
		}
		else {
			System.out.println("THiS TYPE iS CURRENTLY NOT VACANT");
		}
	}
}
