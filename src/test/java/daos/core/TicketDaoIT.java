package daos.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import config.PersistenceConfig;
import config.TestsPersistenceConfig;
import entities.core.Ticket;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class, TestsPersistenceConfig.class})
public class TicketDaoIT {

    @Autowired
    private TicketDao ticketDao;

    @Test
    public void testCount() {
        assertTrue(5 <= ticketDao.count());
    }

    @Test
    public void testFindFirstByOrderByCreatedDescIdDesc() {
        Ticket ticket = ticketDao.findFirstByOrderByCreatedDescIdDesc();
        assertEquals(6, ticket.getId());
    }

    @Test
    public void testFindFirstByReference() {
        Ticket ticket = ticketDao.findAll().get(0);
        assertEquals(ticket, ticketDao.findFirstByReference(ticket.getReference()));
    }

    @Test
    public void testFirstByDate() {
        int date = Integer.parseInt((new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime())));
        assertTrue(6 >= ticketDao.findByDate(date).size());
    }

    @Test
    public void testFindByUserMobile() {
        int pageNumber = 1;
        int pageSize = 1;
        Page<Ticket> ticketPage = ticketDao.findByUserMobile(666000002L, new PageRequest(pageNumber, pageSize));
        assertNotNull(ticketPage);
        assertEquals(pageSize, ticketPage.getNumberOfElements());
    }
}
