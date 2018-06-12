package com.code.api.advertise;

import com.code.api.advertise.controller.AdvertiserController;
import com.code.api.advertise.exception.AdvertiserAlreadyExistsException;
import com.code.api.advertise.exception.AdvertiserNotFoundException;
import com.code.api.advertise.model.Advertiser;
import com.code.api.advertise.model.TransactionValidity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdvertiseApplicationTests {

    @Autowired
    private AdvertiserController advertiserController;

	@Test
	public void testSelectAllAdvertisers() {
        Collection<Advertiser> advertisers = advertiserController.findAllAdvertisers();
        assertNotNull(advertisers);
        assertEquals(3, advertisers.size());
    }

    @Test
    public void testSelectAdvertiser() {
        Advertiser advertiser = advertiserController.findAdvertiserById(1L);
        Advertiser expAdv = new Advertiser();
        expAdv.setId(1L);
        expAdv.setContactName("Yury Zhyshko");
        expAdv.setName("IHeartMedia");
        expAdv.setCreditLimit(new BigDecimal("102330.09"));
        assertEquals(expAdv.hashCode(), advertiser.hashCode());
        assertEquals(expAdv, advertiser);
    }

    @Test
    public void testFindByName() {
        Advertiser advertiser = advertiserController.findAdvertiserByName("Amazon");
        Advertiser expAdv = new Advertiser();
        expAdv.setId(2L);
        expAdv.setContactName("Jeff Bezos");
        expAdv.setName("Amazon");
        expAdv.setCreditLimit(new BigDecimal("3050.00"));
        assertEquals(expAdv.hashCode(), advertiser.hashCode());
        assertEquals(expAdv, advertiser);
    }

    @Test
    public void testAddDeleteAdvertiser() {
        Advertiser expAdv = new Advertiser();
        expAdv.setContactName("Unknown");
        expAdv.setName("Yelp");
        expAdv.setCreditLimit(new BigDecimal("00.00"));
        advertiserController.addAdvertiser(expAdv);
        Collection<Advertiser> advertisers = advertiserController.findAllAdvertisers();
        assertNotNull(advertisers);
        assertEquals(4, advertisers.size());
        expAdv.setId(4L);
        Advertiser advertiser = advertiserController.findAdvertiserById(4L);
        assertEquals(expAdv.hashCode(), advertiser.hashCode());
        assertEquals(expAdv, advertiser);
        advertiserController.deleteById(4L);
        advertisers = advertiserController.findAllAdvertisers();
        assertNotNull(advertisers);
        assertEquals(3, advertisers.size());
    }

    @Test
    public void testUpdateAdvertiser() {
        Advertiser expAdv = new Advertiser();
        expAdv.setContactName("Jackson");
        expAdv.setName("M&M");
        expAdv.setCreditLimit(new BigDecimal("1.00"));
        advertiserController.updateAdvertiser(1L, expAdv);
        expAdv.setId(1L);
        Collection<Advertiser> advertisers = advertiserController.findAllAdvertisers();
        assertNotNull(advertisers);
        assertEquals(3, advertisers.size());
        Advertiser advertiser = advertiserController.findAdvertiserById(1L);
        assertEquals(expAdv.hashCode(), advertiser.hashCode());
        assertEquals(expAdv, advertiser);
    }

    @Test
    public void testHasEnoughCredit() {
        TransactionValidity expectedValidity = new TransactionValidity();
        expectedValidity.setValid(true);
        expectedValidity.setAmount(new BigDecimal("1.00"));
	    TransactionValidity actualValidity = advertiserController.checkTransaction(2L, new BigDecimal("1.00"));
        assertEquals(expectedValidity.hashCode(), actualValidity.hashCode());
        assertEquals(expectedValidity, actualValidity);
    }

    @Test
    public void testAdvertiserExistsOnAdd() {
        Advertiser expAdv = new Advertiser();
        expAdv.setContactName("Unknow");
        expAdv.setName("Amazon");
        expAdv.setCreditLimit(new BigDecimal("00.00"));
        AdvertiserAlreadyExistsException ex = null;
        try{
            advertiserController.addAdvertiser(expAdv);
        } catch (AdvertiserAlreadyExistsException e) {
            ex = e;
        }
        assertNotNull("Should fail with AdvertiserAlreadyExistsException",ex);
    }

    @Test
    public void testAdvertiserExistsOnUpdate() {
        Advertiser expAdv = new Advertiser();
        expAdv.setContactName("Unknown");
        expAdv.setName("Amazon");
        expAdv.setCreditLimit(new BigDecimal("00.00"));
        AdvertiserAlreadyExistsException ex = null;
        try{
            advertiserController.updateAdvertiser(1L, expAdv);
        } catch (AdvertiserAlreadyExistsException e) {
            ex = e;
        }
        assertNotNull("Should fail with AdvertiserAlreadyExistsException", ex);
    }

    @Test
    public void testAdvertiserNotFoundOnUpdate() {
        Advertiser expAdv = new Advertiser();
        expAdv.setContactName("Unknown");
        expAdv.setName("Amazon");
        expAdv.setCreditLimit(new BigDecimal("00.00"));
        AdvertiserNotFoundException ex = null;
        try{
            advertiserController.updateAdvertiser(10L, expAdv);
        } catch (AdvertiserNotFoundException e) {
            ex = e;
        }
        assertNotNull("Should fail with AdvertiserNotFoundException", ex);
    }

    @Test
    public void testAdvertiserNotFoundOnCheckTransaction() {
        AdvertiserNotFoundException ex = null;
        try{
            advertiserController.checkTransaction(10L, new BigDecimal(300));
        } catch (AdvertiserNotFoundException e) {
            ex = e;
        }
        assertNotNull("Should fail with AdvertiserNotFoundException", ex);
    }

    @Test
    public void testMain(){
        AdvertiseApplication.main(new String[]{});
    }


}
