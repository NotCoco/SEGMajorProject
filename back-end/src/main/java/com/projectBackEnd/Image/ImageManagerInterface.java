package main.java.com.projectBackEnd.Image;

import java.util.List;
import java.io.File;
/**
 *  Methods used by ImageManagers for image uploading and deleting
 */

public interface ImageManagerInterface {

    public String saveImage(String imageBytes, String extension);

    public File getImage(String imageName);

    public String getDir();

    public List<String> getImageUrls();

    public boolean deleteImage(String imageName);

    public void deleteAll();

}
