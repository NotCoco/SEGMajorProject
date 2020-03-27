package test.java;

import main.java.com.projectBackEnd.Entities.News.News;
import main.java.com.projectBackEnd.Entities.News.NewsManager;
import main.java.com.projectBackEnd.Entities.News.NewsManagerInterface;
import main.java.com.projectBackEnd.HibernateUtility;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;

public class NewsManagerTest {
    
    public static ConnectionLeakUtil connectionLeakUtil = null;
    public static NewsManagerInterface newsManager = null;

    @BeforeAll
    public static void setUpDatabase() {
        HibernateUtility.setResource("testhibernate.cfg.xml");
        newsManager = NewsManager.getNewsManager();
        connectionLeakUtil = new ConnectionLeakUtil();
    }

    @AfterAll
    public static void assertNoLeaks() {
        HibernateUtility.shutdown();
        connectionLeakUtil.assertNoLeaks();
    }

    @BeforeEach
    public void setUp() {
        newsManager.deleteAll();
    }


    @Test
    public void testCreateAndSaveNews() {
        newsManager.addNews(new News(new Date(12343212L), false,
                "desc213ription1", "ti321tle1", false, "con321tent1", "slug1"));
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
        News replacementNews = new News(id, new Date(12343212L), true,
                "changedDescription", "newTitle", false, "content1", "slug9");
        newsManager.update(replacementNews);

        News newsInDB = newsManager.getByPrimaryKey(id);
        assertEquals(replacementNews.getDescription(), newsInDB.getDescription());
        assertEquals(replacementNews.getTitle(), newsInDB.getTitle());
    }


    @Test
    public void testUpdateNewsWithDupeSlug() {

        assertThrows(PersistenceException.class, () -> {
            fillDatabase();
            int id = newsManager.getAllNews().get(0).getPrimaryKey();
            News replacementNews = new News(id ,new Date(12343212L), true, "changedDescrption",
                    "newTitle", false, "content1", "slug1");
            newsManager.update(replacementNews);
        });

    }

    @Test
    public void testOrderOfNews() {
        fillDatabase();
        List<News> allNews = newsManager.getAllNews();
        assertEquals("title6", allNews.get(0).getTitle());
        assertEquals("title4", allNews.get(1).getTitle());
    }

    @Test
    public void testGetNewsBySlug() {
        fillDatabase();
        News found = newsManager.getNewsBySlug("slug2");
        assertNotNull(found);
        assertEquals("content2", found.getContent());
    }

    @Test
    public void testGetByPrimaryKey() {
        fillDatabase();
        News firstNews = newsManager.getAllNews().get(0);
        News foundNews = firstNews;
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
        newsManager.delete(newsManager.getAllNews().get(1).getPrimaryKey()); //Testing object deletion
        assertEquals( getListOfNews().size()-1, newsManager.getAllNews().size());
        newsManager.delete(newsManager.getAllNews().get(1).getPrimaryKey());
        assertEquals(getListOfNews().size()-2, newsManager.getAllNews().size());
    }
    @Test
    public void testDuplicateSlugAddition() {
        newsManager.addNews(new News(new Date(12343212L), false,
                "desc213ription1", "ti321tle1", false, "con321tent1", "slug1"));
        newsManager.addNews(new News(new Date(12343212L), false,
                "desc213ription1", "ti321tle1", false, "con321tent1", "slug1"));
        assertEquals(newsManager.getAllNews().size(), 1);
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
        News news = new News(new Date(12343212L), false, "description1", "title1",
                false, "content1", "slug123");
        int numberOfNews = newsManager.getAllNews().size();
        try {
            newsManager.delete(news.getPrimaryKey());
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
