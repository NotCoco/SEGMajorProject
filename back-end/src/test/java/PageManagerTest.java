package test.java;

import main.java.com.projectBackEnd.Entities.Page.Hibernate.Page;
import main.java.com.projectBackEnd.Entities.Page.Hibernate.PageManager;
import main.java.com.projectBackEnd.Entities.Page.Hibernate.PageManagerInterface;
import main.java.com.projectBackEnd.Entities.Site.Hibernate.Site;
import main.java.com.projectBackEnd.Entities.Site.Hibernate.SiteManager;
import main.java.com.projectBackEnd.Entities.Site.Hibernate.SiteManagerInterface;
import main.java.com.projectBackEnd.HibernateUtility;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;

/**
 * Test class to extensively unit test interactions between software and the Pages table
 */
public class PageManagerTest {

    private static ConnectionLeakUtil connectionLeakUtil = null;
    private static PageManagerInterface pageManager = null;
    private static SiteManagerInterface siteManager = null;
    private static Site testSiteA = null;
    private static Site testSiteB = null;

    /**
     * Prior to running, databaste information location is set and siteManagers and pageManagers are intialised
     *
     */
    @BeforeAll
    public static void setUpDatabase() {
        HibernateUtility.setResource("testhibernate.cfg.xml");
        pageManager = PageManager.getPageManager();
        assignSites();
        connectionLeakUtil = new ConnectionLeakUtil();
    }

    /**
     * Pages require an existing site foreign key to be created so we'll create some sites for use.
     */
    private static void assignSites() {
        siteManager = SiteManager.getSiteManager();
        siteManager.addSite(new Site("Disease1", "name"));
        siteManager.addSite(new Site("Disease2", "name2"));
        testSiteA = siteManager.getSiteBySlug("Disease1");
        testSiteB = siteManager.getSiteBySlug("Disease2");
    }

    /**
     * After the tests, the factory is shut down and we can see if any connections were leaked with the Database.
     * The created sites are cleansed too.
     */
    @AfterAll
    public static void assertNoLeaks() {
        siteManager.deleteAll();
        HibernateUtility.shutdown();
        connectionLeakUtil.assertNoLeaks();
    }

    /**
     * Prior to each test, we delete all the existing pages.
     */
    @BeforeEach
    public void setUp() {
        pageManager.deleteAll();
    }

//======================================================================================================================
    //Testing Page Creation constructors
    @Test
    public void testPageCopy() {
        Page page1 = new Page(testSiteA.getSlug(), "sameSlug", 1, "TitleA", "ContentA");
        Page page2 = new Page(testSiteA.getSlug(), "Slug", 5, "newTitle", "newContent");
        page1.copy(page2);
        assertEquals(page2.getSlug(), page1.getSlug());
        assertEquals(page2.getTitle(), page1.getTitle());
        assertEquals(page2.getIndex(), page1.getIndex());
        assertEquals(page2.getContent(), page1.getContent());
    }


    @Test
    public void testAddRegularPagesKey() {
        //Page(Site site, String slug, Integer index, String title, String content) {
        Page page1 = new Page(testSiteA.getSlug(), "sameSlug", 1, "TitleA", "ContentA");
        Page page2 = new Page(testSiteB.getSlug(), "sameSlug", 1, "TitleB", "ContentB");
        pageManager.addPage(page1);
        pageManager.addPage(page2);

        assertEquals(2, pageManager.getAllPages().size());
    }



    @Test
    public void testViolateDuplicateCompositeKey() {
        Page page1 = new Page(testSiteA.getSlug(), "sameSlug", 1, "TitleA", "ContentA");
        Page page2 = new Page(testSiteA.getSlug(), "sameSlug", 1, "TitleB", "ContentB");
        pageManager.addPage(page1);
        pageManager.addPage(page2);

        assertEquals(1, pageManager.getAllPages().size());
    }

    @Test
    public void testAddPageWithNullValues() {
        pageManager.addPage(new Page(testSiteA.getSlug(), null, null, null, null));
        assertEquals(0, pageManager.getAllPages().size());
    }

    @Test
    public void testAddPageWithInvalidSite() {
        pageManager.addPage(new Page("", "",2, "", ""));
        assertEquals(0, pageManager.getAllPages().size());
    }

    @Test
    public void testGetAllBySiteOrder() {
        pageManager.addPage(new Page(testSiteB.getSlug(), "I'm from a different site!", 3, "TitleA","ContentA"));
        pageManager.addPage(new Page(testSiteA.getSlug(), "Slug1", 3, "TitleA","ContentA"));
        pageManager.addPage(new Page(testSiteA.getSlug(), "Slug6", 0, "TitleB","ContentB"));
        pageManager.addPage(new Page(testSiteA.getSlug(), "Slug3", 2, "TitleC","ContentC"));
        pageManager.addPage(new Page(testSiteA.getSlug(), "Slug9", 1, "TitleD","ContentD"));
        pageManager.addPage(new Page(testSiteA.getSlug(), "Slug12", 4, "TitleE","ContentE"));
        List<Page> all = pageManager.getAllPagesOfSite(testSiteA.getSlug());
        assertEquals(5, all.size());
        for(int i = 0; i < all.size(); ++i) assertEquals(all.get(i).getIndex(),i);
    }

    @Test
    public void testEffectOfSiteDelete() {
        siteManager.addSite(new Site("toDeleteSite", "siteName"));
        pageManager.addPage(new Page("toDeleteSite", "Slug", 3, "Title", "content"));
        siteManager.delete(siteManager.getSiteBySlug("toDeleteSite").getPrimaryKey());
        assertEquals(0, pageManager.getAllPages().size());
    }

