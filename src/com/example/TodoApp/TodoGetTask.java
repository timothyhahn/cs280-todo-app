package com.example.TodoApp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;

public class TodoGetTask extends AsyncTask<String, Integer, String>{
	HttpClient httpclient;
	SharedPreferences settings;
	
	public void setClient(HttpClient httpclient){
		this.httpclient = httpclient;
	}
	public void setPreferences(SharedPreferences settings){
		this.settings = settings;
	}
	
	private String getB64Auth (String login, String pass) {
		   String source=login+":"+pass;
		   String ret="Basic "+Base64.encodeToString(source.getBytes(),Base64.URL_SAFE|Base64.NO_WRAP);
		   return ret;
	}
	
	@Override
	protected String doInBackground(String... params) {
		HttpGet httpget;
		String postURL = params[0];
		
		httpget = new HttpGet(postURL);

	
		HttpResponse response = null;
		try {
			response = httpclient.execute(httpget);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		HttpEntity entity = response.getEntity();
		String responseBody = "";
		try {
			responseBody = EntityUtils.toString(entity);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseBody;
	}

}
