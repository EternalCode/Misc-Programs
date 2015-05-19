package com.example.triageapp;

//import builtins and packages
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import access_manager.LoginManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class RegisterActivity extends Activity implements OnCheckedChangeListener {
	// globals created for reference later on
	private LoginManager manager;
	public static String FILENAME = "password.txt";
	private RadioGroup radiogroup;
	private RadioButton nurse, physician;
	private String usertype = "";

	/**
	 * A constructor for activity layout 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		//call identifyUser helped method to help identify register 
		identifyUser();

		try {
			manager = new LoginManager(this.getApplicationContext()
					.getFilesDir(), FILENAME);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * A help method to help identifying the user is physician or nurse
	 */
	private void identifyUser() {
		radiogroup = (RadioGroup) findViewById(R.id.RadioGroup);
		nurse = (RadioButton) findViewById(R.id.nurseRadioButton);
		physician =  (RadioButton) findViewById(R.id.physicianRadioButton);
		
		//set the radio buttons as a group to be clicked as nurse or physician
		radiogroup.setOnCheckedChangeListener(this);
	}
	
	/**
	 * when user clicked the radio buttons, it will allow user click only
	 * one button
	 */
	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		switch(arg0.getCheckedRadioButtonId()){
		case R.id.nurseRadioButton:
			break;
		case R.id.physicianRadioButton:
			break;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}
	

	/**
	 * this method will be called when the submit button is clicked
	 * SubmitToFile attempts to register a nurse or a physician.
	 *  it asks for correctness input and raise alert boxes to stop 
	 *  wrong input
	 * @param v
	 *            this.view
	 */
	public void SubmitToFile(View v) {
		// get the user inputs from the text fields and assign them to variables
		EditText userNameText = (EditText) findViewById(R.id.r_username);
		String username = userNameText.getText().toString();

		EditText passwordText = (EditText) findViewById(R.id.r_password);
		String password = passwordText.getText().toString();

		
		//judge if the register user is nurse or physician
		if(nurse.isChecked()){
			usertype = "nurse";
		}else if(physician.isChecked()){
			usertype = "physician";
		}else{
			//if user didn't check a radiobutton
			CheckBoxEmpty(v);
		}
		
		// start up file writer
		FileOutputStream fop = null;

		try {
			fop = new FileOutputStream(new File(this.getApplicationContext()
					.getFilesDir(), FILENAME));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// check if the input fields were empty if they were give an alert if
		if ((username.length() == 0) || (password.length() == 0)) {
			MissValueBox(v);
			// if the nurse has the patient
		} else if (manager.hasThisEmployee(username)) {
			ExistenceOfNurseAccount(v);
		} else {
			// if inputs non-empty
			String[] info = {password, usertype};
			manager.addEmployee(username, info);
			manager.saveToFile(fop);
			successfulBox(v);
		}
	}

	//==================This line is for dividing methods by different works================================
	//redirect button
	
	/**
	 * BackToLogin is essentially a logout button that takes the user back to
	 * the login screen
	 * 
	 * @param v
	 *            this.view
	 */
	public void BackToLogin(View v) {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	//==================This line is for dividing methods by different works================================
	//Helping  alert boxes
			
	/**
	* MissValueBox is a method which creates a pop-up/alert/prompt box and
	* tells the user that they are missing login info values
	* 
	* @param v
	*           this.view
	*/
	public void MissValueBox(View v) {
		// create alert box with specified outputs
		new AlertDialog.Builder(this).setTitle("Error")
				.setMessage("No Value in Username or Password")
				.setNeutralButton("ok", null).show();
		}

		/**
		 * successfulBox is a method which create a pop-up/alert/prompt box and
		 * tells the user that they have successfully registered a nurse
		 * 
		 * @param v
		 *            this.view
		 */
		public void successfulBox(View v) {
			// create alert box with specified outputs
			new AlertDialog.Builder(this).setTitle("successful")
					.setMessage("Employee Register Successfully")
					.setNeutralButton("ok", null).show();
		}
		
		/**
		 * ChcekBoxEmpty will be raised when no radio button is clicked
		 * to ask user redo the clicking
		 * @param v
		 * 			this.view
		 */
		public void CheckBoxEmpty(View v) {
			// create alert box with specified outputs
			new AlertDialog.Builder(this).setTitle("Error")
					.setMessage("You need to choose either nurse or physician")
					.setNeutralButton("ok", null).show();
		}
		
		/**
		 * When the username is registered, this alert box will be raise
		 * @param v
		 * 		this.view
		 */
		private void ExistenceOfNurseAccount(View v) {
			new AlertDialog.Builder(this).setTitle("Error")
					.setMessage("Sorry, this username has been registered")
					.setNeutralButton("ok", null).show();
		}

}
