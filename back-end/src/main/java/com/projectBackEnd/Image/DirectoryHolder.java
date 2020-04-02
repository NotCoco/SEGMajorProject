package main.java.com.projectBackEnd.Image;

import java.io.File;

/**
 * This class is responsible for generating/providing user-specific directory information to allow
 * for image-access operations which require dynamic accessing of specific directories
 */
public class DirectoryHolder {

	// The main directory which will be used
	private static String dir = System.getProperty("user.dir")+"/src/main/resources/images/";
	// Reference to self
	private static DirectoryHolder holder;
	/**
	 * Private constructor implementing singleton design pattern
	 */
	private DirectoryHolder(){
		holder = this;
	}

	/**
	 * Retrieves the directory holder
	 * @return The directory holder
	 */
	public static DirectoryHolder getDirectoryHolder() {
		if (holder == null) return new DirectoryHolder();
		else return holder;
	}

	/**
	 * Sets a new value to the directory attribute, effectively changing the access location
	 * If no directory exists, one is created
	 * @param newDir The new directory to be set
	 */
	public void setDir(String newDir) {
		File locationOfImageStorage = new File(newDir);
		if (!locationOfImageStorage.exists()) {
			if (locationOfImageStorage.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}
		this.dir = newDir;
	}

	/**
	 * Sets the directory back to it's default user-specific setting
	 */
	public void setDefaultDir(){dir = System.getProperty("user.dir")+"/src/main/resources/images/";}
	/**
	 * Retrieves the directory attribute
	 * @return The directory
	 */
	public static String getDir() {
		return dir;
	}
}