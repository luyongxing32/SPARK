/**
 * @author Ry
 * @Date 2012.11.26
 * @FileName AgendaDetailResult.java
 *
 */

package com.twoservices.spark.link;

import com.google.gson.annotations.SerializedName;

public class AgendaDetailResult implements IDetailResult {
	
	@SerializedName("IS_MYAGENDA")
	public String is_myagenda;
	
	@SerializedName("CHECKED_IN")
	public int checked_in;

	@SerializedName("TITLE")
	public String title;
	
	@SerializedName("START_DATE")
	public String start_date;
	
	@SerializedName("START_TIME")
	public String start_time;
	
	@SerializedName("END_TIME")
	public String end_time;
	
	@SerializedName("TRACK")
	public String track;
	
	@SerializedName("COMPANY")
	public String company;
	
	@SerializedName("SPEAKER")
	public String speaker;
	
	@SerializedName("DESCRIPTION")
	public String description;
	
	@SerializedName("QUESTION_NUM")
	public int question_num;
	
	@SerializedName("RESPONSE_NUM")
	public int response_num;
	
	@SerializedName("UU_SESSION_ID")
	public String session_id;
	
}
