package com.code.api.advertise.service;

import com.code.api.advertise.model.Advertiser;
import com.code.api.advertise.mapper.AdvertiserMapper;
import com.code.api.advertise.model.TransactionValidity;
import org.apache.ibatis.type.MappedTypes;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void addAdvertiser(Advertiser advertiser) {
        advertiserMapper.addAdvertiser(advertiser);
    }

    public Advertiser findAdvertiserById(Long id) {
        return advertiserMapper.findAdvertiserByID(id);
    }

    public Advertiser findAdvertiserByName(String name) {
        return advertiserMapper.findAdvertiserByName(name);
    }

    public void updateAdvertiser(Long id, Advertiser advertiser) {
        advertiser.setId(id);
        advertiserMapper.updateAdvertiser(advertiser);
    }

    public TransactionValidity hasEnoughCredit(Long id, BigDecimal order) {
        return advertiserMapper.hasEnoughCreditById(id, order);
    }

    public void deleteAdvertiserById(Long id) {
        advertiserMapper.deleteAdvertiserbyId(id);
    }

}
