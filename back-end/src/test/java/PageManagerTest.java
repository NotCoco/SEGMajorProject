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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PageManagerTest {
    private static ConnectionLeakUtil connectionLeakUtil = null;
    private static PageManagerInterface pageManager = null;
    private static Site testSiteA = null;
    private static Site testSiteB = null;
    @BeforeClass
    public static void setUpDatabase() {
        HibernateUtility.setResource("testhibernate.cfg.xml");
        pageManager = PageManager.getPageManager();
        SiteManagerInterface s = SiteManager.getSiteManager();
        s.addSite("Disease1");
        s.addSite("Disease2");
        testSiteA = s.getBySiteName("Disease1");
        testSiteB = s.getBySiteName("Disease2");
        connectionLeakUtil = new ConnectionLeakUtil();


    }

    @AfterClass
    public static void assertNoLeaks() {
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
}
