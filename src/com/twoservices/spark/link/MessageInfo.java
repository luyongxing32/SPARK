package com.twoservices.spark.link;

import com.google.gson.annotations.SerializedName;

public class MessageInfo implements IResult {

	@SerializedName("MESSAGE_ID")
	public int message_id;

	@SerializedName("MESSAGE_BODY")
	public String message_body;
	
	@SerializedName("SENT_FROM")
	public String sent_from;
	
}
