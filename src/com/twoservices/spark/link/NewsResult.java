package com.twoservices.spark.link;

import com.google.gson.annotations.SerializedName;

public class NewsResult implements IResult {
	
	@SerializedName("CHECKED")
	public String checked;
	
	@SerializedName("TITLE")
	public String title;
	
	@SerializedName("DATEPOSTED")
	public String dateposted;
	
	@SerializedName("TIMEPOSTED")
	public String timeposted;
	
	@SerializedName("UU_NEWS_ID")
	public String news_id;
	
}
