package com.code.api.advertise.service;

import com.code.api.advertise.exception.AdvertiserAlreadyExistsException;
import com.code.api.advertise.exception.AdvertiserNotFoundException;
import com.code.api.advertise.exception.NotEnoughCreditException;
import com.code.api.advertise.mapper.AdvertiserMapper;
import com.code.api.advertise.model.Advertiser;
import com.code.api.advertise.model.TransactionValidity;
import org.apache.ibatis.type.MappedTypes;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collection;

@Component
@MappedTypes({Advertiser.class, TransactionValidity.class})
@MapperScan("com.code.api.advertise.mapper")
public class AdvertiserServiceImpl implements AdvertiserService {

    @Autowired
    private AdvertiserMapper advertiserMapper;

    public Collection<Advertiser> findAllAdvertisers() {
        return advertiserMapper.findAll();
    }

    public void addAdvertiser(Advertiser advertiser) {
        Advertiser adv = findAdvertiserByName(advertiser.getName());
        if (adv != null) throw new AdvertiserAlreadyExistsException("Found advertiser with the same name and id = " + adv.getId());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {

        }
        advertiserMapper.addAdvertiser(advertiser);
    }

    public Advertiser findAdvertiserById(Long id) {
        return advertiserMapper.findAdvertiserByID(id);
    }

    public Advertiser findAdvertiserByName(String name) {
        return advertiserMapper.findAdvertiserByName(name);
    }

    public void updateAdvertiser(Long id, Advertiser advertiser) {
        Advertiser adv = advertiserMapper.findAdvertiserByID(id);
        if (adv == null) throw new AdvertiserNotFoundException("Advertiser with id = " + id);
        adv = findAdvertiserByName(advertiser.getName());
        if (adv != null) throw new AdvertiserAlreadyExistsException("Found advertiser with the same name and id = " + adv.getId());
        advertiser.setId(id);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {

        }
        advertiserMapper.updateAdvertiser(advertiser);
    }

    public TransactionValidity hasEnoughCredit(Long id, BigDecimal order) {
        Advertiser adv = advertiserMapper.findAdvertiserByID(id);
        if (adv == null) throw new AdvertiserNotFoundException("Advertiser with id = " + id);
        return advertiserMapper.hasEnoughCreditById(id, order);
    }

    public void deleteAdvertiserById(Long id) {
        advertiserMapper.deleteAdvertiserbyId(id);
    }

    public Advertiser deductAmount(Long id, BigDecimal amount) {
        Advertiser advertiser = findAdvertiserById(id);
        if (advertiser == null) throw new AdvertiserNotFoundException("Advertiser with id = " + id);
        if (advertiser.getCreditLimit().compareTo(amount) < 0) {
            throw new NotEnoughCreditException("Credit limit is " + advertiser.getCreditLimit());
        }
        advertiser.setCreditLimit(advertiser.getCreditLimit().subtract(amount));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {

        }
        advertiserMapper.updateAdvertiser(advertiser);
        return advertiser;
    }

}
