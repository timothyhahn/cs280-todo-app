package com.example.TodoApp;

import org.apache.http.client.HttpClient;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
public class TodoHome extends Activity {
	HttpClient httpclient;
	public static final String PREFS_NAME = "TodoPrefs";

	static SharedPreferences settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_home);

		settings = getSharedPreferences(PREFS_NAME, 0);
		httpclient = LoginActivity.httpclient;
		
	}
	
	@Override
	protected void onStop(){
		super.onStop();
		setResult(0);
	}

}
