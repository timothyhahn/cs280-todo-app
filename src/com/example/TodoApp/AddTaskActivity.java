package com.example.TodoApp;

import java.util.concurrent.ExecutionException;

import org.apache.http.client.HttpClient;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

public class AddTaskActivity extends Activity {
	HttpClient httpclient;
	public static final String PREFS_NAME = "TodoPrefs";

	static SharedPreferences settings;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_task);

		settings = getSharedPreferences(PREFS_NAME, 0);
		httpclient = LoginActivity.httpclient;

		
		Button addButton = (Button)findViewById(R.id.button1);
		addButton.setOnClickListener(new Clicky(this));
	}
	
	class Clicky implements OnClickListener {
		Activity activity;
		public Clicky(Activity activity){
			this.activity = activity;
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
			tpt.execute("http://tyh25-cs280-todo.herokuapp.com/task", TodoPostTask.ADD, taskDescription.getText().toString(), taskNotes.getText().toString(), dueDate, taskCategory.getText().toString(), "0.00", "0.00");
			try {
				String response = tpt.get();
				Log.v("helo", response);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				activity.finish();
			}
		}
	}


}
