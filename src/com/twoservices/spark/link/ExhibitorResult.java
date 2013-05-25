/**
 * @author Ry
 * @Date 2012.11.23
 * @FileName ExhibitorResult.java
 *
 */

package com.twoservices.spark.link;

import com.google.gson.annotations.SerializedName;

public class ExhibitorResult implements IResult {
	
	@SerializedName("CHECKED")
	public String checked;
	
	@SerializedName("PREFERRED")
	public String preferred;
	
	@SerializedName("COMPANY")
	public String company;
	
	@SerializedName("BOOTH")
	public String booth;
	
	@SerializedName("UU_EXHIBITOR_ID")
	public String exhibitor_id;
	
}
