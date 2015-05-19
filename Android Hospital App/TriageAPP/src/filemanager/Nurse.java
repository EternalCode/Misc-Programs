package filemanager;

import hospital.Patient;
import hospital.PatientCondition;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
//import java.util.ArrayList;
import java.util.List;

//import java.util.List;

public class Nurse extends Users {

	/**
	 * inherits the parent class's constructor
	 * 
	 * @param dir
	 *            the path of file
	 * @param fileName
	 *            filename under the path
	 * @throws IOException
	 *             if the file can't be found
	 */
	public Nurse(File dir, String fileName) throws IOException {
		super(dir, fileName);
	}

	/**
	 * addPatient adds a patient into the patients map
	 * 
	 * @param patient
	 *            a patient that needed to be added
	 */
	public void addPatient(Patient patient) {
		patients.put(patient.getName(), patient);
	}

	/**
	 * judge if the patient is in the patient map by found its name
	 * 
	 * @param name
	 *            patient's name
	 * @return true if the patient is in file
	 */
	public boolean haveThisPatient(String name) {
		return (patients.containsKey(name));
	}// for add vitalsign using

	/**
	 * setPatientCondition sets the patient's condition matching the patient's
	 * name
	 * 
	 * @param patientname
	 *            patient's name from given input
	 * @param condition
	 *            patient's condition from given para
	 */
	public void setPatientCondition(String patientname,
			PatientCondition condition) {
		Patient p = patients.get(patientname);
		p.addConditionByNurse(condition);
	}

	/**
	 * getSortListArrival return a string represents a list of patient's name
	 * with health card number in decreasing order by arrival time
	 * 
	 * @return a string represents a list of patient's name and health ID
	 */
	public String getSortListArrival() {
		String info = "";
		List<Patient> list = sortArrival();
		// to ensure exception appear
		for (int i = 0; i < list.size(); i++) {
			info += (list.get(i).getName() + " " + list.get(i).getHealthID() + "\n");
		}
		return info;
	}

	/**
	 * getSortListUrgency return a string represents a list of patients with
	 * decreasing order by urgency if the patient have no vital sign for check
	 * urgency, this patient won't be sort, so do the one have been seen doctor
	 * before
	 * 
	 * @return a string represents a list of patient's name and healthcardID
	 */
	public String getSortListUrgency() {
		String info = "";
		List<Patient> list = sortByUrgency();
		// to ensure exception appear
		for (int i = 0; i < list.size(); i++) {
			info += (list.get(i).getName() + " " + list.get(i).getHealthID() + "\n");
		}
		return info;
	}

	/**
	 * unSeenList takes the nurses's list of patients and returns a list of
	 * patients which doesn't contain the patients already seen by the doctor.
	 * Note that the original list is unmodified
	 * 
	 * @return a list of patients who haven't seen a doctor
	 */
	public List<Patient> unSeenList() {
		// create a new list to return
		List<Patient> unSeenList = new ArrayList<Patient>();
		// populate list with all patient objects
		unSeenList.addAll(this.patients.values());
		// iterate over all patients and delete all seen patients.
		for (int i = 0; i < unSeenList.size(); i++) {
			if (unSeenList.get(i).getSeenDoctorTime().matches("None")) {
				unSeenList.remove(i);
			}
		}
		// return the list with only patients yet to see a doctor
		return unSeenList;
	}

	/**
	 * sortArrival takes the nurse's map of patients and sorts it according to
	 * the arrival time of the patients
	 * 
	 * @return list of patients sorted by arrival times
	 */
	public List<Patient> sortArrival() {
		// make a new list to populate to prevent aliasing
		// note we remove the patients who have already been
		// seen by a doctor which requires us to correct
		// the aliasing of the original map of patients
		List<Patient> sorted = new ArrayList<Patient>();
		sorted.addAll(this.patients.values());
		// call helper function to return a list sorted by arrival time
		return removeSeen(quickSortArrival(sorted));
	}

