package org.zilker.vigneshb.restaurants;

public class TakeOutBean {// used to handle parcel and delivery details
	int takeoutId;
	String takeoutType, customerName, foodName;

	public int getTakeoutId() {
		return takeoutId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public String getFoodName() {
		return foodName;
	}

	public String getTakeoutType() {
		return takeoutType;
	}

	public int setTakeoutId(int takeoutId) {
		this.takeoutId = takeoutId;
		return 0;
	}

	public int setCustomerName(String customerName) {
		if (customerName != null)
			this.customerName = customerName;
		else
			return -1;
		return 0;
	}

	public int setFoodId(String foodId) {
		if (foodId != null)
			this.foodName = foodId;
		else
			return -1;
		return 0;
	}

	public int setTakeoutType(String takeoutType) {
		if (takeoutType.equals("parcel"))
			this.takeoutType = "parcel";
		else
			this.takeoutType = "delivery";
		return 0;
	}
}
