package main.java.com.projectBackEnd.Image;


public class DirectoryHolder {

	private static String dir = System.getProperty("user.dir")+"\\back-end\\src\\main\\resources\\images\\";
	private static DirectoryHolder holder;
	private DirectoryHolder(){
		holder = this;
	}
	public static DirectoryHolder getDirectoryHolder() {
		if (holder == null) return new DirectoryHolder();
		else return holder;
	}
	public void setDir(String newDir) {
		this.dir = newDir;
	}
	public static String getDir() {
		return dir;
	}
}