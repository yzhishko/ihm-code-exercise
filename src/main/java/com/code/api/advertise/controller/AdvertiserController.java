package com.code.api.advertise.controller;

import com.code.api.advertise.model.Advertiser;
import com.code.api.advertise.model.Deduction;
import com.code.api.advertise.model.TransactionValidity;
import com.code.api.advertise.service.AdvertiserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.Collection;

@RestController
@RequestMapping("advertiser/api/v1")
@Validated
public class AdvertiserController {

    @Autowired
    private AdvertiserService advertiserService;

    @GetMapping(value = "/advertisers", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<Advertiser> findAllAdvertisers() {
        return advertiserService.findAllAdvertisers();
    }

    @GetMapping(value = "/advertisers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Advertiser findAdvertiserById(@PathVariable("id") @Positive Long id) {
        return advertiserService.findAdvertiserById(id);
    }

    @GetMapping(value = "/query/advertisers", produces = MediaType.APPLICATION_JSON_VALUE)
    public Advertiser findAdvertiserByName(@RequestParam String name) {
        return advertiserService.findAdvertiserByName(name);
    }

    @PostMapping(value = "/advertisers", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Advertiser addAdvertiser(@Valid @RequestBody Advertiser advertiser) {
        return advertiserService.addAdvertiser(advertiser);
    }

    @PutMapping(value = "/advertisers/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Advertiser updateAdvertiser(@PathVariable("id") @Positive Long id,
                                           @Valid @RequestBody Advertiser advertiser) {
        return advertiserService.updateAdvertiser(id, advertiser, advertiserService.findAdvertiserById(id));
    }

    @GetMapping(value = "/advertisers/{id}/check_transaction", produces = MediaType.APPLICATION_JSON_VALUE)
    public TransactionValidity checkTransaction(@PathVariable("id") @Positive Long id,
                                               @RequestParam @PositiveOrZero @Digits(integer = 20, fraction = 2) BigDecimal order) {
        return advertiserService.hasEnoughCredit(id, order);
    }

    @DeleteMapping(value = "/advertisers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteAdvertiserById(@PathVariable("id") @Positive Long id) {
        advertiserService.deleteAdvertiserById(id);
    }

    @PostMapping(value = "/advertisers/{id}/deduct", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Advertiser deductAmountFromCredit(@PathVariable("id") @Positive Long id, @Valid @RequestBody Deduction deduction) {
        return advertiserService.deductAmount(id, deduction.getAmount(), findAdvertiserById(id));
    }

}
