/**
 * @author Ry
 * @Date 2012.11.22
 * @FileName LoginResponse.java
 *
 */

package com.twoservices.spark.link;

import com.google.gson.annotations.SerializedName;


public class ServiceResponse {
	
	@SerializedName("ACTION")
	public String action;
	
	//@SerializedName("DATA")
	//public List<LoginResult> data;
	
	@SerializedName("ERROR")
	public String error;
	
	@SerializedName("STATUS")
	public String status;
	
	@SerializedName("TOTAL_RESULTS")
	public int total_results;
	
}
