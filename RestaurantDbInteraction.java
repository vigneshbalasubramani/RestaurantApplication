package org.zilker.vigneshb.restaurants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Logger;

public class RestaurantDbInteraction {
	static Logger logger = Logger.getLogger(RestaurantConsole.class.getName());
	static Connection connection;
	static Statement statement;
	static ResultSet resultSet;
	static int customers = 100, foods = 200, takeouts = 300;
	static Date date;
	static Timestamp timestamp;

	public static int connect() throws SQLException {// connects to the database
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant", "root", "zilkeradmin");
		statement = connection.createStatement();
		logger.info("connection successful");
		return 0;
	}

	public static int close() throws SQLException {// closes the database
		if (resultSet != null)
			resultSet.close();
		if (statement != null)
			statement.close();
		if (connection != null)
			connection.close();
		return 0;
	}

	public static int newFoodItem(String foodName, int foodCost) {// adds new food item row
		FoodBean foodBean = new FoodBean();
		if (foodBean.setFoodName(foodName) == -1)
			return 1;
		if (foodBean.setFoodCost(foodCost) == -1)
			return 2;
		checkTime();
		try {
			resultSet = statement.executeQuery("select count(*) from restaurant.food_item");
			resultSet.next();
			foods = foods + resultSet.getInt("count(*)") + 1;
			foodBean.setFoodId(foods);
			foodBean.setFoodCount(0);
			statement.execute(
					"insert into restaurant.food_item values(" + foodBean.getFoodId() + ",'" + foodBean.getFoodName()
							+ "'," + foodBean.getFoodCost() + "," + foodBean.getFoodCount() + ",'" + timestamp + "')");
		} catch (SQLException e) {
			logger.info("cannot be inserted");
		}

		return 0;
	}

	public static int updateCount(String foodName, int additionCount) {// updates no:of food items ordered
		FoodBean foodBean = new FoodBean();
		if (foodBean.setFoodName(foodName) == -1) {
			return 1;
		}
		if (foodBean.setFoodCount(additionCount) == -1) {
			return 2;
		}
		checkTime();
		try {
			resultSet = statement.executeQuery(
					"select food_count from restaurant.food_item where food_name='" + foodBean.getFoodName() + "'");
			resultSet.next();
			if (foodBean.setFoodCount(resultSet.getInt("food_count") + additionCount) == -1)
				logger.info("hey");
			statement.executeUpdate("update restaurant.food_item set food_count=" + foodBean.getFoodCount()
					+ ",last_updated='" + timestamp + "' where food_name='" + foodBean.getFoodName() + "'");
		} catch (SQLException e) {
			logger.info("invalid food name");
		}
		return 0;
	}

	public static int newOffer() {// suggests new offer to better sales
		try {
			resultSet = statement.executeQuery(
					"select food_name from restaurant.food_item where food_count=(select min(food_count) from restaurant.food_item)");
			resultSet.next();
			String unpopular = resultSet.getString("food_name");
			resultSet = statement.executeQuery(
					"select food_name from restaurant.food_item where food_count=(select max(food_count) from restaurant.food_item)");
			resultSet.next();
			String popular = resultSet.getString("food_name");
			logger.info("add a new offer " + unpopular + " with " + popular);
		} catch (Exception e) {
			logger.info("no offers as of now");
		}
		return 0;
	}

	public static int newCustomer(String customerName, String customerPhone) {// adds new customer details
		CustomerBean customerBean = new CustomerBean();
		if (customerBean.setCustomerName(customerName) == -1) {
			return 1;
		}
		if (customerBean.setCustomerPhone(customerPhone) == -1) {
			return 2;
		}
		checkTime();
		try {
			resultSet = statement.executeQuery("select count(*) from restaurant.customer");
			resultSet.next();
			customers = customers + resultSet.getInt("count(*)") + 1;
			customerBean.setCustomerId(customers);
			customerBean.setCustomerOrders(0);
			statement.execute("insert into restaurant.customer values(" + customerBean.getCustomerId() + ",'"
					+ customerBean.getCustomerName() + "'," + customerBean.getCustomerPhone() + ","
					+ customerBean.getCustomerOrders() + ",'" + timestamp + "')");
		} catch (SQLException e) {
			logger.info("enter the number properly");
		}

		return 0;

	}

	public static int checkSpeed(int choice) throws SQLException {// checks no:of orders delivered on time
		if (choice == 1) {

			resultSet = statement.executeQuery(
					"select count(*) from restaurant.take_outs where order_type='parcel' and UNIX_TIMESTAMP(delivered_on)-UNIX_TIMESTAMP(ordered_on)>600");
			resultSet.next();
			logger.info("" + resultSet.getInt("count(*)") + " parcels have been late");
		} else {
			resultSet = statement.executeQuery(
					"select count(*) from restaurant.take_outs where order_type='delivery' and UNIX_TIMESTAMP(delivered_on)-UNIX_TIMESTAMP(ordered_on)>1800");
			resultSet.next();
			logger.info("" + resultSet.getInt("count(*)") + " deliveries have been late");
		}
		return 0;
	}

	public static int parcelOrDelivery(String type,String customerName,String foodName) {//adds new parcel or a delivery
		TakeOutBean takeOutBean=new TakeOutBean();
		takeOutBean.setTakeoutType(type);
		if(takeOutBean.setCustomerName(customerName)==-1) {
			return 1;
		}
		if(takeOutBean.setFoodId(foodName)==-1) {
			return 2;
		}
		checkTime();
		try {
			resultSet=statement.executeQuery("select count(*) from restaurant.take_outs");
			resultSet.next();
			takeouts=takeouts+resultSet.getInt("count(*)")+1;
			takeOutBean.setTakeoutId(takeouts);
			statement.execute("insert into restaurant.take_outs values("+takeOutBean.getTakeoutId()+",'"+takeOutBean.getTakeoutType()+"','"+takeOutBean.getCustomerName()+"','"+takeOutBean.getFoodName()+"','"+timestamp+"','2019/12/12 12:00:00')");
			statement.executeUpdate("update restaurant.food_item set food_count=food_count+1 where food_name='"+takeOutBean.getFoodName()+"'");
			statement.executeUpdate("update restaurant.customer set customer_orders=customer_orders+1 where customer_name='"+takeOutBean.getCustomerName()+"'");
		}
		catch(Exception e) {
			logger.info("enter the food type properly");
		}
		return 0;
	}

	public static int updateDeliveryStatus(String customerName) {// after a parcel or delivery is done
		checkTime();
		try {
			statement.executeUpdate("update restaurant.take_outs set delivered_on='" + timestamp
					+ "' where customer_name='" + customerName + "'");
		} catch (Exception e) {
			logger.info("no such delivery is present");
		}
		return 0;
	}

	public static int checkTime() {// gives current time
		date = new Date();
		timestamp = new Timestamp(date.getTime());
		return 0;
	}

}