    @Test
    public void testSiteUpdateEffectOnPage() {
        siteManager.addSite(new Site("toUpdateSite", "siteName"));
        pageManager.addPage(new Page("toUpdateSite", "Slug", 3, "title", "content"));
        Site updatedSite = siteManager.getSiteBySlug("toUpdateSite");
        updatedSite.setName("UpdatedSite");
        siteManager.update(updatedSite);
        assertEquals("UpdatedSite", pageManager.getAllPages().get(0).getSite().getName());
    }

    @Test
    public void testDeleteNonexistentPrimaryKey() {
        assertThrows(IllegalArgumentException.class, () -> {
            pageManager.delete(-1);
        });
    }

    @Test
    public void testUpdateWithNullData() {
        assertThrows(PersistenceException.class, () -> {
            fillDatabase();
            int pkOfObjectToUpdate =pageManager.getAllPages().get(0).getPrimaryKey();
            Page badPage = new Page(pkOfObjectToUpdate,"Disease1",null, null, null, null);
            pageManager.update(badPage);
            for (int i = 0; i < pageManager.getAllPages().size(); ++i) {
                System.out.println(pageManager.getAllPages().get(i));
            }
        });

    }

    @Test
    public void testAddPage() {
        pageManager.addPage(new Page(testSiteA.getSlug(),"biliary_atresia", 0, "Biliary Atresia", ""
                + "Lorem ipsum dolor sit amet, consectetur adipiscing elit,"
                + "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                ""));
        assertEquals(pageManager.getAllPages().size(), 1);
    }

    @Test
    public void testSafeNames() {
        pageManager.addPage(new Page(testSiteA.getSlug(),";DROP TABLE Pages", 2, "';'''", "sdafds"));
        assertEquals(pageManager.getAllPages().size(), 1);
    }

    @Test
    public void testEmptyContent() {
        pageManager.addPage(new Page(testSiteB.getSlug(),"biliary_atresia", 0, "", ""));
        assertEquals(pageManager.getAllPages().size(), 1);
    }

    @Test
    public void testGetAll() {
        fillDatabase();
        assertEquals(pageManager.getAllPages().size(), getListOfPages().size());
    }

    @Test
    public void testDeleteAll() {
        fillDatabase();
        pageManager.deleteAll();
        assertEquals(pageManager.getAllPages().size(), 0);
    }

    @Test
    public void testDelete() {
        Page replacementPage = pageManager.addPage(new Page(testSiteB.getSlug(),"Slug3", 10, "Title3", "New content!"));
        pageManager.delete(pageManager.getAllPages().get(0).getPrimaryKey());
        assertEquals(pageManager.getAllPages().size(), 0);
    }

    @Test
    public void testUpdatePage() {
        assertNotNull(siteManager.getSiteBySlug("Disease1"));

        Page newPage = new Page("Disease1","Slug3", 10, "Title3", "New content!");
        pageManager.addPage(newPage);
        int assignedID = pageManager.getAllPages().get(0).getPrimaryKey();
        fillDatabase();
        Page updatedPage = new Page(assignedID, "Disease1","Slug3", 14, "Title3",
                "New conwishwashchangedtent!");
        pageManager.update(updatedPage);
        Page foundPage = pageManager.getByPrimaryKey(assignedID);

        assertEquals(foundPage.getContent(), updatedPage.getContent());
    }

    @Test
    public void testGetPageBySiteAndSlug() {
        Page newPage = new Page(testSiteB.getSlug(),"Slug3", 10, "Title3", "New content!");
        pageManager.addPage(newPage);
        assertNotNull(pageManager.getPageBySiteAndSlug(testSiteB.getSlug(), "Slug3"));
    }

    @Test
    public void testUnfoundPrimaryKey() {
        fillDatabase();
        assertNull(pageManager.getByPrimaryKey(-100));
    }



    /**
     * List to fill in database with example Page objects
     * @return example objects
     */
    private static ArrayList<Page> getListOfPages() {
        ArrayList<Page> listOfPages = new ArrayList<>();

        listOfPages.add(new Page(testSiteA.getSlug(),"Slug1", 0, "Title1", "Content1"));
        listOfPages.add(new Page(testSiteA.getSlug(),"Slug2", 8, "Title2", "Content2"));
        listOfPages.add(new Page(testSiteA.getSlug(),"Slug3", 7, "Title3", "Content3"));
        listOfPages.add(new Page(testSiteA.getSlug(),"Slug4", 5, "Title4", "Content4"));
        listOfPages.add(new Page(testSiteA.getSlug(),"Slug5", 4, "Title5", "Content5"));
        listOfPages.add(new Page(testSiteA.getSlug(),"Slug8", 4, "Title5", "Content5"));
        listOfPages.add(new Page(testSiteA.getSlug(),"Slug9", 4, "Title5", "Content5"));
        listOfPages.add(new Page(testSiteA.getSlug(),"Slug12", 4, "Title5", "Content5"));
        listOfPages.add(new Page(testSiteA.getSlug(),"Slug17", 4, "Title5", "Content5"));

        return listOfPages;
    }

    private void fillDatabase() {
        ArrayList<Page> listOfPage = getListOfPages();
        for (int i = 0; i<listOfPage.size(); ++i) pageManager.addPage(listOfPage.get(i));
    }

}
