package org.zilker.vigneshb.restaurants;

public class FoodBean {// used to handle food details
	int foodId, foodCost, foodCount;
	String foodName;

	public int getFoodId() {
		return foodId;
	}

	public int getFoodCost() {
		return foodCost;
	}

	public int getFoodCount() {
		return foodCount;
	}

	public String getFoodName() {
		return foodName;
	}

	public int setFoodId(int foodId) {
		this.foodId = foodId;
		return 0;
	}

	public int setFoodCount(int foodCount) {
		if (foodCount >= 0)
			this.foodCount = foodCount;
		else
			return -1;
		return 0;
	}

	public int setFoodCost(int foodCost) {
		if (foodCost > 0)
			this.foodCost = foodCost;
		else
			return -1;
		return 0;
	}

	public int setFoodName(String foodName) {
		if (foodName != null)
			this.foodName = foodName;
		else
			return -1;
		return 0;
	}

}
