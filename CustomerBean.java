package org.zilker.vigneshb.restaurants;

public class CustomerBean {// used to handle customer details
	int customerId, customerOrders;
	String customerName, customerPhone;

	public int getCustomerId() {
		return customerId;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public int getCustomerOrders() {
		return customerOrders;
	}

	public String getCustomerName() {
		return customerName;
	}

	public int setCustomerId(int customerId) {
		this.customerId = customerId;
		return 0;
	}

	public int setCustomerName(String customerName) {
		if (customerName != null)
			this.customerName = customerName;
		else
			return -1;
		return 0;
	}

	public int setCustomerPhone(String customerPhone) {
		if (customerPhone.length() == 10)
			this.customerPhone = customerPhone;
		else
			return -1;
		return 0;
	}

	public int setCustomerOrders(int customerOrders) {
		if (customerOrders >= 0)
			this.customerOrders = customerOrders;
		else
			return -1;
		return 0;
	}
}
