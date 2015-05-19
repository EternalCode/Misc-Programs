package com.example.triageapp;

import hospital.Patient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import filemanager.Nurse;
//import filemanager.NurseManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * AddNewctivity is the activity responsible for adding patients it contains
 * methods for aiding this goal and other methods for graphically communicating
 * with the user
 * 
 * @author group_0326
 * 
 */
public class AddNewActivity extends Activity implements OnCheckedChangeListener {
	public static String FILENAME = "Patient.txt";

	// Used in multiple occasions so made it global
	private Nurse nurse;
	private RadioGroup radiogroup;
	private RadioButton seendoctor, notseendoctor;
	private LinearLayout layout;
	private String seendoctortime, seendoctordate, timedates;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addnew);

		// a method that is used for set the radiobuttons
		setradiogroup();

		// go through the file to get information
		try {
			nurse = new Nurse(this.getApplicationContext().getFilesDir(),
					FILENAME);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * A method that uses to set the radio buttons in a group and make the click
	 * to be unique
	 */
	private void setradiogroup() {
		radiogroup = (RadioGroup) findViewById(R.id.n_radiogroup);
		seendoctor = (RadioButton) findViewById(R.id.n_seendoctor);
		notseendoctor = (RadioButton) findViewById(R.id.n_notseendoctor);
		layout = (LinearLayout) findViewById(R.id.invisiablelayout);

		// accept the button to be clicked, but only one button should be
		// clicked
		radiogroup.setOnCheckedChangeListener(this);
	}

	/**
	 * A method to help the radio buttons to be clicked
	 */
	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		switch (arg0.getCheckedRadioButtonId()) {
		// when the seen doctor button is clicked, show the rest information
		case R.id.n_seendoctor:
			layout.setVisibility(View.VISIBLE);
			break;
		// when the not seen doctor button is clicked, do nothing
		case R.id.n_notseendoctor:
			layout.setVisibility(View.INVISIBLE);
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_new, menu);
		return true;
	}

	// =============================================================
	// redirect buttons
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

	// ===========================================================================
	// Major works

	/**
	 * This method will be called when the submit button is called It gathers
	 * all informations and put into files by Nurse It will ask for correctness
	 * input
	 * 
	 * @param v
	 *            this.view
	 */
	public void SubmitPatientinfo(View v) {
		// get user inputs from the text fields
		EditText name = (EditText) findViewById(R.id.n_name);
		String n = name.getText().toString();

		EditText dob = (EditText) findViewById(R.id.n_dob);
		String d = dob.getText().toString();

		EditText HID = (EditText) findViewById(R.id.n_number);
		String H = HID.getText().toString();

		EditText arrivaltime = (EditText) findViewById(R.id.n_arrivaltime);
		String at = arrivaltime.getText().toString();

		// get the value when the radio button is clicked
		if (seendoctor.isChecked()) {
			EditText seendate = (EditText) findViewById(R.id.n_seendoctordate);
			seendoctordate = seendate.getText().toString();

			EditText seentime = (EditText) findViewById(R.id.n_seendoctortime);
			seendoctortime = seentime.getText().toString();

			if ((!datesCheck(seendoctordate))) {
				datesTimeBox(v);
			}else if ((!timeCheck(seendoctortime))){
				timeStructureError(v);
			}
			// group the dates and time together convenient to be saved
			else {
				timedates = seendoctordate + " " + seendoctortime;
			}
		} else if (notseendoctor.isChecked()) {
			// if the patient have not been seen a doctor, mark the timedates as
			// none
			timedates = "None";
		} else {
			RadioButtonEmpty(v);
		}

		// get the file directory and create a output stream object who writes
		// to that directory
		FileOutputStream fop = null;
		try {
			fop = new FileOutputStream(new File(this.getApplicationContext()
					.getFilesDir(), FILENAME));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// if all fields of text are not completed, show alert and force input
		// to continue
		if (n.length() == 0 || d.length() == 0 || H.length() == 0
				|| at.length() == 0) {
			MissValueBox(v);
		} else if (!datesCheck(d)) {
			// if the date of birth structure are not matching raise alert box
			dobstructureError(v);
		} else if (!cardIDCheck(H)) {
			// if the health card number's structure are not correct
			// raise alert box
			IDstructureError(v);
		} else if (!timeCheck(at)) {
			// if the arrival time is not in correct structure
			// raise alert box
			timeStructureError(v);
		} else if (nurse.haveThisPatient(n)) {
			ExistenceOfPatient(v);
		}else if (nurse.HavePatient(H)){
			ExistencePatientID(v);
		} else {
			// if all info is present, then we write to file and save patient
			Patient patient = new Patient(n, d, H, at, timedates);
			nurse.addPatient(patient);
			nurse.saveToFile(fop);
			// show alert the tell user that the save was successful
			successfulBox(v);
		}
	}

	/**
	 * Helping method to help check the structure of date of birth
	 * 
	 * @param dob
	 *            a string represents date of birth
	 * @return true if the structure is correct
	 */
	private boolean datesCheck(String dob) {
		// Check the structure of date of birth
		if (dob.length() == 10) {
			String M = dob.substring(0, 2);
			String D = dob.substring(3, 5);
			String Y = dob.substring(6, 10);

			// Initialize the integers
			int MM = 0, DD = 0, YYYY = 0;
			try {
				// if the substrings are able to convert to integer
				// pass values to integers
				MM = Integer.parseInt(M);
				DD = Integer.parseInt(D);
				YYYY = Integer.parseInt(Y);
			} catch (NumberFormatException e) {
				// when the substrings do not represent integer
				return false;
			}
			if ((MM < 13) && (MM > 00) && (DD < 32) && (DD > 00)
					&& (YYYY < 2014) && (YYYY > 0000)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * A Helping method to help identifying health card number as correct format
	 * 
	 * @param cardnum
	 *            a string represents health cards number
	 * @return true if the format is correct
	 */
	private boolean cardIDCheck(String cardnum) {
		try {
			Integer.parseInt(cardnum);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * A Helping method to help identifying arrival time as correct format
	 * 
	 * @param arrivetime
	 *            a string represents arrival time
	 * @return true if the format is correct
	 */
	private boolean timeCheck(String arrivetime) {
		if (arrivetime.length() == 5) {
			String H = arrivetime.substring(0, 2);
			String M = arrivetime.substring(3, 5);

			// Initialize the integers
			int Hour = 0, Min = 0;
			try {
				// if the substrings are able to convert to integer
				// pass values to integers
				Hour = Integer.parseInt(H);
				Min = Integer.parseInt(M);
			} catch (NumberFormatException e) {
				// when the substrings do not represent integer
				return false;
			}
			if ((Hour < 25) && (Hour >= 00) && (Min < 60) && (Min >= 00)) {
				return true;
			}
		}
		return false;
	}

	// =========================================================================
	// Helping alert boxes

	/**
	 * MissValueBox is a method which creates a pop-up/alert/prompt box and
	 * tells the user that they are missing patient info values
	 * 
	 * @param v
	 *            this.view
	 */
	public void MissValueBox(View v) {
		// create an alert box and make it display some
		// relevant text to the user
		new AlertDialog.Builder(this).setTitle("Error")
				.setMessage("No Value in patient's information")
				.setNeutralButton("ok", null).show();
	}

	/**
	 * successfulBox is a method which create a pop-up/alert/prompt box and
	 * tells the user that they have successfully registered a patient
	 * 
	 * @param v
	 *            this.view
	 */
	public void successfulBox(View v) {
		new AlertDialog.Builder(this).setTitle("successful")
				.setMessage("New Patient has been added")
				.setNeutralButton("ok", null).show();
	}

	/**
	 * An alert box to announce user click a radio button
	 * 
	 * @param v
	 *            this.view
	 */
	public void RadioButtonEmpty(View v) {
		// create alert box with specified outputs
		new AlertDialog.Builder(this).setTitle("Error")
				.setMessage("You need to choose patient saw a doctor or not")
				.setNeutralButton("ok", null).show();
	}

	/**
	 * ExistenceOfPatient method creates a prompt box which tells the user that
	 * the patient can't be registered
	 * 
	 * @param v
	 *            this.view
	 */
	private void ExistenceOfPatient(View v) {
		// create prompt with specified values
		new AlertDialog.Builder(this).setTitle("Error")
				.setMessage("Sorry, this Patient has been registered")
				.setNeutralButton("ok", null).show();
	}
	
	private void ExistencePatientID(View v){
		new AlertDialog.Builder(this).setTitle("Error")
		.setMessage("Sorry, this HealthCardNumber has been registered")
		.setNeutralButton("ok", null).show();
	}

	/**
	 * Alert box to alert the user about wrong input format
	 * 
	 * @param v
	 *            this.view
	 */
	private void dobstructureError(View v) {
		new AlertDialog.Builder(this)
				.setTitle("FormatError")
				.setMessage(
						"Format of date of birth is MM/DD/YYYY, invalid dates won't be tanken")
				.setNeutralButton("ok", null).show();
	}

	/**
	 * Alert box to alert the user about wrong input format
	 * 
	 * @param v
	 *            this.view
	 */
	private void IDstructureError(View v) {
		new AlertDialog.Builder(this).setTitle("FormatError")
				.setMessage("Format of health card should be numeric")
				.setNeutralButton("ok", null).show();
	}

	/**
	 * Alert box to alert the user about wrong input format
	 * 
	 * @param v
	 *            this.view
	 */
	private void timeStructureError(View v) {
		new AlertDialog.Builder(this).setTitle("FormatError")
				.setMessage("Format of arrival time should be HH/MM eg:05:12")
				.setNeutralButton("ok", null).show();
	}

	/**
	 * Alert box to alert the user about wrong input format
	 * 
	 * @param v
	 *            this.view
	 */
	private void datesTimeBox(View v) {
		new AlertDialog.Builder(this)
				.setTitle("FormatError")
				.setMessage(
						"Format of dates should be MM/DD/YYYY")
				.setNeutralButton("ok", null).show();
	}
	
	
}
