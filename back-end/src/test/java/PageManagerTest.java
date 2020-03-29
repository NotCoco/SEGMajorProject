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
    //public Page(String siteSlug, String slug, Integer index, String title, String content) {

    /**
     * Test pages are created properly with their foreign key assignments
     */
    @Test
    public void testCreateValidPage() {
        Page page = new Page(testSiteB.getSlug(), "cool slug", 1, "Interesting", "Content");
        assertEquals("cool slug", page.getSlug());
        assertEquals(1, page.getIndex());
        assertEquals(testSiteB.getSlug(), page.getSite().getSlug());
    }

    /**
     * Test that pages with a bad site name are given a null Site field
     */
    @Test
    public void testCreatePageWithBadSite() {
        Page page = new Page("this is not a slug of a given site", "cool slug", 1, "Interesting", "Content");
        assertNull(page.getSite());
    }

    /**
     * Test that a page object with null values can still be created
     */
    @Test
    public void testCreateWithNullValuesPage() {
        Page page = new Page(null,null,null,null,null);
        assertNotNull(page);
        assertNull(page.getSlug());
        assertNull(page.getSite());
        assertNull(page.getContent());
        assertNull(page.getTitle());
        assertNull(page.getIndex());
    }

    /**
     * Test that a page object with empty values will still be created
     */
    @Test
    public void testCreatePageWithEmptyValues() {
        Page page = new Page("","",0,"","");
        assertNotNull(page);
        assertEquals("", page.getSlug());
        assertNull(page.getSite());
    }

    /**
     * Test that pages when copying eachother become identical
     */
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

    //Testing Foreign Key constraints

    /**
     * Test a deleted site removes its associated page
     */
    @Test
    public void testEffectOfSiteDelete() {
        siteManager.addSite(new Site("toDeleteSite", "siteName"));
        pageManager.addPage(new Page("toDeleteSite", "Slug", 3, "Title", "content"));
        siteManager.delete(siteManager.getSiteBySlug("toDeleteSite").getPrimaryKey());
        assertEquals(0, pageManager.getAllPages().size());
    }

    /**
     * Test an update to a site will trickle onto its pages
     */
    @Test
    public void testSiteUpdateEffectOnPage() {
        siteManager.addSite(new Site("toUpdateSite", "siteName"));
        pageManager.addPage(new Page("toUpdateSite", "Slug", 3, "title", "content"));
        Site updatedSite = siteManager.getSiteBySlug("toUpdateSite");
        updatedSite.setName("UpdatedSite");
        siteManager.update(updatedSite);
        assertEquals("UpdatedSite", pageManager.getAllPages().get(0).getSite().getName());
    }

    //Testing PageManagerInterface: getAllPages

    /**
     * Test the fill database method below, and the getAllPages method to show that all are successfully added.
     * Expected: All the pages from the list are added successfully.
     */
    @Test
    public void testFillingAndGetting() {
        fillDatabase(getListOfPages());
        assertEquals(getListOfPages().size(), pageManager.getAllPages().size());
    }

    /**
     * Test the fill database method such that all the pages stored have matching values to the ones added.
     */
    @Test
    public void testFillingAndGettingValues() {
        ArrayList<Page> addedPages = getListOfPages();
        fillDatabase(addedPages);
        List<Page> foundPages = pageManager.getAllPages();
        for (int i =0; i < foundPages.size() ; ++i) {
            Page foundPage = foundPages.get(i);
            Page addedPage = addedPages.get(i);
            assertEquals(addedPage.getTitle(), foundPage.getTitle());
            assertEquals(addedPage.getSlug(), foundPage.getSlug());
            assertNotNull(foundPage.getPrimaryKey());
        }
    }

    /**
     * Test that an empty table returns no pages
     */
    @Test
    public void testGetAllOnEmptyTable() {
        assertEquals(0, pageManager.getAllPages().size());
    }

    //Test PageManagerInterface: getAllPagesBySite
    /**
     * Test that the order returned by getAllPages sorts the pages in the database by index before returning.
     */
    @Test
    public void testGetAllBySiteOrder() {
        pageManager.addPage(new Page(testSiteB.getSlug(), "I'm from a different site!", 3, "TitleA","ContentA"));
        pageManager.addPage(new Page(testSiteA.getSlug(), "Slug3", 3, "TitleA","ContentA"));
        pageManager.addPage(new Page(testSiteA.getSlug(), "Slug0", 0, "TitleB","ContentB"));
        pageManager.addPage(new Page(testSiteA.getSlug(), "Slug2", 2, "TitleC","ContentC"));
        pageManager.addPage(new Page(testSiteA.getSlug(), "Slug1", 1, "TitleD","ContentD"));
        pageManager.addPage(new Page(testSiteA.getSlug(), "Slug4", 4, "TitleE","ContentE"));
        List<Page> allPagesOfSite = pageManager.getAllPagesOfSite(testSiteA.getSlug());
        assertEquals(5, allPagesOfSite.size());
        for(int i = 0; i < allPagesOfSite.size(); ++i) {
            assertEquals(i, allPagesOfSite.get(i).getIndex());
            assertEquals("Slug"+i, allPagesOfSite.get(i).getSlug());
        }
    }

    /**
     * Test what is returned from a site slug that doesn't exist
     */
    @Test
    public void testGetAllWithNonExistentSite() {
        assertEquals(0, pageManager.getAllPagesOfSite("not an existing site slug").size());
    }

    /**
     * Test what is returned from a site that has no pages
     */
    @Test
    public void testGetAllBySiteWithNoPages() {
        siteManager.addSite(new Site("this site has no pages", "name"));
        assertEquals(0, pageManager.getAllPagesOfSite("this site has no pages").size());
    }

    /**
     * Test what is returned when a null is given as the site
     */
    @Test
    public void testGetAllByNullSite() {
        assertEquals(0, pageManager.getAllPagesOfSite(null).size());
    }

    //Testing PageManagerInterface: deleteAll
    /**
     * Testing a database can have deleteAll run on it, even if it is empty
     * Expected: The number of entries in the database remains zero.
     */
    @Test
    public void testDeleteAllEmptyDatabase() {
        pageManager.deleteAll();
        assertEquals(0, pageManager.getAllPages().size());
        pageManager.deleteAll();
        assertEquals(0, pageManager.getAllPages().size());
    }

    /**
     * Testing a database will be flushed by the deleteAll method used between tests
     * Expected: The entries will disappear from the database.
     */
    @Test
    public void testDeleteAllFilledDatabase() {
        fillDatabase(getListOfPages());
        assertEquals(getListOfPages().size(), pageManager.getAllPages().size());
        pageManager.deleteAll();
        assertEquals(0, pageManager.getAllPages().size());
    }

    //Testing PageManagerInterface: addPage

    /**
     * Test that pages with valid sites can be added provided they don't violate constraints
     */
    @Test
    public void testAddRegularPagesKey() {
        Page page1 = new Page(testSiteA.getSlug(), "sameSlug", 1, "TitleA", "ContentA");
        Page page2 = new Page(testSiteB.getSlug(), "sameSlug", 1, "TitleB", "ContentB");
        pageManager.addPage(page1);
        pageManager.addPage(page2);

        assertEquals(2, pageManager.getAllPages().size());
    }

    /**
     * Test that pages which violate the constraints of same site slug / slug will not be added
     */
    @Test
    public void testViolateDuplicateCompositeKey() {
        Page page1 = new Page(testSiteA.getSlug(), "sameSlug", 1, "TitleA", "ContentA");
        Page page2 = new Page(testSiteA.getSlug(), "sameSlug", 1, "TitleB", "ContentB");
        pageManager.addPage(page1);
        pageManager.addPage(page2);

        assertEquals(1, pageManager.getAllPages().size());
    }

    /**
     * Test that a page with null values will not be added
     */
    @Test
    public void testAddPageWithNullValues() {
        pageManager.addPage(new Page(testSiteA.getSlug(), null, null, null, null));
        assertEquals(0, pageManager.getAllPages().size());
    }

    /**
     * Test that a page with an invalid site will not be added
     */
    @Test
    public void testAddPageWithInvalidSite() {
        pageManager.addPage(new Page("", "",2, "", ""));
        assertEquals(0, pageManager.getAllPages().size());
    }

    /**
     * Test that pages with forbidden/strange characters will still be added safely
     */
    @Test
    public void testAddPageWithUnsafeValues() {
        pageManager.addPage(new Page(testSiteA.getSlug(),";DROP TABLE Pages", 2, "';'''", "sdafds"));
        assertEquals(pageManager.getAllPages().size(), 1);
    }

    /**
     * Test Pages with empty values can still be added provided they don't violate constraints
     */
    @Test
    public void testEmptyContent() {
        pageManager.addPage(new Page(testSiteB.getSlug(),"", 0, "", ""));
        assertEquals(pageManager.getAllPages().size(), 1);
    }

    //Test PageManagerInterface: getByPrimaryKey
    /**
     * Testing that page objects can be found and made from their primary key.
     * Expected: The page found shares the same values as the page in the database.
     */
    @Test
    public void testGetByPrimaryKey() {
        fillDatabase(getListOfPages());
        Page foundPage = pageManager.getAllPages().get(0);
        int pagePK = foundPage.getPrimaryKey();
        Page foundPageFromDB = pageManager.getByPrimaryKey(pagePK);
        assertEquals(foundPage.getTitle(), foundPageFromDB.getTitle());
        assertEquals(foundPage.getIndex(), foundPageFromDB.getIndex());
        assertEquals(foundPage.getSlug(), foundPageFromDB.getSlug());
    }

    /**
     * Testing that attempting to obtain a page article with a primary key that doesn't exist returns null
     */
    @Test
    public void testGetUnfoundPrimaryKey() {
        assertNull(pageManager.getByPrimaryKey(-1));
    }

    /**
     * Testing an error is thrown if a primary key searched for is null
     */
    @Test
    public void testGetNullPrimaryKey() {
        fillDatabase(getListOfPages());
        int previousSize = pageManager.getAllPages().size();
        try {
            pageManager.getByPrimaryKey(null);
            fail();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            assertEquals(pageManager.getAllPages().size(), previousSize);
        }
    }

    //Testing PageManagerInterface: getBySiteAndSlug

    /**
     * Test the correct site can be found by giving its slug and site's slug
     */
    @Test
    public void testGetPageBySiteAndSlug() {
        Page newPage = new Page(testSiteB.getSlug(),"Slug3", 10, "Title3", "New content!");
        pageManager.addPage(newPage);
        assertNotNull(pageManager.getPageBySiteAndSlug(testSiteB.getSlug(), "Slug3"));
    }

    /**
     * Test how the getBySiteAndSlug reacts to a site that cannot be found
     */
    @Test
    public void testGetBySiteSlugUnfoundSite() {
        Page newPage = new Page(testSiteB.getSlug(),"Slug3", 10, "Title3", "New content!");
        pageManager.addPage(newPage);
        assertNull(pageManager.getPageBySiteAndSlug("Not found site sorry", "Slug3"));
    }
    /**
     * Test how the getBySiteAndSlug reacts to a page slug that cannot be found
     */
    @Test
    public void testGetBySiteSlugUnfoundPageSlug() {
        assertNull(pageManager.getPageBySiteAndSlug(testSiteB.getSlug(), "not a real page sorry"));
    }
    /**
     * Test how the getBySiteAndSlug reacts to a null site input
     */
    @Test
    public void testGetBySiteSlugNullSite() {
        Page newPage = new Page(testSiteB.getSlug(),"Slug3", 10, "Title3", "New content!");
        pageManager.addPage(newPage);
        assertNull(pageManager.getPageBySiteAndSlug(null, "Slug3"));
    }
    /**
     * Test how the getBySiteAndSlug reacts to a null page input
     */
    @Test
    public void testGetBySiteSlugNullPageSlug() {
        assertNull(pageManager.getPageBySiteAndSlug(testSiteB.getSlug(), null));
    }

    /**
     * Test how getBySiteAndSlug reacts to double null input
     */
    @Test
    public void testGetBySiteSlugNulls() {
        assertNull(pageManager.getPageBySiteAndSlug(null, null));
    }

    //Testing PageManagerInterface: delete

    /**
     * Test the delete function removes a page from the database table
     */
    @Test
    public void testDelete() {
        Page replacementPage = pageManager.addPage(new Page(testSiteB.getSlug(),"Slug3", 10, "Title3", "New content!"));
        pageManager.delete(pageManager.getAllPages().get(0).getPrimaryKey());
        assertEquals(pageManager.getAllPages().size(), 0);
    }

    /**
     * Test that an error is thrown if the primary key sent to delete could not be found
     */
    @Test
    public void testWithDeleteUnfoundPrimaryKey() {
        int previousSize = pageManager.getAllPages().size();
        try {
            pageManager.delete(-1);
            fail();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            assertEquals(pageManager.getAllPages().size(), previousSize);
            // Check that nothing has been removed
        }
    }
    /**
     * Test the correct page was infact deleted when using delete
     */
    @Test
    public void testCorrectPageDeletedUsingPrimaryKey() {
        Page toBeDeleted = pageManager.addPage(new Page(testSiteB.getSlug(),"Slug3", 10, "Title3", "New content!"));
        Page alsoAdded = pageManager.addPage(new Page(testSiteA.getSlug(),"Slug2", 1, "Title", "New!"));
        assertEquals(2, pageManager.getAllPages().size());
        pageManager.delete(toBeDeleted.getPrimaryKey());
        assertEquals(1, pageManager.getAllPages().size());
        Page leftoverPage = pageManager.getAllPages().get(0);
        assertEquals(alsoAdded.getSlug(), leftoverPage.getSlug());
        assertNull(pageManager.getByPrimaryKey(toBeDeleted.getPrimaryKey()));
    }

    /**
     * Test interaction when a null primary key is sent
     */
    @Test
    public void testDeleteWithNullPrimaryKey() {
        int previousSize = pageManager.getAllPages().size();
        try {
            pageManager.delete(null);
            fail();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            assertEquals(pageManager.getAllPages().size(), previousSize);
            // Check that nothing has been removed
        }
    }

    //Testing PageManagerInterface: update

    /**
     * Test that updating a page works - including changing unique slug
     */
    @Test
    public void testUpdatePage() {
        assertNotNull(siteManager.getSiteBySlug("Disease1"));

        Page newPage = new Page("Disease1","Slug3", 10, "Title3", "New content!");
        pageManager.addPage(newPage);
        int assignedID = pageManager.getAllPages().get(0).getPrimaryKey();
        fillDatabase(getListOfPages());
        Page updatedPage = new Page(assignedID, testSiteB.getSlug(),"Slug3", 14, "Title3",
                "New conwishwashchangedtent!");
        pageManager.update(updatedPage);
        Page foundPage = pageManager.getByPrimaryKey(assignedID);

        assertEquals(foundPage.getContent(), updatedPage.getContent());
    }

    /**
     * Test updating with null data throws an error
     */
    @Test
    public void testUpdateWithNullData() {
        fillDatabase(getListOfPages());
        int previousSize = pageManager.getAllPages().size();
        int pkOfObjectToUpdate =pageManager.getAllPages().get(0).getPrimaryKey();
        Page badPage = new Page(pkOfObjectToUpdate,"Disease1",null, null, null, null);
        try {
            pageManager.update(badPage);
            fail();
        } catch( PersistenceException e) {
            e.printStackTrace();
            assertEquals(pageManager.getAllPages().size(), previousSize);
        }

    }

    /**
     * Test updating a page to be the same as another page is not allowed
     */
    @Test
    public void testUpdateToViolateDuplicateSlugs() {
        Page page1 = new Page(testSiteA.getSlug(), "sameSlug", 1, "TitleA", "ContentA");
        Page page2 = pageManager.addPage(new Page(testSiteB.getSlug(), "sameSlug", 1, "TitleB", "ContentB"));
        Page replacement = new Page(page2.getPrimaryKey(), testSiteA.getSlug(), "sameSlug", 1, "TitleB", "ContentB");
        pageManager.update(page2);
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

    /**
     * Fills the database with a list of pages
     * @param pagesToAdd The list of pages to be inserted to the database.
     */
    private void fillDatabase(ArrayList<Page> pagesToAdd) {
        for (int i = 0; i<pagesToAdd.size(); ++i) pageManager.addPage(pagesToAdd.get(i));
    }

}
