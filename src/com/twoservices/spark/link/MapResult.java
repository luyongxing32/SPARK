package com.twoservices.spark.link;

import com.google.gson.annotations.SerializedName;

public class MapResult implements IResult {

	@SerializedName("MAP_TEXT")
	public String map_text;
	
	@SerializedName("MAP")
	public String map;
	
}
