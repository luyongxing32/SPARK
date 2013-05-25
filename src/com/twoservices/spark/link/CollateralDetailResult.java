/**
 * @author Ry
 * @Date 2012.11.26
 * @FileName CollateralDetailResult.java
 *
 */

package com.twoservices.spark.link;

import com.google.gson.annotations.SerializedName;

public class CollateralDetailResult implements IDetailResult {

	@SerializedName("IS_MYCOLLATERAL")
	public String is_mycollateral;
	
	@SerializedName("FILE_TITLE")
	public String file_title;

	@SerializedName("FILE_NAME")
	public String file_name;

	@SerializedName("FILE_DESCRIPTION")
	public String file_descrition;
	
	@SerializedName("FILE_SIZE")
	public String file_size;
	
	@SerializedName("UU_COLLATERAL_ID")
	public String collateral_id;
	
}
