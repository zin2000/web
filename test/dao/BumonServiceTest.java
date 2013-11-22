package dao;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import dto.Bumon;

import service.IBumonService;

public class BumonServiceTest {

    IBumonService service;

    @Before
    public void setUp() {
        ApplicationContext context = 
        new ClassPathXmlApplicationContext("applicationContext.xml");
        service = (IBumonService) context.getBean("bumonService");
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testRegistAndGetBumon() {
        try {
            Bumon b = new Bumon("000001", "ESC");
            service.registBumon(b);

            // 部門名が正しいか？
            assertEquals("ESC", service.getBumon("000001").getNmBumon());
        } finally {
            //service.removeBumon("000001");
        }
    }

    @Test
    public void testGetAllBumon() {
        try {
            Bumon b1 = new Bumon("000001", "ESC");
            Bumon b2 = new Bumon("000002", "OSK");
            service.registBumon(b1);
            service.registBumon(b2);

            // 部門数が2であるか？
            assertEquals(2, service.getAllBumon().size());
        } finally {
            service.removeBumon("000001");
            service.removeBumon("000002");
        }
    }
}
