package com.quintonpyx.healthapp.model;

import com.google.gson.annotations.SerializedName;

public class Food{

	@SerializedName("image")
	private String image;

	@SerializedName("foodId")
	private String foodId;

	@SerializedName("label")
	private String label;

	@SerializedName("nutrients")
	private Nutrients nutrients;

	public String getImage(){
		return image;
	}

	public String getFoodId(){
		return foodId;
	}

	public String getLabel(){
		return label;
	}

	public Nutrients getNutrients(){
		return nutrients;
	}
}