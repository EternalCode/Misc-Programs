package filemanager;



import java.io.File;
import java.io.IOException;
import hospital.Patient;

/**
 * 
 * @author Kai
 *
 */
public class Physician extends Users{

	/**
	 * inherits parent class's construtor
	 * @param dir the path of file
	 * @param fileName the file's name under the path
	 * @throws IOException if the direction doesn't exist
	 */
	public Physician(File dir, String fileName) throws IOException {
		super(dir, fileName);
	}
	
	/**
	 * let physician add the prescription for the latest vitalsign of 
	 * patient
	 * @param patient the Patient patient that ready for adding prescription
	 * @param prescription the string represents prescription for patient's vitalsign
	 */
	public void addPrescription(Patient patient, String prescription){
		//check if there aren't any patients we don't add a report
		if (!(patients.isEmpty())){
			patient.EditPrescriptionByPhysician(prescription);
		} else{
			; //do nothing
		}
	}

}
