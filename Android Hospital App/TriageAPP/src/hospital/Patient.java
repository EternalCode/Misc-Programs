package hospital;

//import needed builtins
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Patient class handles various patient object needs and has methods for
 * patient condition manipulation
 * 
 * @author group_0326
 * 
 */
public class Patient {

	// define class instance variables
	private String name, dob; // name and date of birth properties of a patient
								// are strings
	private List<String> conditions; // conditions is a list of PatientCondition
										// objects
	private String temp, bloodp, heartr, prescription, vitaltime, hID, arrival,
			seendoctordatestime;

	// temperature, bloodpressure, heartrate, symptoms, vitals recorded time are
	// all recorded as strings in this phase

	/**
	 * Patient constructor creates a patient object and assigns it's instance
	 * variables according to the parameters passed into the constructor
	 * 
	 * @param name
	 *            string name of patient
	 * @param dob
	 *            string date of birth of patient
	 * @param hID
	 *            health card number of patient
	 * @param arrivetime
	 *            arrival time of patient
	 */
	public Patient(String name, String dob, String hID, String arrivetime,
			String datestime) {
		// sets properties in accordance to paramaters
		this.name = name;
		this.dob = dob;
		this.conditions = new ArrayList<String>();
		this.hID = hID;
		this.arrival = arrivetime;
		this.seendoctordatestime = datestime;
	}

	/**
	 * getSeenDoctorTime return a string represents the time that patient saw a
	 * doctor
	 * 
	 * @return a string represents the time when patient saw a doctor
	 */
	public String getSeenDoctorTime() {
		return seendoctordatestime;
	}

	/**
	 * getName is a getter for the patient's name
	 * 
	 * @return string patient's name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * getTime return a int represents the time with no ":" between it, for
	 * comparing later
	 * 
	 * @return a int represent a time with no ":" between hour and minutes
	 */
	public int getTime() {
		String newtype = arrival.replaceAll("[:]", "");
		int time = Integer.parseInt(newtype);
		return time;
	}

	/**
	 * getHealthID let outside class this method for get patient's id
	 * 
	 * @return a string represents the patient's health card number
	 */
	public String getHealthID() {
		return hID;
	}

	/**
	 * toString returns a string representation of a patient object it includes
	 * the name, date of birth, healthcard number and arrival time
	 * 
	 * @return returns a string representation of a patient object
	 */
	public String toString() {
		String info = name + "," + dob + "," + hID + "," + arrival + ","
				+ seendoctordatestime;
		return info;

	}

	/**
	 * showInfo collects all required information that patient have for
	 * displaying in the layout
	 * 
	 * @return a string represents all information the patient have in readable
	 *         format
	 */
	public String showInfo() {
		String conditionstring = "";
		for (int i = 0; i < conditions.size(); i++) {
			switch ((i + 1) % 5) {
			case 3:
				conditionstring += "	Blood Presure: " + conditions.get(i) + " ";
				break;

			case 4:
				conditionstring += "	Heart Rate: " + conditions.get(i) + " ";
				break;

			case 1:

				conditionstring += "At the time "
						+ conditions.get(i) + " vital signs are" + ": \n";
				break;

			case 2:

				conditionstring += "Temperture: " + conditions.get(i) + " ";
				break;
			case 0:
				conditionstring += "	Prescription: " + conditions.get(i)
						+ ". \n" + " \n";
				break;

			}

		}
		// rewrite the tostring as a readable way
		String newinfo = "name: " + name + ", \n" + "Date of birth: " + dob
				+ ", \n" + "Health Card Number: " + hID + ", \n"
				+ "Arrival Time: " + arrival + ", \n"
				+ "Time of seen a doctor: " + seendoctordatestime;
		String display = newinfo + ", \n" + conditionstring;
		return display;
	}

	/**
	 * make the latestcondition to be the new one and add each item by
	 * descreasing order to the front of String array in order to be recorded as
	 * correct order
	 * 
	 * @param c
	 *            a patientcondition from nurse
	 */
	public void addConditionByNurse(PatientCondition c) {
		// add to condition list the following condition properties
		conditions.add(0, c.getPrescription());
		conditions.add(0, c.getHeartRate());
		conditions.add(0, c.getBloodPressure());
		conditions.add(0, c.getTemperature());
		conditions.add(0, c.getVitalTime());

	}

