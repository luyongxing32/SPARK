/**
 * @author Ry
 * @Date 2012.11.22
 * @FileName HomeResult.java
 *
 */

package com.twoservices.spark.link;

import com.google.gson.annotations.SerializedName;

public class HomeResult implements IResult {

	@SerializedName("USER_NAME")
	public String username;

	@SerializedName("HOME_TOP_TEXT")
	public String top_text;
	
	@SerializedName("AGENDA")
	public EventInfo agenda;
	
	@SerializedName("EXHIBITOR")
	public EventInfo exhibitor;
	
	@SerializedName("MAP")
	public EventInfo map;
	
	@SerializedName("COLLATERAL")
	public EventInfo collateral;
	
	@SerializedName("NEWS")
	public EventInfo news;
	
	@SerializedName("NETWORK")
	public EventInfo network;
	
}
