/**
 * @author Ry
 * @Date 2012.11.23
 * @FileName NetworkResult.java
 *
 */

package com.twoservices.spark.link;

import com.google.gson.annotations.SerializedName;

public class NetworkResult implements IResult {
	
	@SerializedName("INVITED")
	public String invited;
	
	@SerializedName("NAME")
	public String name;
	
	@SerializedName("TITLE")
	public String title;
	
	@SerializedName("RELATIONSHIP")
	public String relationship;
	
	@SerializedName("UU_NETWORK_CONTACT_ID")
	public String network_contact_id;
	
}
