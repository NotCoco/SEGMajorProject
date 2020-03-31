package main.java.com.projectBackEnd.Image;

import java.io.File;

public class DirectoryHolder {

	private static String dir = System.getProperty("user.dir")+"\\src\\main\\resources\\images\\";
	private static DirectoryHolder holder;
	private DirectoryHolder(){
		holder = this;
	}
	public static DirectoryHolder getDirectoryHolder() {
		if (holder == null) return new DirectoryHolder();
		else return holder;
	}
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
	public void setDefaultDir(){dir = System.getProperty("user.dir")+"\\src\\main\\resources\\images\\";}
	public static String getDir() {
		return dir;
	}
}