package main.java.com.projectBackEnd.Image;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * ImageManager class is used to handle image related executions:
 * - Upload images
 * - Delete images
 * - Get images
 */
public class ImageManager {

	//Random name related variables
	final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";
	final java.util.Random rand = new java.util.Random();
	final Set<String> identifiers = new HashSet<String>();
	//Directory of the folder where the images are saved
	final String dir;
	//Constructor
	public ImageManager() {
		dir = DirectoryHolder.getDir();
	}
	/**
	 * Return a random name
	 * @retunn random name
	 */
	public String randomIdentifier() {
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

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
					+ Character.digit(s.charAt(i+1), 16));
		}
		return data;
	}
	/**
	 * Save a image with bytes and its extension
	 * @param imageBytes
	 * @param extension
	 * @retunn random name
	 */
	public String saveImage(String imageBytes, String extension)
	{
		if(extension == null){ return "Failed"; }
		extension = extension.toLowerCase();
		//convert base64 string to binary data
		byte[] data = hexStringToByteArray("e04fd020ea3a6910a2d808002b30309d");
		try {
			data = Base64.getDecoder().decode(imageBytes.getBytes(StandardCharsets.UTF_8));
		}catch (IllegalArgumentException e){
		}
		String randomName = randomIdentifier();
		String path = dir + randomName + "." + extension;
		System.out.println(path);
		File file = new File(path);
		try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
			outputStream.write(data);
		} catch (IOException e) {
			e.printStackTrace();
			return "Failed";
		}
		return (randomName + "." + extension);
	}

	/**
	 * Delete image
	 * @param imageName
	 * @retunn boolean value to check if its deleted
	 */
	public boolean deleteImage(String imageName)
	{
		File folder = new File(dir);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++)
		{
			System.out.println(dir+listOfFiles[i].getName());
			if (listOfFiles[i].isFile() && listOfFiles[i].getName().equals(imageName)) {
				File file = new File(dir+listOfFiles[i].getName());
				return file.delete();
			}
		}
		return false;
	}

	/**
	 * Delete all the images
	 */
	public void deleteAll()
	{
		List<String> listOfImageUrls = getImageUrls();
		if(!listOfImageUrls.isEmpty()){
			for (String imageUrl : listOfImageUrls)
			{
				deleteImage(imageUrl.substring(imageUrl.lastIndexOf("/") + 1));
			}
		}
	}

	/**
	 * Get the image by image name
	 * @param imageName
	 * @return
	 */
	public File getImage(String imageName){
		File folder = new File(dir);
		File targetFile = null;
		File[] listOfFiles = folder.listFiles();
		for (File f: listOfFiles) {
			if(f.getName().equals(imageName)){
				targetFile = f;
			}
		}
		return targetFile;
	}

	public  List<String> getImageUrls()
	{
		File folder = new File(dir);
		File[] listOfFiles = null;
		try{listOfFiles = folder.listFiles();}
		catch (Exception e){}
		List<String> urls = new ArrayList<String>();
		for (int i = 0; i < listOfFiles.length; i++)
		{
			if (listOfFiles[i].isFile()) {
				urls.add(dir+listOfFiles[i].getName());
			}
		}
		return urls;
	}

	public String getDir()
	{
		return dir;
	}
}