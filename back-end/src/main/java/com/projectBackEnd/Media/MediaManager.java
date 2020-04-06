package main.java.com.projectBackEnd.Media;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * The MediaManager class deals with low level business logic transactions regarding medias. These are stored
 * on the server so no database interaction is involved.
 */
public class MediaManager implements MediaManagerInterface {

	private static MediaManager mediaManager;

	//Random name related variables
	final static String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";
	final java.util.Random rand = new java.util.Random();
	final Set<String> identifiers = new HashSet<String>();

	//Directory of the folder where the medias are saved
	final String dir;

	/**
	 * Private singleton constructor for an MediaManager
	 */
	private MediaManager() {
		mediaManager = this;
		dir = DirectoryHolder.getDir();
	}

	/**
	 * Get the media manager singleton object
	 * @return media manager
	 */
	public static MediaManager getMediaManager() {
		if (mediaManager != null) return mediaManager;
		else return new MediaManager();
	}

	/**
	 * Generate a random name using the lexicon
	 * @return generated name
	 */
	private String randomIdentifier() {

		StringBuilder builder = new StringBuilder();
		while(builder.toString().length() == 0) {

			int length = rand.nextInt(5)+5;
			for(int i = 0; i < length; i++) {
				builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
			}

			if(identifiers.contains(builder.toString())) builder = new StringBuilder();
		}
		return builder.toString();
	}

	/**
	 * Save a media with bytes and its extension
	 * @param mediaBytes	media as binary file
	 * @param extension		type of the media
	 * @retunn random name
	 */
	public String saveMedia(String mediaBytes, String extension) {

		if (extension == null) return null;
		byte[] data = Base64.getDecoder().decode(mediaBytes.getBytes(StandardCharsets.UTF_8));
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
	 * Delete an media based on its name
	 * @param mediaName The name of the media to be deleted
	 * @return Whether delete was successful
	 */
	public boolean deleteMedia(String mediaName) {
		File foundMedia = getMedia(mediaName);
		return (foundMedia == null) ? false : foundMedia.delete();
	}

	/**
	 * Delete all the medias in the database
	 */
	public void deleteAll() {
		List<String> listOfMediaUrls = getMediaUrls();
		for (String mediaUrl : listOfMediaUrls) {
			deleteMedia(mediaUrl.substring(mediaUrl.lastIndexOf("/") + 1));
		}
	}

	/**
	 * Get a file based on its name
	 * @param mediaName The name of the file to be searched for
	 * @return The matching file
	 */
	public File getMedia(String mediaName) {
		File[] fileArray = getFileArray(dir);
		for (File file : fileArray) {
			if (file.isFile() && file.getName().equals(mediaName)) return file;
		}
		return null;
	}

	/**
	 * Get a list of all the media URLs in the directory
	 * @return String list of all the Media URLs in dir.
	 */
	public List<String> getMediaUrls()	{
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