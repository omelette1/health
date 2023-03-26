package com.quintonpyx.healthapp.model;

import com.google.gson.annotations.SerializedName;

public class Nutrients{

	@SerializedName("PROCNT")
	private Object pROCNT;

	@SerializedName("ENERC_KCAL")
	private float eNERCKCAL;

	@SerializedName("FAT")
	private Object fAT;

	@SerializedName("CHOCDF")
	private Object cHOCDF;

	@SerializedName("FIBTG")
	private Object fIBTG;

	public Object getPROCNT(){
		return pROCNT;
	}

	public float getENERCKCAL(){
		return eNERCKCAL;
	}

	public Object getFAT(){
		return fAT;
	}

	public Object getCHOCDF(){
		return cHOCDF;
	}

	public Object getFIBTG(){
		return fIBTG;
	}
}