package com.twoservices.spark.link;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MessageResult implements IResult {

	@SerializedName("NAME")
	public String name;
	
	@SerializedName("UU_NETWORK_CONTACT_ID")
	public String uu_network_contact_id;
	
	@SerializedName("MESSAGES")
	public ArrayList<MessageInfo> messages;
	
}
