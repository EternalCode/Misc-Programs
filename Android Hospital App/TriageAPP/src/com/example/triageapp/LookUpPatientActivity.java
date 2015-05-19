package com.example.triageapp;

import hospital.Patient;

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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;

public class LookUpPatientActivity extends Activity implements
		OnCheckedChangeListener {
	public static String FILENAME = "Patient.txt";
	private TextView display, text2;
	private Patient patient;
	private Nurse nurse;
	private RadioGroup radiogroup;
	private RadioButton seendoctor, notseendoctor;
	private ScrollView scrolllayout;
	private String seendoctordate, seendoctortime, timedates;

	/**
	 * A constructor of layout which is called when the activity starts
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_look_up_patient);

		display = (TextView) findViewById(R.id.l_DisplayPinfor);
		text2 = (TextView) findViewById(R.id.l_textView1);

		setradiogroup();
		try {
			nurse = new Nurse(this.getApplicationContext().getFilesDir(),
					FILENAME);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * A method that uses to set the radio buttons in a group and make the click
	 * to be unique
	 */
	private void setradiogroup() {
		radiogroup = (RadioGroup) findViewById(R.id.l_radioGroup1);
		seendoctor = (RadioButton) findViewById(R.id.l_seendoctor);
		notseendoctor = (RadioButton) findViewById(R.id.l_notseendoctor);
		scrolllayout = (ScrollView) findViewById(R.id.l_scrolllayout);

		// accept the button to be clicked, but only one button should be
		// clicked
		radiogroup.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		switch (arg0.getCheckedRadioButtonId()) {
		// when the seen doctor button is clicked, show the rest information
		case R.id.l_seendoctor:
			scrolllayout.setVisibility(View.VISIBLE);
			break;
		// when the not seen doctor button is clicked, do nothing
		case R.id.l_notseendoctor:
			scrolllayout.setVisibility(View.INVISIBLE);
			patient.editSeenDoctorByNurse("None");
			timedates = "None";
			patient.editSeenDoctorByNurse(timedates);
			
			FileOutputStream fop = null;
			try {
				fop = new FileOutputStream(new File(this.getApplicationContext()
						.getFilesDir(), FILENAME));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			nurse.saveToFile(fop);
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.look_up_patient, menu);
		return true;
	}

	/**
	 * A help method to helping constructor set the patient's information into
	 * the TextView
	 * 
	 * @param v
	 *            this.view
	 */
	public void LookupInfoPatient(View v) {
		EditText HealthCardNum = (EditText) findViewById(R.id.l_HID);
		String healthcard_ID = HealthCardNum.getText().toString();

		// if the input is empty raise the alert box
		if (healthcard_ID.length() == 0) {
			MissHCNBox(v);
		} else if (!nurse.HavePatient(healthcard_ID)) {
			// if the patient's healthcard number does not exist raise the alert
			// box
			PatientNoExist(v);
		} else {
			patient = nurse.getByHealthCard(healthcard_ID);
			// match and find the patient then ask patient to get information
			String readytodisplay = patient.showInfo();
			display.setText(readytodisplay);
			text2.setVisibility(View.VISIBLE);
			radiogroup.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * when user edit information about new seen doctor time and dates this
	 * method will be called to edit the seen doctor time
	 * 
	 * @param v
	 *            this.view
	 */
	public void editSeenDoctor(View v) {

		FileOutputStream fop = null;
		try {
			fop = new FileOutputStream(new File(this.getApplicationContext()
					.getFilesDir(), FILENAME));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (seendoctor.isChecked()) {
			EditText seendate = (EditText) findViewById(R.id.l_seendoctordate);
			seendoctordate = seendate.getText().toString();

			EditText seentime = (EditText) findViewById(R.id.l_seendoctortime);
			seendoctortime = seentime.getText().toString();

			if (!datesCheck(seendoctordate)) {
				datesBox(v);
			}
			else if(!timeCheck(seendoctortime)){
				timeBox(v);
			}
			// group the dates and time together convenient to be saved
			else {
				timedates = seendoctordate + " " + seendoctortime;
				patient.editSeenDoctorByNurse(timedates);
				nurse.saveToFile(fop);
				successfulBox(v);

			}
		} else {
			RadioButtonEmpty(v);
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

	// ==================This line is for dividing methods by different
	// works================================
	// redirect buttons
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

	/**
	 * BackToMenu is a method that is called which directs the user into the
	 * main menu activity
	 * 
	 * @param v
	 *            this.view
	 */
	public void backToMenu(View v) {
		// direct to main menu
		Intent intent = new Intent(this, MenuActivity.class);
		startActivity(intent);
	}

	// ==================This line is for dividing methods by different
	// works================================
	// Helping alert boxes

	/**
	 * PatientNoExistis a method that gives an alert telling the user that the
	 * specified patient does not exist
	 * 
	 * @param v
	 *            this.view
	 */
	private void PatientNoExist(View v) {
		// create an alert box with specified properties
		new AlertDialog.Builder(this).setTitle("Error")
				.setMessage("Sorry, This Patient doesn't exist")
				.setNeutralButton("ok", null).show();
	}

	/**
	 * Raise MissHCNBox if the input EditText is empty
	 * 
	 * @param v
	 *            this.view
	 */
	private void MissHCNBox(View v) {
		// create an alert box with specified properties
		new AlertDialog.Builder(this)
				.setTitle("Error")
				.setMessage(
						"Sorry, please enter a health card number of a patient")
				.setNeutralButton("ok", null).show();
	}

	/**
	 * Alert box to alert the user about wrong input format
	 * 
	 * @param v
	 *            this.view
	 */
	private void datesBox(View v) {
		new AlertDialog.Builder(this)
				.setTitle("FormatError")
				.setMessage(
						"Format of dates if MM/DD/YYYY and no invaid date input allowed")
				.setNeutralButton("ok", null).show();
	}
	
	/**
	 * Alert box to alert the user about wrong input format
	 * 
	 * @param v
	 *            this.view
	 */
	private void timeBox(View v) {
		new AlertDialog.Builder(this)
				.setTitle("FormatError")
				.setMessage(
						"Format of  time, should be HH/MM, no invaid time input allowed")
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
	 * successfulBox is a method which create a pop-up/alert/prompt box and
	 * tells the user that they have successfully registered a patient
	 * 
	 * @param v
	 *            this.view
	 */
	public void successfulBox(View v) {
		new AlertDialog.Builder(this).setTitle("successful")
				.setMessage("Seen Doctor is set successfuly")
				.setNeutralButton("ok", null).show();
	}
}
