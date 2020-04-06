package main.java.com.projectBackEnd.Image;

import main.java.com.projectBackEnd.Entities.News.Hibernate.NewsManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * The ImageManager class deals with low level business logic transactions regarding images. These are stored
 * on the server so no database interaction is involved
 */
public class ImageManager implements ImageManagerInterface {

	private static ImageManager imageManager;
	//Random name related variables
	final static String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";
	final java.util.Random rand = new java.util.Random();
	final Set<String> identifiers = new HashSet<String>();
	//Directory of the folder where the images are saved
	final String dir;

	/**
	 * Private singleton constructor for an ImageManager
	 */
	private ImageManager() {
		imageManager = this;
		dir = DirectoryHolder.getDir();
	}

	/**
	 * Get the image manager singleton object
	 * @return The image manager
	 */
	public static ImageManager getImageManager() {
		if (imageManager != null) return imageManager;
		else return new ImageManager();
	}

	/**
	 * Generate a random name using the lexicon
	 * @return random name
	 */
	private String randomIdentifier() {
		StringBuilder builder = new StringBuilder();
		while(builder.toString().length() == 0) {
			int length = rand.nextInt(5)+5;
			for(int i = 0; i < length; i++) {
				builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
			}
			if(identifiers.contains(builder.toString())) {
				builder = new StringBuilder();
			}
		}
		return builder.toString();
	}

	/**
	 * Save a image with bytes and its extension
	 * @param imageBytes
	 * @param extension
	 * @retunn random name
	 */
	public String saveImage(String imageBytes, String extension) {
		if (extension == null) return null;
		byte[] data = Base64.getDecoder().decode(imageBytes.getBytes(StandardCharsets.UTF_8));
		String randomName = randomIdentifier();
		String path = dir + randomName + "." + extension.toLowerCase();
		try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new File(path)))) {
			outputStream.write(data);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return (randomName + "." + extension);
	}

	/**
	 * Delete an image based on its name
	 * @param imageName The name of the image to be deleted
	 * @return Whether delete was successful
	 */
	public boolean deleteImage(String imageName)
	{
		File foundImage = getImage(imageName);
		return (foundImage == null) ? false : foundImage.delete();
	}

	/**
	 * Delete all the images
	 */
	public void deleteAll() {
		List<String> listOfImageUrls = getImageUrls();
		for (String imageUrl : listOfImageUrls) {
			deleteImage(imageUrl.substring(imageUrl.lastIndexOf("/") + 1));
		}
	}

	/**
	 * Get a file based on its name
	 * @param imageName The name of the file to be searched for
	 * @return The matching file
	 */
	public File getImage(String imageName) {
		File[] fileArray = getFileArray(dir);
		for (File file : fileArray) {
			if (file.isFile() && file.getName().equals(imageName)) return file;
		}
		return null;
	}

	/**
	 * Get a list of all the image URLs in the directory
	 * @return String list of all the Image URLs in dir.
	 */
	public List<String> getImageUrls()	{
		File[] fileArray = getFileArray(dir);
		List<String> urls = new ArrayList<String>();
		for (int i = 0; i < fileArray.length; i++) {
			if (fileArray[i].isFile()) urls.add(dir+fileArray[i].getName());
		}
		return urls;
	}

	/**
	 * Using a string directory, return an array of the files in the directory.
	 * @param directoryPath The string location of the directory to be searched
	 * @return All the files in the directory
	 */
	private static File[] getFileArray(String directoryPath) {
		File directory = new File(directoryPath);
		try {
			return directory.listFiles();
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get the output directory
	 * @return directory
	 */
	public String getDir() {
		return dir;
	}
}