/**
 * @author Ry
 * @Date 2012.11.22
 * @FileName LoginResult.java
 *
 */

package com.twoservices.spark.link;

import com.google.gson.annotations.SerializedName;

public class LoginResult implements IResult {
	
	@SerializedName("PROJECT_NAME")
	public String project_name;
	
	@SerializedName("UU_CONTACT_ID")
	public String uu_contact_id;
	
	@SerializedName("UU_PROJECT_ID")
	public String uu_project_id;
	
}
