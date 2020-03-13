package test.java;

import main.java.com.projectBackEnd.Entities.Page.Page;
import main.java.com.projectBackEnd.Entities.Page.PageManager;
import main.java.com.projectBackEnd.Entities.Page.PageManagerInterface;
import main.java.com.projectBackEnd.Entities.Site.Site;
import main.java.com.projectBackEnd.Entities.Site.SiteManager;
import main.java.com.projectBackEnd.Entities.Site.SiteManagerInterface;
import main.java.com.projectBackEnd.HibernateUtility;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PageManagerTest {
    private static ConnectionLeakUtil connectionLeakUtil = null;
    private static PageManagerInterface pageManager = null;
    private static SiteManagerInterface siteManager = null;
    private static Site testSiteA = null;
    private static Site testSiteB = null;
    @BeforeClass
    public static void setUpDatabase() {
        HibernateUtility.setResource("testhibernate.cfg.xml");
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
}
