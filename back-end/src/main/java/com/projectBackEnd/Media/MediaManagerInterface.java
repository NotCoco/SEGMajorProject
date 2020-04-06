package main.java.com.projectBackEnd.Media;

import java.util.List;
import java.io.File;

/**
 *  Methods used by MediaManagers for media uploading and deleting
 */
public interface MediaManagerInterface {

    public String saveMedia(String imageBytes, String extension);

    public boolean deleteMedia(String imageName);

    public List<String> getMediaUrls();

    public void deleteAll();

    public File getMedia(String imageName);

    public String getDir();

}
