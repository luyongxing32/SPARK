/**
 * @author Ry
 * @Date 2012.11.22
 * @FileName MyEventResult.java
 *
 */

package com.twoservices.spark.link;

import com.google.gson.annotations.SerializedName;

public class MyEventResult implements IResult {

	@SerializedName("USER_NAME")
	public String username;

	@SerializedName("HOME_TOP_TEXT")
	public String top_text;
	
	@SerializedName("SESSION")
	public EventInfo session;
	
	@SerializedName("EXHIBITOR")
	public EventInfo exhibitor;
	
	@SerializedName("COLLATERAL")
	public EventInfo collateral;
	
	@SerializedName("NETWORK")
	public EventInfo network;
	
}
