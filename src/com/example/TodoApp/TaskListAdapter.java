package com.example.TodoApp;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class TaskListAdapter extends BaseExpandableListAdapter{
	private Context context;
	private List<HashMap<String,String>> taskHeaderList;
	private HashMap<String, String> taskNotesList;
	
	public TaskListAdapter(Context context, List<HashMap<String,String>> taskHeaderList, HashMap<String, String> taskNotesList){
		this.context = context;
		this.taskHeaderList = taskHeaderList;
		this.taskNotesList = taskNotesList;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition){
		Log.v("is selecting", "selecting");
		return taskNotesList.get(this.taskHeaderList.get(groupPosition).get("description"));
	}
	
	@Override
	public long getChildId(int groupPosition, int childPosition){
		return childPosition;
	}
	
	@Override
	public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent){
		
		Log.v("is selecting", "selecting");
		final String taskDescriptionText = (String) getChild(groupPosition, childPosition);
		
		if (convertView == null){
			LayoutInflater childInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = childInflater.inflate(R.layout.list_item, null);
		}
		TextView taskDescriptionView = (TextView)convertView.findViewById(R.id.taskNotes);
		taskDescriptionView.setText(taskDescriptionText);

		Log.v("is selecting", taskDescriptionText);
		return convertView;
	}
	
	@Override
	public int getChildrenCount(int groupPosition){
		return 1;
	}
	
	@Override
	public HashMap<String,String> getGroup(int groupPosition){
		return taskHeaderList.get(groupPosition);
	}
	
	@Override
	public int getGroupCount(){
		return taskHeaderList.size();
	}
	
	@Override
	public long getGroupId(int groupPosition){
		return groupPosition;
	}
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent){
		String taskDescription = (String) getGroup(groupPosition).get("description");
		String taskCategory = (String) getGroup(groupPosition).get("category_name");
		String taskDueDate = (String) getGroup(groupPosition).get("due_date"); 

		boolean taskCompleted;
		if(getGroup(groupPosition).get("complete").equals("true")){
			taskCompleted = true;
		} else {
			taskCompleted = false;
		}
		
		if(convertView == null) {
			LayoutInflater groupInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = groupInflater.inflate(R.layout.list_group, null);
		}
		
		TextView taskDescriptionView = (TextView)convertView.findViewById(R.id.taskDescription);
		//CheckBox taskDescriptionBox = (CheckBox)convertView.findViewById(R.id.taskCheckBox);
		TextView taskCategoryView = (TextView)convertView.findViewById(R.id.taskCategory);
		TextView taskDueDateView = (TextView)convertView.findViewById(R.id.taskDueDate);
	
	//	taskDescriptionBox.setText(taskDescription);
		//taskDescriptionBox.setChecked(taskCompleted);
		taskDescriptionView.setText(taskDescription);
		taskCategoryView.setText(taskCategory);
		taskDueDateView.setText(taskDueDate);
		return convertView;
	}
	
    @Override
    public boolean hasStableIds() {
        return false;
    }
 
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