	/**
	 * getAllConditionsToSave return a string of all conditions data and ready
	 * to be saved
	 * 
	 * @return a string of all conditions data with space between them
	 */
	public String getAllConditionsToSave() {
		// generate a string to represent all the conditions in the condition
		// property of a patient
		String result = "";
		for (int i = 0; i < conditions.size(); i++) {
			result += conditions.get(i) + ";";
		}
		// return the generated string
		return result;
	}

	/**
	 * addConditions When get the Vitalsigns from populate in Nurse, this method
	 * will be called and put all collected string array into conditions list
	 * and record the latest data of conditions
	 * 
	 * @param vitalsigns
	 */
	public void addConditions(String[] vitalsigns) {
		for (int i = 0; i < vitalsigns.length; i++) {
			conditions.add(vitalsigns[i]);
		}
		/*
		 * //creates a condition object for the patient temp = vitalsigns[0];
		 * bloodp = vitalsigns[1]; heartr = vitalsigns[2]; vitaltime =
		 * vitalsigns [3]; prescription = vitalsigns [4]; PatientCondition c =
		 * new PatientCondition(temp, bloodp, heartr, vitaltime, prescription);
		 * //defines this condition to be the latest condition
		 * this.addLatestCondition(c);
		 */
	}

	/**
	 * conditionsEmpty will return true if the patient have no vital signs
	 * 
	 * @return true if the patient have no vital signs
	 */
	public boolean conditionsEmpty() {
		if (conditions.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * getLatestCondition will collect the first 5 index value from condition
	 * list which is the latest condition recorded by the nurse because of the
	 * decreasing recorded
	 * 
	 * @return the last recorded patient condition
	 */
	public PatientCondition getLatestCondition() {
		vitaltime = conditions.get(0);
		temp = conditions.get(1);
		bloodp = conditions.get(2);
		heartr = conditions.get(3);
		prescription = conditions.get(4);
		
		PatientCondition c = new PatientCondition(vitaltime, temp, bloodp,
				heartr, prescription);
		// defines this condition to be the latest condition
		return c;
	}

	/**
	 * EditPrescriptionByPhysician offer the physician to edit the prescription
	 * from None to some prescription
	 * 
	 * @param prescription
	 *            the string represents the prescription
	 */
	public void EditPrescriptionByPhysician(String prescription) {
		conditions.set(4, prescription);
	}

	/**
	 * getUrgency compare the double of conditions with the hospitial policy to
	 * caculate the points for judge the urgency level
	 * 
	 * @return the int represents the urgency level
	 */
	public int getUrgency() {
		// calculate and return the urgency of the patient, based on most recent
		// condition
		int urgency = 0;
		if (Double.parseDouble(this.getLatestCondition().getBloodPressure()) >= 90) {
			urgency += 1;
		}
		if ((Double.parseDouble(this.getLatestCondition().getHeartRate()) >= 100)
				| (Double.parseDouble(this.getLatestCondition().getHeartRate())) <= 50) {
			urgency += 1;
		}
		if (Double.parseDouble(this.getLatestCondition().getTemperature()) >= 39) {
			urgency += 1;
		}
		if (this.getAge() <= 2) {
			urgency += 1;
		}
		return urgency;

	}

	/**
	 * a help function for helping find calculate the patient's age from a
	 * string of date of birth
	 * 
	 * @return int represents the age of the patient
	 */
	private int getAge() {
		// we calculate age from Date of birth
		// make a date object for easy date calculations
		Date date = new Date();
		// split string and put into a string list
		List<String> dOb = new ArrayList<String>(Arrays.asList(dob.split("/")));
		// calculate years passed
		int year = ((date.getYear() + 1900) - (Integer.parseInt(dOb.get(2))));
		int month = ((date.getMonth() + 1) - (Integer.parseInt(dOb.get(0))));
		int day = ((date.getDate()) - (Integer.parseInt(dOb.get(1))));
		if (day < 0 && month <= 0) {
			year -= 1;
		} else if (month < 0) {
			year -= 1;
		}

		// return years passed
		return year;
	}

	/**
	 * editSeenDoctorByNurse allows the nurse to change the patient's seen
	 * doctor status
	 * 
	 * @param datestime
	 *            the string represents the date and time that patient saw a
	 *            doctor if it is none the patient haven't seen a doctor
	 */
	public void editSeenDoctorByNurse(String datestime) {
		this.seendoctordatestime = datestime;
	}

}