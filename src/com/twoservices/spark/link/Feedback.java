/**
 * @author Ry
 * @Date 2012.11.23
 * @FileName Feedback.java
 *
 */

package com.twoservices.spark.link;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.Window;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Feedback {

	private static final String TAG = Feedback.class.getSimpleName();
	private static final boolean DEBUG = true;

	// Define the root URL
	public static final String HTTP_ROOT_URL = "http://con-nect.com/jumpfire";

	// Define the destination URL
	public static final String HTTP_URL = "http://con-nect.com/jumpfire/services/api.cfm";
	
	// When service was run successfully, the identifier of array part that will received
	private static final String ID_FIELD_DATA = "DATA";
	
	// When service was run successfully, the string that "STATUS" field will hold
	private static final String STATUS_SUCCESS = "Success";	
	
	// Variable that was parsed JSON object
	public static ServiceResponse sServiceResponse;
	
	// ProgressDialog that will display the state of loading
	private static ProgressDialog sProgressDialog = null;

	
	/**
	 * Initialize progress dialog
	 */
	public static void initProgressDialog(Context context) {
		if (sProgressDialog == null || !sProgressDialog.isShowing()) {
			sProgressDialog = new ProgressDialog(context);
			sProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			sProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			sProgressDialog.setCancelable(true);
			sProgressDialog.setMessage("Loading...");
		}
	}
	
	/**
	 * Show progress dialog
	 */
	public static void showProgress() {
		sProgressDialog.show();
	}
	
	/**
	 * Dismiss progress dialog
	 */
	public static void dismissProgress() {
		sProgressDialog.dismiss();
	}

	/**
	 * Read JSON message from web server
	 * 
	 * @param uri String that will execute in web server
	 * @return String that represent by JSON style
	 */
	public static String runServiceAPI(String uri) {
		
		/*StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);*/

		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(uri);

		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();

			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				
				// Parse service response
				sServiceResponse = new Gson().fromJson(builder.toString(), ServiceResponse.class);
			} else {
				if (DEBUG) Log.e(TAG, "Failed getting result");
			}
		} catch (ClientProtocolException e) {
			if (DEBUG) e.printStackTrace();
		} catch (IOException e) {
			if (DEBUG) e.printStackTrace();
		} catch (Exception e) {
			if (DEBUG) e.printStackTrace();
		}
		
		return builder.toString();
	}
	
	/**
	 * Parse the "DATA" object from service response
	 * 
	 * @param response received from service api
	 * @param valueType
	 * @return result of parsing response
	 */
	public static <T> T parseData(String response, Class<T> valueType) {

		try {
			// Get the "DATA" object array from service response
			JSONObject dataObj = new JSONObject(response).getJSONObject(ID_FIELD_DATA);
			
			return new Gson().fromJson(dataObj.toString(), valueType);
		} catch (JSONException e) {
			if (DEBUG) e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Parse the "DATA" object array from service response
	 * 
	 * @param response received from service api
	 * @param valueType
	 * @return result of parsing response
	 */
	public static <T> T parseDataArray(String response, Class<T> valueType) {

		try {
			// Get the "DATA" object array from service response
			JSONArray dataArray = new JSONObject(response).getJSONArray(ID_FIELD_DATA);
			
			// Parse Response into our object
			//Type collectionType = new TypeToken<ArrayList<LoginResult>>(){}.getType();
			
			return new Gson().fromJson(dataArray.toString(), valueType);
		} catch (JSONException e) {
			if (DEBUG) e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Decide whether service result is success or not
	 * 
	 * @return Flag that indicate whether service result is success or not
	 */
	public static boolean isSuccess() {
		return (sServiceResponse != null) && sServiceResponse.status.equals(STATUS_SUCCESS);
	}
	
}
