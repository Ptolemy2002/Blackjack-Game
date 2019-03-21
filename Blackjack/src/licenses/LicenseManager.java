package licenses;

import java.util.ArrayList;
import java.util.HashMap;

import main.Tools;

public class LicenseManager {
	public static String HOME_PATH = Tools.Variables.getAppdata() + "\\Ptolemy's Code";
	private static final ArrayList<String> licenses = new ArrayList<>();
	private static final HashMap<String, String> licensePaths = new HashMap<>();

	/**
	 * Add a license to this project. Will automatically update licenses.
	 * 
	 * @param name      The name of the jar with the license
	 * @param path      The path of the license file inside the jar
	 * @param mainClass The class to use as the main class for the jar.
	 */
	public static void addLicense(String name, String path, Class<?> mainClass) {
		licenses.add(name);
		licensePaths.put(name, path);
		updateLicenses(mainClass);
	}

	/**
	 * Add a license to this project.
	 * 
	 * @param name The name of the jar with the license
	 * @param path The path of the license file inside the jar
	 */
	public static void addLicense(String name, String path) {
		licenses.add(name);
		licensePaths.put(name, path);
	}

	/**
	 * Get a list of licenses that this project has.
	 * 
	 * @return the list of licenses
	 */
	public static ArrayList<String> getLicenses() {
		return licenses;
	}

	/**
	 * Get the path in the jar to a specific license.
	 * 
	 * @param license the name of the license
	 * @return the path in the jar to a specific license.
	 */
	public static String getPath(String license) {
		return licensePaths.get(license);
	}

	/**
	 * Get the path in the file system to a specific license.
	 * 
	 * @param license the name of the license
	 * @return the path in the file system to a specific license.
	 */
	public static String getFileSystemPath(String license) {
		return HOME_PATH + "\\licenses\\" + license + ".txt";
	}

	/**
	 * Set the home path. This path will be used to store licenses.
	 * 
	 * @param homePath
	 */
	public static void setHomePath(String homePath) {
		HOME_PATH = homePath;
	}

	/**
	 * Update the licenses in the file system. Will be called whenever a new license
	 * is created.
	 */
	public static void updateLicenses(Class<?> mainClass) {
		for (String i : getLicenses()) {
			Tools.Files.writeToFile(HOME_PATH + "\\licenses\\" + i + ".txt",
					Tools.Files.getResource(licensePaths.get(i), mainClass));
		}
	}
}
