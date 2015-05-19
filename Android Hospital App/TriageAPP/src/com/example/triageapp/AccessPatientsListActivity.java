package com.example.triageapp;

import java.io.IOException;

import filemanager.Nurse;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class AccessPatientsListActivity extends Activity {
	
	public static String FILENAME = "Patient.txt";
	private Nurse nurse;
	
	/**
	 * The constructor of layout, it will run when the layout starts
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_access_patients_list);
		
		TextView display1 = (TextView) findViewById(R.id.ac_DisplayList);
		TextView display2 = (TextView) findViewById(R.id.ac_DisplayList2);
		//Take a look from the patients.txt file
		try {
			nurse = new Nurse(this.getApplicationContext().getFilesDir(),
					FILENAME);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//get the sorted list by nurse class and give it to display TextView
		display1.setText(nurse.getSortListArrival());
		display2.setText(nurse.getSortListUrgency());
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.access_patients_list, menu);
		return true;
	}

	
	/**
	 * BackToMenu is a method that is called which directs the user into the
	 * main menu activity
	 * 
	 * @param v
	 *            this.view
	 */
	public void BackToMenu(View v) {
		// direct to main menu
		Intent intent = new Intent(this, MenuActivity.class);
		startActivity(intent);
	}

	
	/**
	 * BackToLogin is essentially a logout button that takes the user back to
	 * the login screen
	 * 
	 * @param v
	 *            this.view
	 */
	public void BackToLogin(View v) {
		// redirect user to login
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

}
