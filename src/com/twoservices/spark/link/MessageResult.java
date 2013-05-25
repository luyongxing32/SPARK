package com.twoservices.spark.link;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class MessageResult implements IResult {

	@SerializedName("NAME")
	public String name;
	
	@SerializedName("UU_NETWORK_CONTACT_ID")
	public String uu_network_contact_id;
	
	@SerializedName("MESSAGES")
	public ArrayList<MessageInfo> messages;
	
}
