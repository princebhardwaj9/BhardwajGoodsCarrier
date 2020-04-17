package model;

public class Truck {
	private String truckname;
	private String type;
	
	public String getTruckname() {
		return truckname;
	}
	public void setTruckname(String truckname) {
		this.truckname = truckname;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public Truck(String truckname, String type) {
		this.truckname = truckname;
		this.type = type;
	}

}