	/**
	 * removeSeen takes a list of patients and returns a copy of that list with
	 * entries for patients who have already seen a doctor removed.
	 * 
	 * @param patientList
	 *            list of patient objects
	 * @return returns a list of patient objects who haven't seen a doctor
	 */
	private List<Patient> removeSeen(List<Patient> patientList) {
		// create a new list to return
		List<Patient> unSeenList = new ArrayList<Patient>();
		// populate list with all patient objects
		unSeenList.addAll(patientList);
		// iterate over all patients and delete all seen patients.
		for (int i = 0; i < unSeenList.size(); i++) {
			if (!unSeenList.get(i).getSeenDoctorTime().matches("None")) {
				unSeenList.remove(i);
			}
		}
		return unSeenList;
	}

	/**
	 * quickSortArrival takes a list of patients and sorts it depending on the
	 * arrivalTime property of the patients
	 * 
	 * @param sorted
	 *            is ironicly an unsorted list of patient objects
	 * @return returns a list of patients sorted according the arrival time
	 */
	private List<Patient> quickSortArrival(List<Patient> sorted) {
		// base case, no patients or one patient
		if (sorted.size() < 2) {
			return sorted;
			// case where list is bigger than one element
		} else {
			// pick a fat pivot
			List<Patient> pivot = new ArrayList<Patient>();
			pivot.add(sorted.get(0));
			// make a smaller than pivot and greater than pivot list
			List<Patient> small = new ArrayList<Patient>();
			List<Patient> big = new ArrayList<Patient>();
			// fill in lists with values
			for (int i = 1; i < sorted.size(); i++) {
				// if smaller than pivot put in smaller
				if (sorted.get(i).getTime() > pivot.get(0).getTime()) {
					small.add(sorted.get(i));
					// if greater add into big
				} else if (sorted.get(i).getTime() < pivot.get(0).getTime()) {
					big.add(sorted.get(i));
					// if same as pivot put in pivot
				} else {
					pivot.add(sorted.get(i));
				}
			}
			// sort the sublists
			List<Patient> start = quickSortArrival(small);
			List<Patient> end = quickSortArrival(big);
			// combine sorted sublists
			start.addAll(pivot);
			start.addAll(end);
			// return sorted list
			return start;
		}

	}

	/**
	 * sortArrival takes the nurse's map of patients and sorts it according to
	 * the urgency by checking latest recorded vital signs of the patients
	 * 
	 * @return list of patients sorted by urgency
	 */
	public List<Patient> sortByUrgency() {
		// make a new list to populate
		List<Patient> allPatients = new ArrayList<Patient>();
		allPatients.addAll(patients.values());
		for (int i = 0; i < allPatients.size(); i++) {
			if (allPatients.get(i).conditionsEmpty()) {
				allPatients.remove(i);
			}
		}
		// call helper function
		return removeSeen(quickSortUrgency(allPatients));

	}

	/**
	 * helping function to help sort by urgency
	 * 
	 * @param sorted
	 *            a list of patient that wants to be sorted
	 * @return a list of sorted patients
	 */
	private List<Patient> quickSortUrgency(List<Patient> sorted) {
		// base case, no patients or one patient
		if (sorted.size() < 2) {
			return sorted;
			// case where list is bigger than one element
		} else {
			// pick a fat pivot
			List<Patient> pivot = new ArrayList<Patient>();
			pivot.add(sorted.get(0));
			// make a smaller than pivot and greater than pivot list
			List<Patient> small = new ArrayList<Patient>();
			List<Patient> big = new ArrayList<Patient>();
			// fill in lists with values
			for (int i = 1; i < sorted.size(); i++) {
				// if smaller than pivot put in smaller
				if (sorted.get(i).getUrgency() < pivot.get(0).getUrgency()) {
					small.add(sorted.get(i));
					// if greater add into big
				} else if (sorted.get(i).getUrgency() > pivot.get(0)
						.getUrgency()) {
					big.add(sorted.get(i));
					// if same as pivot put in pivot
				} else {
					pivot.add(sorted.get(i));
				}
			}
			// sort the sublists
			List<Patient> start = quickSortUrgency(small);
			List<Patient> end = quickSortUrgency(big);
			// combine sorted sublists
			start.addAll(pivot);
			start.addAll(end);
			// return sorted list
			return start;
		}
	}

}