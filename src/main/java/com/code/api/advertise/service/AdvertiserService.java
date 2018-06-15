package com.code.api.advertise.service;

import com.code.api.advertise.model.Advertiser;
import com.code.api.advertise.model.TransactionValidity;

import java.math.BigDecimal;
import java.util.Collection;

public interface AdvertiserService {


    Collection<Advertiser> findAllAdvertisers();

    Advertiser addAdvertiser(Advertiser advertiser);

    Advertiser findAdvertiserById(Long id);

    Advertiser findAdvertiserByName(String name);

    Advertiser updateAdvertiser(Long id, Advertiser advertiser, Advertiser existAdvertiser);

    TransactionValidity hasEnoughCredit(Long id, BigDecimal order);

    void deleteAdvertiserById(Long id);

    Advertiser deductAmount(Long id, BigDecimal amount, Advertiser advertiser);

}
