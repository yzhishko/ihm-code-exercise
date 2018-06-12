package com.code.api.advertise.service;

import com.code.api.advertise.exception.AdvertiserAlreadyExistsException;
import com.code.api.advertise.exception.AdvertiserNotFoundException;
import com.code.api.advertise.model.Advertiser;
import com.code.api.advertise.mapper.AdvertiserMapper;
import com.code.api.advertise.model.TransactionValidity;
import org.apache.ibatis.type.MappedTypes;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;

@Service
@MappedTypes({Advertiser.class, TransactionValidity.class})
@MapperScan("com.code.api.advertise.mapper")
public class AdvertiserService {

    @Autowired
    private AdvertiserMapper advertiserMapper;

    public Collection<Advertiser> findAllAdvertisers() {
        return advertiserMapper.findAll();
    }

    @Transactional
    public void addAdvertiser(Advertiser advertiser) {
        Advertiser adv = findAdvertiserByName(advertiser.getName());
        if (adv != null) throw new AdvertiserAlreadyExistsException("Found advertiser with the same name and id = " + adv.getId());
        advertiserMapper.addAdvertiser(advertiser);
    }

    public Advertiser findAdvertiserById(Long id) {
        return advertiserMapper.findAdvertiserByID(id);
    }

    public Advertiser findAdvertiserByName(String name) {
        return advertiserMapper.findAdvertiserByName(name);
    }

    @Transactional
    public void updateAdvertiser(Long id, Advertiser advertiser) {
        Advertiser adv = advertiserMapper.findAdvertiserByID(id);
        if (adv == null) throw new AdvertiserNotFoundException("Advertiser with id = " + id);
        adv = findAdvertiserByName(advertiser.getName());
        if (adv != null) throw new AdvertiserAlreadyExistsException("Found advertiser with the same name and id = " + adv.getId());
        advertiser.setId(id);
        advertiserMapper.updateAdvertiser(advertiser);
    }

    @Transactional
    public TransactionValidity hasEnoughCredit(Long id, BigDecimal order) {
        Advertiser adv = advertiserMapper.findAdvertiserByID(id);
        if (adv == null) throw new AdvertiserNotFoundException("Advertiser with id = " + id);
        return advertiserMapper.hasEnoughCreditById(id, order);
    }

    public void deleteAdvertiserById(Long id) {
        advertiserMapper.deleteAdvertiserbyId(id);
    }

}
