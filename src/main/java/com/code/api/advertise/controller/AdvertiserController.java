package com.code.api.advertise.controller;

import com.code.api.advertise.model.Advertiser;
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

    @GetMapping("/advertisers")
    public Collection<Advertiser> findAllAdvertisers() {
        return advertiserService.findAllAdvertisers();
    }

    @GetMapping("/advertisers/{id}")
    public Advertiser findAdvertiserById(@PathVariable("id") @Positive Long id) {
        return advertiserService.findAdvertiserById(id);
    }

    @GetMapping("/query/advertisers")
    public Advertiser findAdvertiserByName(@RequestParam String name) {
        return advertiserService.findAdvertiserByName(name);
    }

    @PostMapping(value = "/advertisers", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addAdvertiser(@Valid @RequestBody Advertiser advertiser) {
        advertiserService.addAdvertiser(advertiser);
    }

    @PutMapping("/advertisers/{id}")
    public void updateAdvertiser(@PathVariable("id") @Positive Long id,
                                 @Valid @RequestBody Advertiser advertiser) {
        advertiserService.updateAdvertiser(id, advertiser);
    }

    @RequestMapping(value = "/advertisers/{id}/check_transaction", method = RequestMethod.GET)
    public TransactionValidity checkTransaction(@PathVariable("id") @Positive Long id,
                                               @RequestParam @PositiveOrZero @Digits(integer = 20, fraction = 2) BigDecimal order) {
        return advertiserService.hasEnoughCredit(id, order);
    }

    @DeleteMapping("/advertisers/{id}")
    public void deleteById(@PathVariable("id") @Positive Long id) {
        advertiserService.deleteAdvertiserById(id);
    }

}
