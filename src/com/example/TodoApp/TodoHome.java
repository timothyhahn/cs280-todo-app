package com.example.TodoApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.client.HttpClient;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
public class TodoHome extends Activity {
	HttpClient httpclient;
	public static final String PREFS_NAME = "TodoPrefs";

	static SharedPreferences settings;
	private List<HashMap<String,String>> taskHeaderList;
	private HashMap<String, String> taskNotesList;
	
	ExpandableListView taskListView;
	TaskListAdapter taskListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_home);

		settings = getSharedPreferences(PREFS_NAME, 0);
		httpclient = LoginActivity.httpclient;
		
		Button addButton = (Button)findViewById(R.id.add_button);
		Clicky clicky = new Clicky(this);
		addButton.setOnClickListener(clicky);
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		TodoGetTask tgt = new TodoGetTask();
		tgt.setClient(httpclient);
		tgt.setPreferences(settings);
		tgt.execute("http://tyh25-cs280-todo.herokuapp.com/task");
		String responseJSON ="";
		try {
			responseJSON = tgt.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		taskHeaderList = new ArrayList<HashMap<String,String>>();
		taskNotesList = new HashMap<String, String>();
		
		JsonParser jp = new JsonParser();
		JsonElement root = null;
		root = jp.parse(responseJSON);
		JsonObject rootobj = root.getAsJsonObject();
		JsonArray tasks = rootobj.get("tasks").getAsJsonArray();
		for(int i = 0; i < tasks.size(); i++){
			JsonObject task = tasks.get(i).getAsJsonObject();
			String category_name = task.get("category_name").getAsString();
			String complete = task.get("complete").getAsString();
			String description = task.get("description").getAsString();
			String due_date = task.get("due_date").getAsString();
			String notes = task.get("notes").getAsString();
			String id = task.get("id").getAsString();
			
			HashMap<String, String> taskHash = new HashMap<String, String>();
			taskHash.put("description", description);
			taskHash.put("category_name", category_name);
			taskHash.put("complete", complete);
			taskHash.put("due_date", due_date);
			taskHash.put("id", id);
			taskHeaderList.add(taskHash);
			taskNotesList.put(description, notes);
		}
		

		taskListView = (ExpandableListView)findViewById(R.id.task_expandable_list);
		taskListAdapter = new TaskListAdapter(this, taskHeaderList, taskNotesList);
		taskListView.setAdapter(taskListAdapter);
		
		LongClicky longClicky = new  LongClicky(this);
		taskListView.setOnItemLongClickListener(longClicky);
		TodoGetTask tgtc = new TodoGetTask();
		tgtc.setClient(httpclient);
		tgtc.setPreferences(settings);
		tgtc.execute("http://tyh25-cs280-todo.herokuapp.com/category");
		
		String categoryJSON = "";
		try {
			categoryJSON = tgtc.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		ArrayList<String> categoryList = new ArrayList<String>();

		categoryList.add("All");
		jp = new JsonParser();
		root = null;
		root = jp.parse(categoryJSON);
		rootobj = root.getAsJsonObject();
		JsonArray categories = rootobj.get("categories").getAsJsonArray();
		for(int i = 0; i < categories.size(); i++){
			JsonObject category = categories.get(i).getAsJsonObject();
			String category_name = category.get("name").getAsString();
			if(!categoryList.contains(category_name)){
				categoryList.add(category_name);
			}
		}
		String[] categoryArray = new String[categoryList.size()];
		categoryArray = categoryList.toArray(categoryArray);
		
		final ArrayList<String> accessList = categoryList;
		
		final Spinner categorySpinner = (Spinner) findViewById(R.id.category_spinner);
		ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoryArray);
		categorySpinner.setAdapter(categoryAdapter);
		categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view,
					int i, long l) {
				/**
				Log.v("hello",accessList.get(i));


				TodoGetTask tgt = new TodoGetTask();
				tgt.setClient(httpclient);
				tgt.setPreferences(settings);
				tgt.execute("http://tyh25-cs280-todo.herokuapp.com/task?category="+accessList.get(i));
				String responseJSON ="";
				try {
					responseJSON = tgt.get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				
				taskHeaderList = new ArrayList<HashMap<String,String>>();
				taskNotesList = new HashMap<String, String>();
				
				JsonParser jp = new JsonParser();
				JsonElement root = null;
				root = jp.parse(responseJSON);
				JsonObject rootobj = root.getAsJsonObject();
				JsonArray tasks = rootobj.get("tasks").getAsJsonArray();
				for(int i1 = 0; i1 < tasks.size(); i1++){
					JsonObject task = tasks.get(i1).getAsJsonObject();
					String category_name = task.get("category_name").getAsString();
					String complete = task.get("complete").getAsString();
					String description = task.get("description").getAsString();
					String due_date = task.get("due_date").getAsString();
					String notes = task.get("notes").getAsString();
					String id = task.get("id").getAsString();
					
					HashMap<String, String> taskHash = new HashMap<String, String>();
					taskHash.put("description", description);
					taskHash.put("category_name", category_name);
					taskHash.put("complete", complete);
					taskHash.put("due_date", due_date);
					taskHash.put("id", id);
					taskHeaderList.add(taskHash);
					taskNotesList.put(description, notes);
				}
				

				taskListView = (ExpandableListView)view.findViewById(R.id.task_expandable_list);
				taskListAdapter = new TaskListAdapter(view.getContext(), taskHeaderList, taskNotesList);
				taskListView.setAdapter(taskListAdapter);
				**/
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}
	class Clicky implements OnClickListener{
		private Activity activity;
		public Clicky(Activity activity){
			this.activity = activity;
		}

		@Override
		public void onClick(View view) {
			Intent listIntent = new Intent(activity, AddTaskActivity.class);
			activity.startActivity(listIntent);
		}
		
		
	}
	class LongClicky implements OnItemLongClickListener{
		private Activity activity;
		public LongClicky(Activity activity){
			this.activity = activity;
		}
		@Override
	    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
	    	Log.v("position", String.valueOf(position));
	    	
			HashMap<String,String> taskHash = taskHeaderList.get(position);
			String taskNotes = taskNotesList.get(taskHash.get("description"));

			Intent listIntent = new Intent(activity, AddTaskActivity.class);
			listIntent.putExtra("id", taskHash.get("id"));
			listIntent.putExtra("description", taskHash.get("description"));
			listIntent.putExtra("category", taskHash.get("category_name"));
			listIntent.putExtra("due_date", taskHash.get("due_date"));
			listIntent.putExtra("notes", taskNotes);
			activity.startActivity(listIntent);
	    	return true;
	    }
	}
	
	@Override
	protected void onStop(){
		super.onStop();
		setResult(0);
	}

}
