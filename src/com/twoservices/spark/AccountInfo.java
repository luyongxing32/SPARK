/**
 * @author Ry
 * @Date 2012.11.23
 * @FileName AccountInfo.java
 *
 */

package com.twoservices.spark;

import com.twoservices.spark.link.LoginResult;


public class AccountInfo {

	// User contact id
	public static String sContactId = "";
	
	// User project id
	public static String sProjectId = "";

	// Project string that user will select among the several one
	public static String sProjectName = "";
	
	// User name
	public static String sUserName = "";
	
	// User email string
	public static String sMyEmail = "";
	
	/**
	 * Initiate the account information such as contact_id, project_id etc.
	 */
	public static void init(LoginResult result) {
		AccountInfo.sContactId = result.uu_contact_id;
		AccountInfo.sProjectId = result.uu_project_id;
		AccountInfo.sProjectName = result.project_name;
	}	
	
}
