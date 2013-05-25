package com.twoservices.spark.link;

import com.google.gson.annotations.SerializedName;

public class SponsorResult implements IResult {

	@SerializedName("SPONSOR_ID")
	public String sponsor_id;
	
	@SerializedName("SPONSOR_NAME")
	public String sponsor_name;
	
	@SerializedName("SPONSOR_URL")
	public String sponsor_url;
	
}
