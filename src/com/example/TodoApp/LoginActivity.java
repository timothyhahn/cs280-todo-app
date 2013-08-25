package com.example.TodoApp;

import java.util.Locale;
import java.util.concurrent.ExecutionException;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class LoginActivity extends FragmentActivity implements
		ActionBar.TabListener {

	private final static String todoURL = "http://tyh25-cs280-todo.herokuapp.com";

	public static final String PREFS_NAME = "TodoPrefs";
	static HttpClient httpclient;
	static SharedPreferences settings;
	
	SectionsPagerAdapter mSectionsPagerAdapter;

	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		httpclient = new DefaultHttpClient();
		settings = getSharedPreferences(PREFS_NAME, 0);
		
		// If already logged in
		/**
		if(settings.getString("username", null) != null && settings.getString("password", null) != null){

			TodoPostTask tpt = new TodoPostTask();
			String postURL = todoURL + "/login";
			String username = settings.getString("username", null);
			String password = settings.getString("password", null);
			tpt.setClient(httpclient);
			tpt.setPreferences(settings);
			tpt.execute(postURL, TodoPostTask.LOGIN, username, password);
			JsonParser jp = new JsonParser();
			JsonElement root = null;
			root = jp.parse(responseJSON);
			JsonObject rootobj = root.getAsJsonObject();
			int id = rootobj.get("id").getAsInt();
			if (id > 0) {
				Intent listIntent = new Intent(this, TodoHome.class);
				startActivity(listIntent);
			} else {
				TextView errorText = (TextView)findViewById(R.id.fragment_login_error);
				errorText.setVisibility(View.VISIBLE);
			}
		}
		*/
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	   if (resultCode == 0) 
	       finish();
   }

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = new LoginSectionFragment();
				break;
			case 1:
				fragment = new RegisterSectionFragment();
			}
			
			return fragment;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.login_title).toUpperCase(l);
			case 1:
				return getString(R.string.register_title).toUpperCase(l);
			}
			return null;
		}
	}
	
	public static class LoginSectionFragment extends Fragment {
		public LoginSectionFragment(){
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
			if(container == null){
				return null;
			}

			View view = inflater.inflate(R.layout.fragment_login, container, false);
			
			TextView errorText = (TextView)view.findViewById(R.id.fragment_login_error);
			errorText.setVisibility(View.INVISIBLE);
			
			Button loginButton = (Button)view.findViewById(R.id.fragment_login_button);
			final View accessView = view;
			loginButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view){
					TodoPostTask tpt = new TodoPostTask();
					String postURL = todoURL + "/login";
					EditText usernameEditText = (EditText)accessView.findViewById(R.id.fragment_login_username);
					EditText passwordEditText = (EditText)accessView.findViewById(R.id.fragment_login_password);
					String username = usernameEditText.getText().toString();
					String password = passwordEditText.getText().toString();
					tpt.setClient(httpclient);
					tpt.setPreferences(settings);
					tpt.execute(postURL, TodoPostTask.LOGIN, username, password);
					String responseJSON ="";
					try {
						responseJSON = tpt.get();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
					// TODO Ensure it actually checks if login is valid
					JsonParser jp = new JsonParser();
					JsonElement root = null;
					root = jp.parse(responseJSON);
					JsonObject rootobj = root.getAsJsonObject();
					int id = rootobj.get("id").getAsInt();
					if (id > 0) {
						Intent listIntent = new Intent(getActivity(), TodoHome.class);
						getActivity().startActivity(listIntent);
					} else {
						TextView errorText = (TextView)accessView.findViewById(R.id.fragment_login_error);
						errorText.setVisibility(View.VISIBLE);
					}
				}
			});
			return view;
		}
	}
	

	public static class RegisterSectionFragment extends Fragment {
		public RegisterSectionFragment(){
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
			if(container == null){
				return null;
			}

			View view = inflater.inflate(R.layout.fragment_register, container, false);
			
			Button registerButton = (Button)view.findViewById(R.id.fragment_register_button);
			final View accessViewGroup = view;
			registerButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					TodoPostTask tpt = new TodoPostTask();
					String postURL = todoURL + "/register";
					EditText usernameEditText = (EditText)accessViewGroup.findViewById(R.id.fragment_register_username);
					EditText passwordEditText = (EditText)accessViewGroup.findViewById(R.id.fragment_register_password);
					String username = usernameEditText.getText().toString();
					String password = passwordEditText.getText().toString();
					tpt.setClient(httpclient);
					tpt.setPreferences(settings);
					tpt.execute(postURL, TodoPostTask.REGISTER, username, password);
					String responseJSON = "";
					try {
						responseJSON = tpt.get();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
					
					Intent listIntent = new Intent(getActivity(), TodoHome.class);
					getActivity().startActivity(listIntent);
					
				}
				
			});
			return view;
		}
	}
}
