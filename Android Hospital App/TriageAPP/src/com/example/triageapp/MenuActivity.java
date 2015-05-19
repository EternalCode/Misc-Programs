package com.example.triageapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	/**
	 * GoToNewPatientEditor starts the AddNewActivty activity
	 * 
	 * @param v
	 *            this.view
	 */
	public void GoToNewPatientEditor(View v) {
		// start a new activity called AddNew
		Intent intent = new Intent(this, AddNewActivity.class);
		startActivity(intent);

	}

	/**
	 * GoToVitalSignEditor starts AddVitalsignActicty activity
	 * 
	 * @param v
	 *            this.view
	 */
	public void GoToVitalSignEditor(View v) {
		// start a new activity called AddVitalsign
		Intent intent = new Intent(this, AddVitalsignActivity.class);
		startActivity(intent);
	}

	/**
	 * ChcekPatientInfo starts LookUpPatientActivity
	 * @param v
	 * 			this.view
	 */
	public void CheckPatientInfo(View v){

		Intent intent = new Intent(this, LookUpPatientActivity.class);
		startActivity(intent);
	}
	
	/**
	 * AccessPatientList starts AccessPatientsListActivity
	 * @param v
	 * 				this.view
	 */
	public void AccessPatientList(View v){
		Intent intent = new Intent(this, AccessPatientsListActivity.class);
		startActivity(intent);
	}
	
	/**
	 * BackToLogin starts a new activity MainActivity which happens to be the
	 * same activity a back button would link to in this activity
	 * 
	 * @param v
	 *            this.view
	 */
	public void BackToLogin(View v) {
		// start new activity
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);

	}
}
