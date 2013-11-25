package dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import dto.DonjonEquItem;
import service.DonjonItemService;

public class DonjonItemServiceTest {

	DonjonItemService service;

    @Before
    public void setUp() {
        ApplicationContext context = 
        new ClassPathXmlApplicationContext("applicationContext.xml");
        service = (DonjonItemService) context.getBean("donjonItemService");
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testRegistAndGetBumon() {
        try {
            List<DonjonEquItem> itemList = service.queryEquItemVersion(1);

            // 部門名が正しいか？
            assertEquals(10000001, itemList.get(0).getItemDetailId());
            assertEquals("ボルケーノソード", itemList.get(0).getItemName());
        } finally {
        }
    }
}
