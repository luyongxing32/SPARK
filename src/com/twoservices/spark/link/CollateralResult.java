package com.twoservices.spark.link;

import com.google.gson.annotations.SerializedName;

public class CollateralResult implements IResult {

	@SerializedName("CHECKED")
	public String checked;
	
	@SerializedName("COMPANY")
	public String company;
	
	@SerializedName("FILE_SIZE")
	public String file_size;
	
	@SerializedName("FILE_TITLE")
	public String file_title;
	
	@SerializedName("FILE_TYPE")
	public String file_type;
	
	@SerializedName("UU_COLLATERAL_ID")
	public String collateral_id;
	
}
