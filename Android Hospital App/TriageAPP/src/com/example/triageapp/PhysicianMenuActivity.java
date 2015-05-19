package com.example.triageapp;


import hospital.Patient;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import filemanager.Physician;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class PhysicianMenuActivity extends Activity {
	//create globals for easy references later on
	public static String FILENAME = "Patient.txt";

	private String item;
	private Physician physician;
	private Patient patient;
	private ScrollView patientinfodisplay;
	private LinearLayout prescriptionlayout;
	private TextView viewdisplay;

	/**
	 * A constructor of activity layout 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_physician_menu);

		patientinfodisplay = (ScrollView) findViewById(R.id.p_scrolldisplay);
		viewdisplay = (TextView) findViewById(R.id.p_DisplayPinfor);
		prescriptionlayout = (LinearLayout) findViewById(R.id.p_prescriptionlayout);
		
		//get information from files to be using
		 try {
				physician = new Physician (this.getApplicationContext().getFilesDir(),
						FILENAME);
			} catch (IOException e) {
				e.printStackTrace();
			}
		 
		 
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.physician_menu, menu);
		return true;
	}
	
	
	//==================This line is for dividing methods by different works================================
	//redirect buttons
	
	/**
	 * BackToLogin is essentially a logout button that takes the user back to
	 * the login screen
	 * 
	 * @param v
	 *            this.view
	 */
	public void BackToLogin(View v){
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
	
	//==================This line is for dividing methods by different works================================
	//Major works
		
	/**
	 * LookupPatient is called when the search button is clicked
	 * it gets given input health card number's patient information
	 * and display below 
	 * @param v
	 * 			this.view
	 */
	public void LookupPatient(View v){
		EditText HealthCardNum = (EditText) findViewById(R.id.p_HID);
		String healthcard_ID = HealthCardNum.getText().toString();
	
		//check the empty case of edittext
		if (healthcard_ID.length() == 0){
			MissHCNBox(v);
		}else if(! physician.HavePatient(healthcard_ID)){
			//if the input health card number does not exist
			PatientNoExist(v);
		}else{
			
			patient = physician.getByHealthCard(healthcard_ID);
			//item will be passed to alert box to display the successful work
			item = patient.getName() + " " + healthcard_ID;
			
			//show the layout and display the information for user
			prescriptionlayout.setVisibility(View.VISIBLE);
			patientinfodisplay.setVisibility(View.VISIBLE);
			
			//get target patient's information and display
			String display = patient.showInfo();
			viewdisplay.setText(display);
			
		}
		
	}
	
	/**
	 * Submit_prescription is called when the submit button is called
	 * it gathers information from edit text and save into file
	 * @param v
	 * 			this.view
	 */
	public void Submit_prescription(View v){
		EditText pres = (EditText) findViewById(R.id.p_prescription);
		String newprescription = pres.getText().toString();
		
		
		FileOutputStream fop = null;
		try {
			fop = new FileOutputStream( new File(this.getApplicationContext().getFilesDir(),
						FILENAME));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//check empty case
		if (newprescription.length() == 0){
			MissPIBox(v);
		}else if(patient.conditionsEmpty()){
			noConditionBox(v);
		}
		else{
			//let physician edit the new prescription for the target vitalsign
			physician.addPrescription(patient, newprescription);
			physician.saveToFile(fop);
			SuccessfulBox(v);
		}

	}
	
	//==================This line is for dividing methods by different works================================
	//Helping  alert boxes
   
	/**
	 * PatientNoExistis a method that gives an alert telling the user that
	 * the specified patient does not exist
	 * @param v
	 * 			this.view
	 */
	private void PatientNoExist(View v) {
		//create an alert box with specified properties
    	new AlertDialog.Builder(this)
    	.setTitle("Error")
    	.setMessage("Sorry, This Patient doesn't exist")
    	.setNeutralButton("ok", null)
    	.show();
	}
	
	/**
	 * SuccessfulBox a method that gives an alert telling the user that
	 * submit is successful
	 * @param v
	 * 			this.view
	 */
    private void SuccessfulBox(View v){
    	//create an alert box with specified properties
    	new AlertDialog.Builder(this)
    	.setTitle("Submit Successfully!")
    	.setMessage("New prescription information has been added to" + " " +item)
    	.setNeutralButton("ok", null)
    	.show();
    }
    
	/**
	 * MissHCNBox a method that gives an alert telling the user that
	 * no health card number is entered.
	 * @param v
	 * 			this.view
	 */
	private void MissHCNBox(View v){
		//create an alert box with specified properties
		new AlertDialog.Builder(this)
		.setTitle("Error")
		.setMessage("Sorry, please enter a health card number of a patient")
		.setNeutralButton("ok", null)
    	.show();
	}
	
	/**
	 * MissPIBox a method that gives an alert telling the user that
	 * no prescription information is entered.
	 * @param v
	 * 			this.view
	 */
	private void MissPIBox(View v){
		//create an alert box with specified properties
		new AlertDialog.Builder(this)
		.setTitle("Error")
		.setMessage("Sorry, please enter the prescription information")
		.setNeutralButton("ok", null)
    	.show();
	}
	
	/**
	 * the alert box will be raised when the nurse have not given vitalsign
	 * for a patient before the physician try to add prescription to null
	 * @param v
	 * 			this.view
	 */
	private void noConditionBox(View v){
		new AlertDialog.Builder(this)
		.setTitle("Error")
		.setMessage("Nurse have not given this patient a vitalsign")
		.setNeutralButton("ok", null)
    	.show();
	}
}
