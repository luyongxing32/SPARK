/**
 * @author Ry
 * @Date 2012.11.26
 * @FileName NetworkDetailResult.java
 *
 */

package com.twoservices.spark.link;

import com.google.gson.annotations.SerializedName;

public class NetworkDetailResult implements IDetailResult {

	@SerializedName("IS_MYNETWORK")
	public String is_mynetwork;
	
	@SerializedName("COMPANY")
	public String company;
	
	@SerializedName("NAME")
	public String name;
	
	@SerializedName("TITLE")
	public String title;

    @SerializedName("ADDRESS")
    public String address;

    @SerializedName("CITY")
    public String city;

    @SerializedName("STATE")
    public String state;

    @SerializedName("ZIP")
    public String zip;

    @SerializedName("EMAIL")
    public String email;

    @SerializedName("PHONE")
    public String phone;

    @SerializedName("MOBILE")
    public String mobile;

	@SerializedName("UU_NETWORK_CONTACT_ID")
	public String network_contact_id;
	
}