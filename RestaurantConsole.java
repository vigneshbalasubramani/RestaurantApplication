package org.zilker.vigneshb.restaurants;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Logger;

public class RestaurantConsole {
	static Logger logger = Logger.getLogger(RestaurantConsole.class.getName());
	static Scanner scanner = new Scanner(System.in);
	static int moduleType = 1;
	//static int moduleType = 2;
	static int choice = 1, response;
	static String request;

	public static void main(String[] args) throws SQLException {
		switch (moduleType) {
		case 1:
			restaurantMode();// goes to the console of restaurant manager
			break;
		case 2:
			takeoutMode();// goes to the console of food packaging manager
			break;
		default:
			break;
		}

	}

	public static int restaurantMode() throws SQLException {// restaurant manager console
		System.out.println("-----RESTAURANT MODE-----");
		RestaurantDbInteraction.connect();
		while (choice < 4) {
			logger.info("Enter your option \n 1.new food item\n 2.update the count\n 3.new offer\n 4.exit");
			choice = scanner.nextInt();
			switch (choice) {
			case 1:
				logger.info("enter the dish name and cost");
				response = RestaurantDbInteraction.newFoodItem(scanner.next(), scanner.nextInt());
				check(response, "dish name", "cost", "inserted");
				break;
			case 2:
				logger.info("enter the dish name and additional count");
				response = RestaurantDbInteraction.updateCount(scanner.next(), scanner.nextInt());
				check(response, "dish name", "additional count", "updated");
				break;
			case 3:
				RestaurantDbInteraction.newOffer();
				break;
			default:
				logger.info("exitting");
				RestaurantDbInteraction.close();
				break;
			}
		}
		return 0;
	}

	public static int takeoutMode() throws SQLException {// food packaging manager console
		System.out.println("-----TAKEOUT MODE-----");
		RestaurantDbInteraction.connect();
		while (choice < 5) {
			logger.info(
					"enter your choice\n 1.New customer\n 2.Parcel or Delivery\n 3.update Status\n 4.Check delivery speed\n 5.exit ");
			choice = scanner.nextInt();
			switch (choice) {
			case 1:
				logger.info("Enter the customer name and phone number");
				response = RestaurantDbInteraction.newCustomer(scanner.next(), scanner.next());
				check(response, "customer name", "phone number", "inserted");
				break;
			case 2:
				logger.info("enter the type (parcel/delivery)");
				request = scanner.next();
				logger.info("enter the customer name and dish name");
				response = RestaurantDbInteraction.parcelOrDelivery(request, scanner.next(), scanner.next());
				check(response, "customer name", "dish name", "added");
				break;
			case 3:
				logger.info("enter the customer name");
				response = RestaurantDbInteraction.updateDeliveryStatus(scanner.next());
				if (response == 0) {
					logger.info("updated successfully");
				} else {
					logger.info("enter the customer name properly");
				}
				break;
			case 4:
				logger.info("enter the type\n 1.parcel\n 2.delivery");
				RestaurantDbInteraction.checkSpeed(scanner.nextInt());
				break;
			default:
				logger.info("exitting");
				RestaurantDbInteraction.close();
				break;
			}
		}
		return 0;
	}

	public static int check(int response, String alpha, String beta, String task) {// informs the response for each
																					// action
		if (response > 0) {
			if (response == 1) {
				logger.info("enter the " + alpha + " properly");
			} else {
				logger.info("enter the " + beta + " properly");
			}
		} else {
			logger.info(task + " successfully");
		}
		return 0;
	}

}
