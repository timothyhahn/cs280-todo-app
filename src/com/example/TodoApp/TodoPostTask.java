package com.example.TodoApp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;

public class TodoPostTask extends AsyncTask<String, Integer, String>{
	HttpClient httpclient;
	SharedPreferences settings;
	//Actions
	public static final String LOGIN = "l";
	public static final String REGISTER = "r";
	public static final String ADD = "a";
	public static final String EDIT = "e";
	public static final String DELETE = "d";

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
		HttpPost httppost;
		String postURL = params[0];
		String action = params[1];
		
		httppost = new HttpPost(postURL);

		List<NameValuePair> formValues = new ArrayList<NameValuePair>();
		
		if(action.equals(REGISTER)){
			String username = params[2];
			String password = params[3];
			formValues.add(new BasicNameValuePair("username", username));
			formValues.add(new BasicNameValuePair("password", password));
			
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("username", username);
			editor.putString("password", password);
			editor.commit();
		}
		if(action.equals(LOGIN)){
			String username = params[2];
			String password = params[3];
			httppost.setHeader("Authorization", getB64Auth(username, password));
		}
		try {
			httppost.setEntity(new UrlEncodedFormEntity(formValues, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		HttpResponse response = null;
		try {
			response = httpclient.execute(httppost);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseBody;
	}

}
