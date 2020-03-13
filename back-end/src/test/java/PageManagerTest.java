package test.java;

import main.java.com.projectBackEnd.Entities.Page.Page;
import main.java.com.projectBackEnd.Entities.Page.PageManager;
import main.java.com.projectBackEnd.Entities.Page.PageManagerInterface;
import main.java.com.projectBackEnd.Entities.Site.Site;
import main.java.com.projectBackEnd.Entities.Site.SiteManager;
import main.java.com.projectBackEnd.Entities.Site.SiteManagerInterface;
import main.java.com.projectBackEnd.HibernateUtility;
import org.junit.*;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PageManagerTest {
    private static ConnectionLeakUtil connectionLeakUtil = null;
    private static PageManagerInterface pageManager = null;
    private static SiteManagerInterface siteManager = null;
    private static Site testSiteA = null;
    private static Site testSiteB = null;
    @BeforeClass
    public static void setUpDatabase() {
        HibernateUtility.setResource("test/resources/testhibernate.cfg.xml");
        pageManager = PageManager.getPageManager();
        siteManager = SiteManager.getSiteManager();
        siteManager.addSite("Disease1");
        siteManager.addSite("Disease2");
        testSiteA = siteManager.getBySiteName("Disease1");
        testSiteB = siteManager.getBySiteName("Disease2");
        connectionLeakUtil = new ConnectionLeakUtil();


    }

    @AfterClass
    public static void assertNoLeaks() {
        siteManager.deleteAll();
        HibernateUtility.shutdown();
        connectionLeakUtil.assertNoLeaks();
    }

    @Before
    public void setUp() {
        pageManager.deleteAll();
    }

    @Test
    public void testNoDuplicateCompositeKey() {
        //Page(Site site, String slug, Integer index, String title, String content) {
        Page page1 = new Page(testSiteA, "sameSlug", 1, "TitleA", "ContentA");
        Page page2 = new Page(testSiteB, "sameSlug", 1, "TitleB", "ContentB");
        pageManager.addPage(page1);
        pageManager.addPage(page2);

        assertEquals(2, pageManager.getAllPages().size());
    }

    @Test
    public void testDuplicateCompositeKey() {
        Page page1 = new Page(testSiteA, "sameSlug", 1, "TitleA", "ContentA");
        Page page2 = new Page(testSiteA, "sameSlug", 1, "TitleB", "ContentB");
        pageManager.addPage(page1);
        pageManager.addPage(page2);
        assertEquals(1, pageManager.getAllPages().size());
    }

    @Test
    public void testNullSlugIndexTitleContent() {
        pageManager.addPage(new Page(testSiteA, null, null, null, null));
        assertEquals(0, pageManager.getAllPages().size());
    }
    @Test
    public void testInvalidSite() {
        pageManager.addPage(new Page("", "",2, "", ""));
        assertEquals(0, pageManager.getAllPages().size());
    }

    @Test
    public void testGetAllForStateRetrieval() {
        pageManager.addPage(testSiteA, "Slug1", 3, "TitleA","ContentA");
        pageManager.addPage(testSiteA, "Slug6", 0, "TitleB","ContentB");
        pageManager.addPage(testSiteA, "Slug3", 2, "TitleC","ContentC");
        pageManager.addPage(testSiteA, "Slug9", 1, "TitleD","ContentD");
        pageManager.addPage(testSiteA, "Slug12", 4, "TitleE","ContentE");
        List<Page> all = pageManager.getAllPagesOfSite(testSiteA);
        for(int i = 0; i < all.size(); ++i) {
            assertEquals(all.get(i).getIndex(),i);
        }
    }

    @Test
    public void testForeignKeyDelete() {
        siteManager.addSite("toDeleteSite");
        pageManager.addPage("toDeleteSite", "Slug", 3, "Title", "content");
        siteManager.delete(siteManager.getBySiteName("toDeleteSite"));
        assertEquals(0, pageManager.getAllPages().size());
    }

    @Test
    public void testSiteUpdateEffectOnPage() {
        siteManager.addSite("toUpdateSite");
        pageManager.addPage("toUpdateSite", "Slug", 3, "title", "content");
        Site updatedSite = siteManager.getBySiteName("toUpdateSite");
        updatedSite.setName("UpdatedSite");
        siteManager.update(updatedSite);
        assertEquals("UpdatedSite", pageManager.getAllPages().get(0).getSite().getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteNonexistentPrimaryKey() {
        pageManager.delete("");
    }
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteNotInDBObject() {
        Page pageNotInTable = new Page(testSiteB,"notaddedtotable", 0, "notaddedtoTable", "");
        pageManager.delete(pageNotInTable);
    }
    @Test(expected = PersistenceException.class)
    public void testUpdateWithNullData() {
        fillDatabase();
        int primaryKeyOfObjectToUpdate =pageManager.getAllPages().get(0).getPrimaryKey();
        Page badPage = new Page(primaryKeyOfObjectToUpdate,testSiteB,null, null, null, null);
        pageManager.update(badPage);
        for (int i = 0; i < pageManager.getAllPages().size(); ++i) {
            System.out.println(pageManager.getAllPages().get(i));
        }
    }

    @Test
    public void testCreateAndSavePage() {
        pageManager.addPage(testSiteA,"biliary_atresia", 0, "Biliary Atresia", "" +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                "");
        Assert.assertEquals(pageManager.getAllPages().size(), 1);
    }

    @Test
    public void testSafeNames() {
        pageManager.addPage(testSiteA,";DROP TABLE Pages", 2, "';'''", "sdafds");
        Assert.assertEquals(pageManager.getAllPages().size(), 1);
    }

    @Test
    public void testEmptyContent() {
        pageManager.addPage(testSiteB,"biliary_atresia", 0, "", "");
        Assert.assertEquals(pageManager.getAllPages().size(), 1);
    }

    @Test
    public void testGetAll() {
        fillDatabase();
        Assert.assertEquals(pageManager.getAllPages().size(), getListOfPages().size());
    }

    @Test
    public void testIdenticalPages() {
        Page page = new Page(testSiteB, "biliary_atresia", 0, "Biliary Atresia", "" +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                "");
        Page page2 = new Page(testSiteB,"biliary_atresia", 0, "Biliary Atresia", "" +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                "");
        assertThat(page, samePropertyValuesAs(page2));
        //assertTrue(page.equals(page2));
        //TODO Notice this has no equals method implemented as not necessary
    }

    @Test
    public void testDeleteAll() {
        fillDatabase();
        pageManager.deleteAll();
        Assert.assertEquals(pageManager.getAllPages().size(), 0);
    }

    @Test
    public void testDelete() {
        Page replacementPage = pageManager.addPage(testSiteB,"Slug3", 10, "Title3", "New content!");
        pageManager.delete(pageManager.getAllPages().get(0).getPrimaryKey());
        Assert.assertEquals(pageManager.getAllPages().size(), 0);
    }

    @Test
    public void testUpdatePage() {
        Page newPage = new Page(9999, testSiteB,"Slug3", 10, "Title3", "New content!");
        pageManager.addPage(newPage);
        int assignedID = pageManager.getAllPages().get(0).getPrimaryKey();
        fillDatabase();
        Page updatedPage = new Page(assignedID, testSiteB,"Slug3", 14, "Title3", "New conwishwashchangedtent!");
        pageManager.update(updatedPage);
        Page foundPage = pageManager.getByPrimaryKey(assignedID);

        Assert.assertEquals(foundPage.getContent(), updatedPage.getContent());
    }

    @Test
    public void testGetPageBySiteAndSlug() {
        Page newPage = new Page(testSiteB,"Slug3", 10, "Title3", "New content!");
        pageManager.addPage(newPage);
        assertNotNull(pageManager.getPageBySiteAndSlug(testSiteB, "Slug3"));
    }

    @Test
    public void testUnfoundPrimaryKey() {
        fillDatabase();
        assertNull(pageManager.getByPrimaryKey(-100));
    }
    @Test(expected = IllegalArgumentException.class)
    public void testNullPrimaryKey() throws IllegalStateException {
        fillDatabase();
        assertNull(pageManager.getByPrimaryKey(null));
    }
    private static ArrayList<Page> getListOfPages() {
        ArrayList<Page> listOfPages = new ArrayList<>();
        listOfPages.add(new Page(testSiteA,"Slug1", 0, "Title1", "Content1"));
        listOfPages.add(new Page(testSiteA,"Slug2", 8, "Title2", "Content2"));
        listOfPages.add(new Page(testSiteA,"Slug3", 7, "Title3", "Content3"));
        listOfPages.add(new Page(testSiteA,"Slug4", 5, "Title4", "Content4"));
        listOfPages.add(new Page(testSiteA,"Slug5", 4, "Title5", "Content5"));
        listOfPages.add(new Page(testSiteA,"Slug8", 4, "Title5", "Content5"));
        listOfPages.add(new Page(testSiteA,"Slug9", 4, "Title5", "Content5"));
        listOfPages.add(new Page(testSiteA,"Slug12", 4, "Title5", "Content5"));
        listOfPages.add(new Page(testSiteA,"Slug17", 4, "Title5", "Content5"));
        return listOfPages;
    }
    private void fillDatabase() {
        for (Page p : getListOfPages()) {
            pageManager.addPage(p);
        }
    }
}
