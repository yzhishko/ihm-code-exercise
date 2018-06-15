package com.code.api.advertise;

import com.code.api.advertise.controller.AdvertiserController;
import com.code.api.advertise.exception.AdvertiserAlreadyExistsException;
import com.code.api.advertise.exception.AdvertiserNotFoundException;
import com.code.api.advertise.exception.NotEnoughCreditException;
import com.code.api.advertise.model.Advertiser;
import com.code.api.advertise.model.Deduction;
import com.code.api.advertise.model.TransactionValidity;
import com.code.api.advertise.service.AdvertiserService;
import org.flywaydb.test.FlywayTestExecutionListener;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.math.BigDecimal;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        FlywayTestExecutionListener.class })
@SpringBootTest
public class AdvertiseApplicationTests {

    @Autowired
    private AdvertiserController advertiserController;

    @Autowired
    private AdvertiserService advertiserService;

	@Test
    @FlywayTest
    public void testSelectAllAdvertisers() {
        Collection<Advertiser> advertisers = advertiserController.findAllAdvertisers();
        assertNotNull(advertisers);
        assertEquals(3, advertisers.size());
    }

    @Test
    @FlywayTest
    public void testSelectAdvertiser() {
        Advertiser advertiser = advertiserController.findAdvertiserById(1L);
        Advertiser expAdv = new Advertiser();
        expAdv.setId(1L);
        expAdv.setContactName("Yury Zhyshko");
        expAdv.setName("IHeartMedia");
        expAdv.setCreditLimit(new BigDecimal("102330.09"));
        expAdv.setVersion(0L);
        assertEquals(expAdv.hashCode(), advertiser.hashCode());
        assertEquals(expAdv, advertiser);
    }

    @Test
    @FlywayTest
    public void testFindByName() {
        Advertiser advertiser = advertiserController.findAdvertiserByName("Amazon");
        Advertiser expAdv = new Advertiser();
        expAdv.setId(2L);
        expAdv.setContactName("Jeff Bezos");
        expAdv.setName("Amazon");
        expAdv.setCreditLimit(new BigDecimal("3050.00"));
        expAdv.setVersion(0L);
        assertEquals(expAdv.hashCode(), advertiser.hashCode());
        assertEquals(expAdv, advertiser);
    }

    @Test
    @FlywayTest
    public void testAddDeleteAdvertiser() {
        Advertiser expAdv = new Advertiser();
        expAdv.setContactName("Unknown");
        expAdv.setName("Yelp");
        expAdv.setCreditLimit(new BigDecimal("00.00"));
        Advertiser advertiser = advertiserController.addAdvertiser(expAdv);
        Collection<Advertiser> advertisers = advertiserController.findAllAdvertisers();
        assertNotNull(advertisers);
        assertEquals(4, advertisers.size());
        expAdv.setId(4L);
        expAdv.setVersion(0L);
        assertEquals(expAdv.hashCode(), advertiser.hashCode());
        assertEquals(expAdv, advertiser);
        advertiserController.deleteAdvertiserById(4L);
        advertisers = advertiserController.findAllAdvertisers();
        assertNotNull(advertisers);
        assertEquals(3, advertisers.size());
    }

    @Test
    @FlywayTest
    public void testUpdateAdvertiser() {
        Advertiser expAdv = new Advertiser();
        expAdv.setContactName("Jackson");
        expAdv.setName("M&M");
        expAdv.setCreditLimit(new BigDecimal("1.00"));
        Advertiser advertiser = advertiserController.updateAdvertiser(1L, expAdv);
        expAdv.setId(1L);
        expAdv.setVersion(1L);
        assertEquals(expAdv.hashCode(), advertiser.hashCode());
        assertEquals(expAdv, advertiser);
    }

    @Test
    @FlywayTest
    public void testHasEnoughCredit() {
        TransactionValidity expectedValidity = new TransactionValidity();
        expectedValidity.setValid(true);
        expectedValidity.setAmount(new BigDecimal("1.00"));
	    TransactionValidity actualValidity = advertiserController.checkTransaction(2L, new BigDecimal("1.00"));
        assertEquals(expectedValidity.hashCode(), actualValidity.hashCode());
        assertEquals(expectedValidity, actualValidity);
    }

    @Test
    @FlywayTest
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
    @FlywayTest
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
    @FlywayTest
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
    @FlywayTest
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
    @FlywayTest
    public void testDeductAmountFromCreditNotFound() {
	    AdvertiserNotFoundException ex = null;
	    try {
            Deduction deduction = new Deduction();
            deduction.setAmount(new BigDecimal("30.00"));
            advertiserController.deductAmountFromCredit(30L, deduction);
        } catch (AdvertiserNotFoundException e) {
	        ex = e;
        }
        assertNotNull("Should fail with AdvertiserNotFoundException", ex);
    }

    @Test
    @FlywayTest
    public void testDeductAmountFromCreditNotEnoughCredit() {
        NotEnoughCreditException ex = null;
        try {
            Deduction deduction = new Deduction();
            deduction.setAmount(new BigDecimal("4000.00"));
            advertiserController.deductAmountFromCredit(2L, deduction);
        } catch (NotEnoughCreditException e) {
            ex = e;
        }
        assertNotNull("Should fail with NotEnoughCreditException", ex);
    }

    @Test
    @FlywayTest
    public void testDeductAmountFromCredit() {
        Deduction deduction = new Deduction();
        deduction.setAmount(new BigDecimal("3000.00"));
        Advertiser advertiser = advertiserController.deductAmountFromCredit(2L, deduction);
        assertEquals(new BigDecimal("50.00") ,advertiser.getCreditLimit());
    }

    @Test
    @FlywayTest
    public void testUpdateConcurrently(){
        Advertiser expAdv = new Advertiser();
        expAdv.setContactName("Jackson");
        expAdv.setName("M&M");
        expAdv.setCreditLimit(new BigDecimal("1.00"));
        expAdv.setId(1L);
        expAdv.setVersion(0L);
        advertiserService.updateAdvertiser(1L, expAdv, expAdv);
        ConcurrencyFailureException ex = null;
        try{
            advertiserService.updateAdvertiser(1L, expAdv, expAdv);
        } catch (ConcurrencyFailureException e) {
            ex = e;
        }
        assertNotNull(ex);
    }

    @Test
    @FlywayTest
    public void testDeductAmountFromCreditConcurrently() {
        Advertiser advertiser = advertiserController.findAdvertiserById(2L);
        advertiserService.deductAmount(2L, new BigDecimal("1.00"), advertiser);
        ConcurrencyFailureException ex = null;
        try{
            advertiserService.deductAmount(2L, new BigDecimal("1.00"), advertiser);
        } catch (ConcurrencyFailureException e) {
            ex = e;
        }
        assertNotNull(ex);
    }

    @Test
    public void testMain(){
        AdvertiseApplication.main(new String[]{});
    }


}
