/**
 * @author Ry
 * @Date 2012.11.26
 * @FileName NewsDetailResult.java
 *
 */

package com.twoservices.spark.link;

import com.google.gson.annotations.SerializedName;

public class NewsDetailResult implements IDetailResult {

	@SerializedName("IS_READ")
	public String is_read;
	
	@SerializedName("AUTHOR")
	public String author;
	
	@SerializedName("TITLE")
	public String title;
	
	@SerializedName("BODY")
	public String body;
	
	@SerializedName("DATEPOSTED")
	public String dateposted;
	
	@SerializedName("TIMEPOSTED")
	public String timeposted;
	
	@SerializedName("UU_NEWS_ID")
	public String news_id;
	
}
