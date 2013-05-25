/**
 * @author Ry
 * @Date 2012.11.23
 * @FileName AgendaResult.java
 *
 */

package com.twoservices.spark.link;

import com.google.gson.annotations.SerializedName;

public class AgendaResult implements IResult {

	@SerializedName("CHECKED")
	public String checked;
	
	@SerializedName("START_DATE")
	public String start_date;
	
	@SerializedName("START_TIME")
	public String start_time;
	
	@SerializedName("TITLE")
	public String title;
	
	@SerializedName("UU_SESSION_ID")
	public String session_id;
	
}
