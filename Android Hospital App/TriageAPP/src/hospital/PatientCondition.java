package hospital;

/**
 * PatientCondition class takes care of patientCondition objects Patient
 * Condition objects consist of a series of properties about the patients
 * conditions as well as some methods to manipulate those properties.
 * 
 */
public class PatientCondition {

	private String temp, bloodp, heartr, vitaltime, prescription;

	/**
	 * a constructor of patient's condition with the given order
	 * 
	 * @param vitaltime
	 *            the time of recording vital signs
	 * @param bloodp
	 *            the string represents the blood pressure
	 * @param heartr
	 *            the string represents the heart rate
	 * @param temp
	 *            the string represents the temperature
	 * @param prescription
	 *            the prescription of patient
	 */
	public PatientCondition(String vitaltime, String temp, String bloodp,
			String heartr, String prescription) {
		this.vitaltime = vitaltime;
		this.temp = temp;
		this.bloodp = bloodp;
		this.heartr = heartr;
		this.prescription = prescription;

	}

	/**
	 * toString retrieves properties of a PatientCondition object and returns a
	 * string representation of them seperated by the "$" charachter
	 * 
	 * @return A string representation of a PatientCondition object
	 */
	public String toString() {
		// return a string representation of the PatientCondition
		// seperated by "$" rather than spaces
		String condition = vitaltime + "," + temp + "," + bloodp + "," + heartr
				+ "," + prescription;
		return condition;

	}

	/**
	 * getPrescription allows outside class find the value of patient's
	 * condition
	 * 
	 * @return a string represents the prescription
	 */
	public String getPrescription() {
		// TODO Auto-generated method stub
		return prescription;
	}

	/**
	 * getVitalTime allows outside class find the value of patient's condition
	 * 
	 * @return a string represents the vitalsign recording time
	 */
	public String getVitalTime() {
		// TODO Auto-generated method stub
		return vitaltime;
	}

	/**
	 * getHeartRate allows outside class find the value of patient's condition
	 * 
	 * @return a string represents the heartRate
	 */
	public String getHeartRate() {
		// TODO Auto-generated method stub
		return heartr;
	}

	/**
	 * getBloodPressure allows outside class find the value of patient's
	 * condition
	 * 
	 * @return a string represents the blood pressure
	 */
	public String getBloodPressure() {
		// TODO Auto-generated method stub
		return bloodp;
	}

	/**
	 * getTemperature allows outside class find the value of patient's condition
	 * 
	 * @return a string represents the temperature
	 */
	public String getTemperature() {
		// TODO Auto-generated method stub
		return temp;
	}

}
