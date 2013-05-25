package com.twoservices.spark.link;

import com.google.gson.annotations.SerializedName;

public class SurveyItem {
	
	@SerializedName("EVAL_QUESTION_ID")
	public String eval_question_id;
	
	@SerializedName("TITLE")
	public String title;
	
	@SerializedName("TYPE")
	public String type;
	
	@SerializedName("RESPONSE_INT")
	public String response_int;
	
	@SerializedName("RESPONSE_TEXT")
	public String response_text;
	
}
