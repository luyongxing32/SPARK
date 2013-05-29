package com.twoservices.spark.link;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SurveyResult implements IResult {

	@SerializedName("NAME")
	public String name;
	
	@SerializedName("TITLE")
	public String title;

	@SerializedName("QUESTIONS")
	public ArrayList<SurveyItem> questions;

	@SerializedName("QUESTION_NUM")
	public int question_num;
	
	@SerializedName("UU_SESSION_ID")
	public String session_id;
	
}
