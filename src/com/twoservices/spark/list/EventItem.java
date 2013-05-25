/**
 * @author Ry
 * @Date 2012.11.22
 * @FileName EventResult.java
 *
 */

package com.twoservices.spark.list;

public class EventItem {

	// Name of event list item
	public String name;

	// Extra information of event list item
	public String extra_info;
	
	// Resource id of icon that will drawing in list item 
	public int icon_res_id;
	
	public EventItem(String name, String extra, int res_id) {
		this.name = name;
		this.extra_info = extra;
		this.icon_res_id = res_id;
	}
	
}
