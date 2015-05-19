package filemanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import hospital.Patient;

public abstract class Users {

	protected Map<String, Patient> patients; // patients by their IDs

	/**
	 * the constructure of the users which have two subclasses, it find the file
	 * in given direction path and gather information if the file doesnot exist
	 * create a new one
	 * 
	 * @param dir
	 *            the path of file
	 * @param fileName
	 *            the filename under the path
	 * @throws IOException
	 *             if the file is not found
	 */
	public Users(File dir, String fileName) throws IOException {
		this.patients = new HashMap<String, Patient>();

		// Populates the student list using stored data, if it exists.
		File file = new File(dir, fileName);
		if (file.exists()) {
			this.populate(file.getPath());
		} else {
			file.createNewFile();
		}
	}

	/**
	 * Populates the map of patients from the file at path filePath
	 * 
	 * @param filePath
	 *            the filepath of the data file
	 * @throws FileNotFoundException
	 */
	private void populate(String filePath) throws FileNotFoundException {

		Scanner scanner = new Scanner(new FileInputStream(filePath));
		String[] record;

		while (scanner.hasNextLine()) {
			record = scanner.nextLine().split(",");
			String name = record[0];
			String dob = record[1];
			String HID = record[2];
			String arrivetime = record[3];
			String seendoctordatetime = record[4];
			Patient patient = new Patient(name, dob, HID, arrivetime,
					seendoctordatetime);

			if (record.length > 5) {
				String conditions = record[5];
				String[] vitalsigns = conditions.split(";");
				patient.addConditions(vitalsigns);
			}

			patients.put(name, patient);

		}
		scanner.close();
	}

	/**
	 * Saves the Patients objects to file outputStream.
	 * 
	 * @param outputStream
	 *            the output stream to write the Patient data to
	 */
	public void saveToFile(FileOutputStream outputStream) {
		try {
			// write student info one per line into outputStream
			for (Patient p : patients.values()) {
				String conditions = p.getAllConditionsToSave();
				// using for put the latest vitalsign data in the front
				// "," should not exist in conditions

				outputStream.write((p.toString() + "," + conditions + "\n")
						.getBytes());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * HavePatient method returns true if the employees have this patient in the
	 * file by checking the patient's health card number
	 * 
	 * @param HID
	 *            the health card number of patient
	 * @return true if the employees have this patient
	 */
	public boolean HavePatient(String HID) {
		// record in a list values in a patient map
		List<Patient> pl = new ArrayList<Patient>();
		pl.addAll(this.patients.values());
		// iterate over the patient list
		for (int i = 0; i < pl.size(); i++) {
			if (pl.get(i).getHealthID().matches(HID)) {
				return true;
			}
		}
		// there are no patients with said ID
		return false;
	}

	/**
	 * getByHealthCard return a patient found by given health card number
	 * 
	 * @param hID
	 *            the patient's health card number
	 * @return a patient with given health card number
	 */
	public Patient getByHealthCard(String hID) {
		// record in a list values in a patient map
		List<Patient> patientslist = new ArrayList<Patient>();
		patientslist.addAll(this.patients.values());
		// iterate over the patient list
		for (int i = 0; i < patientslist.size(); i++) {
			if (patientslist.get(i).getHealthID().matches(hID)) {
				return patientslist.get(i);
			}
		}
		return null;
	}

}
