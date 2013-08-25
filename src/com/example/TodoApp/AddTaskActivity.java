package com.example.TodoApp;

import java.util.concurrent.ExecutionException;

import org.apache.http.client.HttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

public class AddTaskActivity extends Activity implements LocationListener{
	HttpClient httpclient;
	public static final String PREFS_NAME = "TodoPrefs";
	

	private String latitude = "0.0000";
	private String longitude = "0.0000";


	private LocationManager locationManager;
	private String provider;

	static SharedPreferences settings;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_task);

		settings = getSharedPreferences(PREFS_NAME, 0);
		httpclient = LoginActivity.httpclient;

	    Intent intent = getIntent();


	   locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    Criteria criteria = new Criteria();
	    provider = locationManager.getBestProvider(criteria, false);
	    Location location = locationManager.getLastKnownLocation(provider);
	    if(location != null) {
	    	onLocationChanged(location);
	    } 

	    if(intent.getStringExtra("id") != null){
			EditText taskDescription = (EditText) findViewById(R.id.editText1);
			EditText taskNotes = (EditText)findViewById(R.id.editText2);
			EditText taskCategory = (EditText)findViewById(R.id.editText3);
			CheckBox taskComplete = (CheckBox)findViewById(R.id.checkBox1);
			DatePicker taskDueDate = (DatePicker)findViewById(R.id.datePicker1);
			taskDescription.setText(intent.getStringExtra("description"));
			taskNotes.setText(intent.getStringExtra("notes"));
			taskCategory.setText(intent.getStringExtra("category"));
	    }
	    
	    int id = -1;
	    if (intent.getStringExtra("id") != null)
	    	id = Integer.parseInt(intent.getStringExtra("id"));
	    
		Button addButton = (Button)findViewById(R.id.button1);
		addButton.setOnClickListener(new Clicky(this, id));
	}
	

	  @Override
	  protected void onResume() {
	    super.onResume();
	    locationManager.requestLocationUpdates(provider, 400, 1, this);
	  }
  
	  @Override
	  public void onLocationChanged(Location location) {
		  longitude = String.valueOf(location.getLongitude());
		  latitude = String.valueOf(location.getLatitude());
	  }

	
	class Clicky implements OnClickListener {
		Activity activity;
		int id;
		public Clicky(Activity activity, int id){
			this.activity = activity;
			this.id = id;
		}

		@Override
		public void onClick(View view) {
			View accessView = (View)view.getParent();
			EditText taskDescription = (EditText) accessView.findViewById(R.id.editText1);
			EditText taskNotes = (EditText)accessView.findViewById(R.id.editText2);
			EditText taskCategory = (EditText)accessView.findViewById(R.id.editText3);
			CheckBox taskComplete = (CheckBox)accessView.findViewById(R.id.checkBox1);
			DatePicker taskDueDate = (DatePicker)accessView.findViewById(R.id.datePicker1);
			String dueDate = (taskDueDate.getMonth() + 1) + "/" + taskDueDate.getDayOfMonth() + "/" + taskDueDate.getYear();
			String complete = "false";
			if(taskComplete.isChecked())
				complete = "true";
			
			TodoPostTask tpt = new TodoPostTask();
			settings = getSharedPreferences(PREFS_NAME, 0);
			httpclient = LoginActivity.httpclient;
			tpt.setPreferences(settings);
			tpt.setClient(httpclient);
			
			String postURL = "http://tyh25-cs280-todo.herokuapp.com/task";
			if(id > 0){
				postURL += "/" + id;
			}
			Log.v("hilo", postURL);
			tpt.execute(postURL, TodoPostTask.ADD, taskDescription.getText().toString(), taskNotes.getText().toString(), dueDate, taskCategory.getText().toString(), latitude, longitude, complete);
			try {
				String response = tpt.get();
				Log.v("helo", response);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} finally {
				activity.finish();
			}
		}
	}



	@Override
	public void onProviderDisabled(String arg0) {
	}


	@Override
	public void onProviderEnabled(String arg0) {
	}


	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}
}
