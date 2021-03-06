/**
 * @author Ry
 * @Date 2012.11.26
 * @FileName ExhibitorResult.java
 *
 */

package com.twoservices.spark.link;

import com.google.gson.annotations.SerializedName;

public class ExhibitorDetailResult implements IDetailResult {
	
	@SerializedName("IS_MYMAP")
	public String is_mymap;

	@SerializedName("COMPANY")
	public String company;
	
	@SerializedName("DESCRIPTION")
	public String description;
	
	@SerializedName("BOOTH")
	public String booth;

    @SerializedName("CONTACT")
    public String contact;

    @SerializedName("PHONE")
    public String phone;

    @SerializedName("ADDRESS")
    public String address;

    @SerializedName("LOGO_URL")
    public String logo_url;

    @SerializedName("WEBSITE")
	public String website;
	
	@SerializedName("EMAIL")
	public String email;

    @SerializedName("SCHEDULE_MEETING")
    public String schedule_meeting;

    @SerializedName("UU_EXHIBITOR_ID")
    public String exhibitor_id;

}
