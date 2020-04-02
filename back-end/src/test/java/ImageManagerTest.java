package test.java;


import main.java.com.projectBackEnd.Image.DirectoryHolder;
import main.java.com.projectBackEnd.Image.ImageManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.*;


public class ImageManagerTest {

    private static ImageManager imageManager;

    public ImageManagerTest(){ imageManager = ImageManager.getImageManager(); }

    @BeforeAll
    public static void setUpBefore() {
        DirectoryHolder.getDirectoryHolder().setDir(System.getProperty("user.dir")+"/src/test/resources/TestImages/");
    }

    @BeforeEach
    public void setUp() { imageManager.deleteAll(); }

    @AfterAll
    public static void cleanUp() {
        imageManager.deleteAll();
        DirectoryHolder.getDirectoryHolder().setDefaultDir();
    }

    @Test
    public void testSaveImage() {
        String imageBytes = readLineByLine("ImageBytes.txt");
        String randomName = imageManager.saveImage(imageBytes, "png");
        List<String> urls = imageManager.getImageUrls();
        boolean check = urls.contains(imageManager.getDir()+randomName);
        assertTrue(check);
    }

    @Test
    public void testDeleteImage() {
        String imageBytes = readLineByLine("ImageBytes.txt");
        String randomName = imageManager.saveImage(imageBytes, "png");
        List<String> urls = imageManager.getImageUrls();
        boolean checkSave = urls.contains(imageManager.getDir()+randomName);
        imageManager.deleteImage(randomName);
        urls = imageManager.getImageUrls();
        boolean checkDelete = true;
        for(String url : urls)
        {
            if(url.equals(imageManager.getDir()+randomName)){
                checkDelete = false;
            }
        }
        boolean check = (checkDelete)&&checkSave;
        assertTrue(check);
        assertEquals(0, imageManager.getImageUrls().size());
    }

    @Test
    public void testSaveAndDeleteAll() {
        String imageBytes = readLineByLine("ImageBytes.txt");
        int counter = 10;
        List<String> generatedUrls = new ArrayList<>();
        while(counter!=0)
        {
            generatedUrls.add(imageManager.getDir()+imageManager.saveImage(imageBytes, "png"));
            counter--;
        }
        List<String> urls = imageManager.getImageUrls();
        assertEquals(10, urls.size());
        Collections.sort(urls);
        Collections.sort(generatedUrls);
        boolean saveCheck = (urls).equals(generatedUrls);
        assertTrue(saveCheck);

        imageManager.deleteAll();
        assertEquals(0, imageManager.getImageUrls().size());
    }

    private static String readLineByLine(String filePath)
    {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8))
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }
}
