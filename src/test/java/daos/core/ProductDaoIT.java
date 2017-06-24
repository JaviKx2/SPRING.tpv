package daos.core;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import config.PersistenceConfig;
import config.TestsPersistenceConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class, TestsPersistenceConfig.class})
public class ProductDaoIT {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private EmbroideryDao embroideryDao;

    @Autowired
    private TextilePrintingDao textilePrintingDao;

    @Test
    public void testCreateArticle() {
        assertTrue(5 <= articleDao.count());
    }

    @Test
    public void testCreateEmbroidery() {
        assertTrue(5 <= embroideryDao.count());
    }

    @Test
    public void testCreateTextilePrinting() {
        assertTrue(5 <= textilePrintingDao.count());
    }

}
