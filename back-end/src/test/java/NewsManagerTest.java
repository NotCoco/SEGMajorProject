package test.java;

import main.java.com.projectBackEnd.Entities.News.News;
import main.java.com.projectBackEnd.Entities.News.NewsManager;
import main.java.com.projectBackEnd.Entities.News.NewsManagerInterface;
import main.java.com.projectBackEnd.HibernateUtility;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class NewsManagerTest {
    
    public static ConnectionLeakUtil connectionLeakUtil = null;
    public static NewsManagerInterface newsManager = null;

    @BeforeClass
    public static void setUpDatabase() {
        HibernateUtility.setResource("testhibernate.cfg.xml");
        newsManager = NewsManager.getNewsManager();
        connectionLeakUtil = new ConnectionLeakUtil();
    }

    @AfterClass
    public static void assertNoLeaks() {
        HibernateUtility.shutdown();
        connectionLeakUtil.assertNoLeaks();
    }

    @Before
    public void setUp() {
        newsManager.deleteAll();
    }


    @Test
    public void testCreateAndSaveNews() {
        newsManager.addNews(new News(new Date(12343212L), false, "desc213ription1", "ti321tle1", false, "con321tent1", "slug1"));
        assertEquals(1, newsManager.getAllNews().size());
    }

    @Test
    public void testCreateWithIllegalValues() {
        newsManager.addNews(null,true, null, null, false, null, null);
        assertEquals(0, newsManager.getAllNews().size());
    }

    @Test
    public void testFillingAndGetting() {
        fillDatabase();
        assertEquals(getListOfNews().size(), newsManager.getAllNews().size());
    }

    @Test
    public void testUpdateNews() {
        fillDatabase();
        int id = newsManager.getAllNews().get(0).getPrimaryKey();
        News replacementMed = new News(id ,new Date(12343212L), true, "changedDescrption", "newTitle", false, "content1", "slug9");
        newsManager.update(replacementMed);

        News newsInDB = newsManager.getAllNews().get(0);
        assertEquals(replacementMed.getDescription(), newsInDB.getDescription());
        assertEquals(replacementMed.getTitle(), newsInDB.getTitle());
    }

    @Test(expected = PersistenceException.class)
    public void testUpdateNewsWithDupeSlug() {
        fillDatabase();
        int id = newsManager.getAllNews().get(0).getPrimaryKey();
        News replacementMed = new News(id ,new Date(12343212L), true, "changedDescrption", "newTitle", false, "content1", "slug1");
        newsManager.update(replacementMed);
    }

    @Test
    public void testOrderOfNews() {
        fillDatabase();
        List<News> allNews = newsManager.getAllNews();
        assertEquals("title4", allNews.get(0).getTitle());
        assertEquals("title1", allNews.get(1).getTitle());
    }
    @Test
    public void testGetPinnedNews() {
        fillDatabase();
        List<News> allPinned = newsManager.getAllPinnedNews();
        for(int i = 0; i < allPinned.size(); ++i) {
            assertTrue(allPinned.get(i).isPinned());
        }
    }

    @Test
    public void testGetByPrimaryKey() {
        fillDatabase();
        News firstMed = newsManager.getAllNews().get(0);
        News foundNews = firstMed;
        int newsPK = foundNews.getPrimaryKey();
        News foundNewsFromDB = newsManager.getByPrimaryKey(newsPK);

        assertThat(foundNews, samePropertyValuesAs(foundNewsFromDB));
    }

    @Test
    public void testGetIllegalPrimaryKey() {
        assertNull(newsManager.getByPrimaryKey(-1));
    }


    @Test
    public void testDeleteAll() {
        // Delete all from empty database
        newsManager.deleteAll();
        assertEquals(0, newsManager.getAllNews().size());
        // Delete all from filled database
        fillDatabase();
        newsManager.deleteAll();
        assertEquals(0, newsManager.getAllNews().size());
    }

    @Test
    public void testDelete() {
        fillDatabase();
        newsManager.delete(newsManager.getAllNews().get(1)); //Testing object deletion
        assertEquals( getListOfNews().size()-1, newsManager.getAllNews().size());
        newsManager.delete(newsManager.getAllNews().get(1));
        assertEquals(getListOfNews().size()-2, newsManager.getAllNews().size());
    }

    @Test
    public void testDeleteByPK() {
        fillDatabase();
        newsManager.delete(newsManager.getAllNews().get(1).getPrimaryKey()); //Testing object deletion
        assertEquals(getListOfNews().size()-1, newsManager.getAllNews().size());
    }

    @Test
    public void testWithDeleteIllegalPK() {
        int numberOfNews = newsManager.getAllNews().size();
        try {
            newsManager.delete(-1);
            fail();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            assertEquals(newsManager.getAllNews().size(), numberOfNews);
            // Check that nothing has been removed
        }
    }

    @Test
    public void testDeleteNotInDBObject() {
        News news = new News(new Date(12343212L), false, "description1", "title1", false, "content1", "slug123");
        int numberOfNews = newsManager.getAllNews().size();
        try {
            newsManager.delete(news);
            fail();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            assertEquals(newsManager.getAllNews().size(), numberOfNews);
            // Check that nothing has been removed
        }
    }



    private static ArrayList<News> getListOfNews() {
        ArrayList<News> listOfNews = new ArrayList<>();

        listOfNews.add(new News(new Date(12343212L), false, "description1", "title1",
                false, "content1", "slug1"));
        listOfNews.add(new News(new Date(12343214L), false, "description2", "title2",
                false, "content2", "slug2"));
        listOfNews.add(new News(new Date(12343215L), false, "description3", "title3",
                false, "content3", "slug3"));
        listOfNews.add(new News(new Date(12343218L), true, "description4", "title4",
                false, "content4", "slug4"));
        listOfNews.add(new News(new Date(12343400L), false, "description5", "title5",
                false, "content5", "slug5"));
        listOfNews.add(new News(new Date(12343900L), false, "description6", "title6",
                true, "content6", "slug6"));
        listOfNews.add(new News(new Date(15343212L), false, "description7", "title7",
                false, "content7", "slug7"));

        return listOfNews;
    }


    private void fillDatabase() {
        ArrayList<News> listOfNews = getListOfNews();
        for (int i = 0; i<listOfNews.size(); ++i) newsManager.addNews(listOfNews.get(i));
    }
}
