package com.example.triageapp;

//import needed builtins and package files
import hospital.PatientCondition;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import filemanager.Nurse;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

/**
 * Class that deals with the addition of vital signs to a specific patient
 * Contains methods with aid in this goal as well as some graphical assistance
 * methods which help the user identify what to do in this activity
 *
 */
public class AddVitalsignActivity extends Activity {
	//create globals for easy references later on
	private Nurse nurse;
	private String item;
	public static String FILENAME = "Patient.txt";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addvitalsign);
		
		
		 try {
				nurse = new Nurse(this.getApplicationContext().getFilesDir(),
						FILENAME);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_vitalsign, menu);
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
	
	

	//==================This line is for dividing methods by different works================================
	//Major Works
	
    
    /**
     * SubmitToPatient attempt to insert a vital sign to a specified patient and 
     * if the insert was successful it will write the changes to the file
     * @param v this.view
     */
	public void SubmitToPatient(View v){
		
		//get user inputs from the text field
		EditText name = (EditText) findViewById(R.id.A_patientname);
		EditText temp = (EditText) findViewById(R.id.A_temp);
		EditText bloodp = (EditText) findViewById(R.id.A_bloodp);
		EditText heartr = (EditText) findViewById(R.id.A_heartr);
		EditText vitaltime = (EditText) findViewById(R.id.A_time);
		//convert inputs into strings
		String n = name.getText().toString();
		String tp = temp.getText().toString();
		String bp = bloodp.getText().toString();
		String hr = heartr.getText().toString();
		String vt = vitaltime.getText().toString();
		String prescription = "None";
		//prepare to write into FILENAME is default storage directory
		FileOutputStream fop = null;
		try {
			fop = new FileOutputStream( new File(this.getApplicationContext().getFilesDir(),
						FILENAME));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		//check if all values are inserted
		if(tp.length()==0 || bp.length()==0 || hr.length()==0 || vt.length()==0){
			//if some values are missing give alert
			MissValueBox(v);
		//prevent patient name empty/null case
		}else if(n.length()==0){
			PatientNameBox(v);
		}else if (! doubleInputCheck(tp, bp, hr)){
			structureError(v);
		}else if(! timeCheck(vt)){
			timeStructureError(v);
		// check if nurse has patient
		}else if(nurse.haveThisPatient(n)){
		//if nurse has patient write vitals to then save this condition to the patient
		PatientCondition condition = new PatientCondition(vt,tp,bp,hr,prescription);
		nurse.setPatientCondition(n, condition);
		//write changes to file
		nurse.saveToFile(fop);
		item = n;
		//show successful alert
		successfulBox(v);
		//give patient doesn't exist alert if the patient doesn't exist
		}else{
			PatientNoExist(v);
		}

	}


	/**
	 * A Helping method to help identifying vital sign recorded time as 
	 * correct format
	 * @param arrivetime a string represents vital sign recording time
	 * @return true if the format is correct
	 */
	private boolean  timeCheck(String time) {
		if (time.length() == 5) {
			String H = time.substring(0, 2);
			String M = time.substring(3, 5);
			
			//Initialize the integers
			int Hour=0, Min=0;
			try {
				//if the substrings are able to convert to integer
				//pass values to integers
				Hour = Integer.parseInt(H);
				Min = Integer.parseInt(M);
			} catch (NumberFormatException e) {
				//when the substrings do not represent integer
				return false;
			}
			if ((Hour<25) && (Hour>=00) && (Min<60) && (Min>=00)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * A Helping method to help identifying input values as 
	 * correct format
	 * @param input1 the string represents the input value
	 * @param input2 the string represents the input value
	 * @param input3 the string represents the input value
	 * @return true if the format is correct
	 */
	private boolean doubleInputCheck(String input1, String input2, String input3) {
		try {
			Double.parseDouble(input1);
			Double.parseDouble(input2);
			Double.parseDouble(input3);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	//==================This line is for dividing methods by different works=====================================
	//Helping  alert boxes
		
	/**
	 * PatientNameBox is a method that gives an alert telling the user to input
	 * a patient name into the field
	 * @param v this.view
	 */
	private void PatientNameBox(View v) {
		//create an alert box with specified properties
    	new AlertDialog.Builder(this)
    	.setTitle("Error")
    	.setMessage("Please Give a Patient Name Below")
    	.setNeutralButton("ok", null)
    	.show();
	}
	
	/**
	 * PatientNoExistis a method that gives an alert telling the user that
	 * the specified patient does not exist
	 * @param v
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
	 * MissValueBox is a method which creates a pop-up/alert/prompt box and
	 * tells the user that they are missing vital sign info values
	 * 
	 * @param v
	 *            this.view
	 */
    public void MissValueBox(View v){
    	new AlertDialog.Builder(this)
    	.setTitle("Error")
    	.setMessage("No Value in vitalsigns")
    	.setNeutralButton("ok", null)
    	.show();
    }
    
	/**
	 * successfulBox is a method which create a pop-up/alert/prompt box and
	 * tells the user that they have successfully added vital signs to a patient
	 * 
	 * @param v
	 *            this.view
	 */
    public void successfulBox(View v){
    	new AlertDialog.Builder(this)
    	.setTitle("successful")
    	.setMessage("Vitalsign has been added to" + " " +item)
    	.setNeutralButton("ok", null)
    	.show();
    }
    
	
	/**
	 * Alert box to alert the user about wrong input format
	 * @param v
	 * 			this.view
	 */
	private void structureError(View v){
		new AlertDialog.Builder(this).setTitle("FormatError")
		.setMessage("Format of Temperture, HeartRate, BloodPressure should be double format eg: 37.19")
		.setNeutralButton("ok", null).show();
	}
	
	/**
	 * Alert box to alert the user about wrong input format
	 * @param v
	 * 			this.view
	 */
	private void timeStructureError(View v) {
		new AlertDialog.Builder(this).setTitle("FormatError")
		.setMessage("Format of record time should be HH/MM eg:05:12")
		.setNeutralButton("ok", null).show();
	}
}
