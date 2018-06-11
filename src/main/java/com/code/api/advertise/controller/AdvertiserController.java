package com.code.api.advertise.controller;

import com.code.api.advertise.model.Advertiser;
import com.code.api.advertise.model.TransactionValidity;
import com.code.api.advertise.service.AdvertiserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collection;

@RestController
@RequestMapping("advertiser/api/v1")
public class AdvertiserController {

    @Autowired
    private AdvertiserService advertiserService;

    @GetMapping("/advertisers")
    public Collection<Advertiser> findAllAdvertisers() {
        return advertiserService.findAllAdvertisers();
    }

    @GetMapping("/advertisers/{id}")
    public Advertiser findAdvertiserById(@PathVariable("id") Long id) {
        return advertiserService.findAdvertiserById(id);
    }

    @GetMapping("/query/advertisers")
    public Advertiser findAdvertiserByName(@RequestParam String name) {
        return advertiserService.findAdvertiserByName(name);
    }

    @PostMapping(value = "/advertisers", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addAdvertiser(@RequestBody Advertiser advertiser) {
        advertiserService.addAdvertiser(advertiser);
    }

    @PutMapping("/advertisers/{id}")
    public void updateAdvertiser(@PathVariable("id") Long id, @RequestBody Advertiser advertiser) {
        advertiserService.updateAdvertiser(id, advertiser);
    }

    @GetMapping("/advertisers/{id}/check_transaction")
    public TransactionValidity hasEnoughCredit(@PathVariable("id") Long id, @RequestParam BigDecimal order) {
        return advertiserService.hasEnoughCredit(id, order);
    }

    @DeleteMapping("/advertisers/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        advertiserService.deleteAdvertiserById(id);
    }

}
