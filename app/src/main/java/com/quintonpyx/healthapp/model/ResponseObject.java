package com.quintonpyx.healthapp.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseObject{

	@SerializedName("hints")
	private List<HintsItem> hints;

	public List<HintsItem> getHints(){
		return hints;
	}
}