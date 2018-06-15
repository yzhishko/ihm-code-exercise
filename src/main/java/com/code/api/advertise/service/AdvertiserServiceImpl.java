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
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;

@Component
@MappedTypes({Advertiser.class, TransactionValidity.class})
@MapperScan("com.code.api.advertise.mapper")
@Transactional(readOnly = true)
public class AdvertiserServiceImpl implements AdvertiserService {

    @Autowired
    private AdvertiserMapper advertiserMapper;

    public Collection<Advertiser> findAllAdvertisers() {
        return advertiserMapper.findAll();
    }

    @Transactional
    public Advertiser addAdvertiser(Advertiser advertiser) {
        try {
            advertiserMapper.addAdvertiser(advertiser);
        } catch (Exception e) {
            if (e.getMessage().contains("Unique index or primary key violation")) {
                throw new AdvertiserAlreadyExistsException("Found advertiser with the same name = "
                        + advertiser.getName());
            }
            throw e;
        }
        return advertiser;
    }

    public Advertiser findAdvertiserById(Long id) {
        return advertiserMapper.findAdvertiserByID(id);
    }

    public Advertiser findAdvertiserByName(String name) {
        return advertiserMapper.findAdvertiserByName(name);
    }

    @Transactional
    public Advertiser updateAdvertiser(Long id, Advertiser advertiser, Advertiser existAdv) {
        if (existAdv == null) throw new AdvertiserNotFoundException("Advertiser with id = " + id);
        advertiser.setId(id);
        advertiser.setVersion(existAdv.getVersion());
        try {
            if (advertiserMapper.updateAdvertiser(advertiser)) {
                return advertiser;
            } else {
                throw new ConcurrencyFailureException("Someone have just updated this record. Try again");
            }
        } catch (Exception e) {
            if (e.getMessage().contains("Unique index or primary key violation")) {
                throw new AdvertiserAlreadyExistsException("Found advertiser with the same name = " + advertiser.getName());
            }
            throw e;
        }
    }

    public TransactionValidity hasEnoughCredit(Long id, BigDecimal order) {
        TransactionValidity validity = advertiserMapper.hasEnoughCreditById(id, order);
        if (validity == null) throw new AdvertiserNotFoundException("Advertiser with id = " + id);
        return validity;
    }

    @Transactional
    public void deleteAdvertiserById(Long id) {
        advertiserMapper.deleteAdvertiserbyId(id);
    }

    @Transactional
    public Advertiser deductAmount(Long id, BigDecimal amount, Advertiser advertiser) {
        if (advertiser == null) throw new AdvertiserNotFoundException("Advertiser with id = " + id);
        if (advertiser.getCreditLimit().compareTo(amount) < 0) {
            throw new NotEnoughCreditException("Credit limit is " + advertiser.getCreditLimit());
        }
        advertiser.setCreditLimit(advertiser.getCreditLimit().subtract(amount));
        updateAdvertiser(id, advertiser, advertiser);
        return advertiser;
    }

}
