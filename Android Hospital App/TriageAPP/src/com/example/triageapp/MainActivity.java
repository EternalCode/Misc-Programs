package com.example.triageapp;

import java.io.IOException;

import access_manager.LoginManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

/**
 * MainActivity is the main login screen. It handles user login as well as allow
 * a user to register a nurse for later logins
 * 
 * @author group_0326
 * 
 */
public class MainActivity extends Activity {

	// create globals for reference in later methods
	private LoginManager manager;
	public static String FILENAME = "password.txt";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		try {
			manager = new LoginManager(this.getApplicationContext()
					.getFilesDir(), FILENAME);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * NotMatchBox is a method which creates a pop-up/alert/prompt box and tells
	 * the user that they're login values are incorrect
	 * 
	 * @param v
	 *            this.view
	 */
	public void NotMatchBox(View v) {
		new AlertDialog.Builder(this).setTitle("Error")
				.setMessage("Invaild username or password")
				.setNeutralButton("ok", null).show();
	}

	/**
	 * MissValueBox is a method which creates a pop-up/alert/prompt box and
	 * tells the user that they are missing login info values
	 * 
	 * @param v
	 *            this.view
	 */
	public void MissValue(View v) {
		// create new alert with some specified details
		new AlertDialog.Builder(this).setTitle("Error")
				.setMessage("No Value in Username or Password")
				.setNeutralButton("ok", null).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * GoToRegister starts the RegisterActivity
	 * 
	 * @param v
	 *            this.view
	 */
	public void GoToRegister(View v) {
		// start new activity
		Intent register = new Intent(this, RegisterActivity.class);
		startActivity(register);
	}

	/**
	 * LoginToMenu attempts to log the user in if the login details are correct
	 * otherwise shows a prompt/alert
	 * 
	 * @param v
	 *            this.view
	 */
	public void LoginToMenu(View v) {
		// start activity
		Intent menu = new Intent(this, MenuActivity.class);
		Intent physicianmenu = new Intent(this, PhysicianMenuActivity.class);
		// get details of user input
		EditText userNameText = (EditText) findViewById(R.id.username);
		String username = userNameText.getText().toString();

		EditText passwordText = (EditText) findViewById(R.id.password);
		String password = passwordText.getText().toString();
		// check if the input is empty
		if ((username.length() == 0) || (password.length() == 0)) {
			// for empty input give prompt
			MissValue(v);
		} else if (manager.isMatchEmployees(username, password)) {
			// inner if statement to separate the activity to entry.
			if(manager.isNurse(username)){
				startActivity(menu);
			}else{
				startActivity(physicianmenu);
			}
		
		} else {
			// input not correct show prompt
			NotMatchBox(v);
		}

	}

	/**
	 * onPause kills the activity after an intent to another activity
	 */
	public void onPause() {
		super.onPause();
		// kill activity
		finish();
	}
}
