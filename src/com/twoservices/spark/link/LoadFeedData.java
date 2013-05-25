package com.twoservices.spark.link;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

public class LoadFeedData extends AsyncTask<String, Void, String> {
	
	private InputStream retrieveStream(String url) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpGet = null;
		httpGet = new HttpGet(url);

		HttpResponse httpResponse = null;
		try {
			httpResponse = client.execute(httpGet);
			HttpEntity getResponseEntity = httpResponse.getEntity();
			return getResponseEntity.getContent();
		} catch (IOException e) {
			httpGet.abort();
		}
		return null;
	}

	@Override
	protected String doInBackground(String... params) {
		InputStream source = retrieveStream(params[0]);
		BufferedReader reader = null;
		StringBuilder builder = new StringBuilder();
		
		try {
			reader = new BufferedReader(new InputStreamReader(source));
			String line;
			
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			
			// Parse service response
			Feedback.sServiceResponse = new Gson().fromJson(builder.toString(), ServiceResponse.class);
		} catch (Exception e) {
			Log.e("LoadFeedData", "Failed getting result");
			Feedback.sServiceResponse = null;
			return null;
		}
		
		return builder.toString();
	}

	@Override
	protected void onPostExecute(String response) {
	}
	
}
