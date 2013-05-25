package com.twoservices.spark.link;

import com.google.gson.annotations.SerializedName;

public class ProfileResult implements IResult {

	@SerializedName("NAME")
	public String name;
	
	@SerializedName("COMPANY")
	public String company;
	
	@SerializedName("EMAIL")
	public String email;
	
	@SerializedName("SHOW_EMAIL")
	public String show_email;
	
	@SerializedName("ALLOW_EMAIL")
	public String allow_email;
	
	@SerializedName("ALLOW_SMS")
	public String allow_sms;
	
	@SerializedName("ALLOW_REMINDERS")
	public String allow_reminders;
	
}
