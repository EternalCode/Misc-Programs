package access_manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LoginManager {

	private Map<String, String[]> employees; // employees by their IDs

	/**
	 * the constructure of login manager, witch open the file in given path and
	 * gather information from that file
	 * 
	 * @param dir
	 *            the path of file
	 * @param fileName
	 *            the filename under the direction
	 * @throws IOException
	 *             when the file is not founded
	 */
	public LoginManager(File dir, String fileName) throws IOException {
		this.employees = new HashMap<String, String[]>();
		// Populates the nurses or physicians list using stored data, if it
		// exists.
		File file = new File(dir, fileName);
		if (file.exists()) {
			this.populate(file.getPath(), fileName);
		} else {
			file.createNewFile();
		}
	}

	/**
	 * Populates the map of Students from the file at path filePath
	 * 
	 * @param filePath
	 *            the filepath of the data file
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	private void populate(String filePath, String fileName)
			throws FileNotFoundException {

		Scanner scanner = new Scanner(new FileInputStream(filePath));
		String[] record;

		while (scanner.hasNextLine()) {
			record = scanner.nextLine().split(",");
			String name = record[0];
			String pass = record[1];
			String identify = record[2];
			String[] info = { pass, identify };
			this.employees.put(name, info);

		}
		scanner.close();
	}

	/**
	 * Saves the employees objects to file outputStream.
	 * 
	 * @param outputStream
	 *            the output stream to write the employees data to
	 */
	public void saveToFile(FileOutputStream outputStream) {
		try {
			// write student info one per line into outputStream
			for (String n : employees.keySet()) {
				String group = n + "," + employees.get(n)[0] + ","
						+ employees.get(n)[1];
				outputStream.write((group + "\n").getBytes());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * isMatchEmployees returns true if the name and password is in the file
	 * 
	 * @param name
	 *            the name of employee
	 * @param password
	 *            the password of employee
	 * @return true if the name matches password
	 */
	public boolean isMatchEmployees(String name, String password) {
		if (this.employees.size() == 0) {
			return false;
		} else if (!this.employees.containsKey(name)) {
			return false;
		} else {
			return (this.employees.get(name)[0].matches(password));
		}
	}

	/**
	 * addEmployee put the employee's name and following information into its
	 * map
	 * 
	 * @param name
	 *            the name of employees
	 * @param userinfo
	 *            the following information about this employees including
	 *            password and identification
	 */
	public void addEmployee(String name, String[] userinfo) {
		this.employees.put(name, userinfo);
	}

	/**
	 * hasThisEmployee returns a boolean depending on if the hospital has this
	 * employee
	 * 
	 * @param name
	 *            name of employee
	 * @return boolean
	 */
	public boolean hasThisEmployee(String name) {
		return (employees.containsKey(name));
	}

	/**
	 * check if the user is a nurse or physician
	 * 
	 * @param name
	 *            name the employee's name
	 * @return true if this employee is nurse, false if this employee is
	 *         physician
	 */
	public boolean isNurse(String name) {
		String type = employees.get(name)[1];
		return (type.matches("nurse"));
	}
}