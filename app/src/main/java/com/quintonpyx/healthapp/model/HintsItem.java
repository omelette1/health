package com.quintonpyx.healthapp.model;

import com.google.gson.annotations.SerializedName;

public class HintsItem{

	@SerializedName("food")
	private Food food;

	public Food getFood(){
		return food;
	}
}