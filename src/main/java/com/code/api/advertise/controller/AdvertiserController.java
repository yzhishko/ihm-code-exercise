package com.code.api.advertise.controller;

import com.code.api.advertise.model.Advertiser;
import com.code.api.advertise.model.Deduction;
import com.code.api.advertise.model.TransactionValidity;
import com.code.api.advertise.service.AdvertiserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "Advertiser API", description = "RESTful endpoints to manage advertisers")
public class AdvertiserController {

    @Autowired
    private AdvertiserService advertiserService;

    @ApiOperation("Get list of all available advertisers")
    @GetMapping(value = "/advertisers", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<Advertiser> findAllAdvertisers() {
        return advertiserService.findAllAdvertisers();
    }

    @ApiOperation("Find advertiser by id")
    @GetMapping(value = "/advertisers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Advertiser findAdvertiserById(@PathVariable("id") @Positive Long id) {
        return advertiserService.findAdvertiserById(id);
    }

    @ApiOperation("Find advertiser by name")
    @GetMapping(value = "/query/advertisers", produces = MediaType.APPLICATION_JSON_VALUE)
    public Advertiser findAdvertiserByName(@RequestParam String name) {
        return advertiserService.findAdvertiserByName(name);
    }

    @ApiOperation("Create advertiser. Property 'id' of request body will be skipped")
    @PostMapping(value = "/advertisers", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Advertiser addAdvertiser(@Valid @RequestBody Advertiser advertiser) {
        return advertiserService.addAdvertiser(advertiser);
    }

    @ApiOperation("Update advertiser. Property 'id' of request body will be skipped")
    @PutMapping(value = "/advertisers/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Advertiser updateAdvertiser(@PathVariable("id") @Positive Long id,
                                           @Valid @RequestBody Advertiser advertiser) {
        return advertiserService.updateAdvertiser(id, advertiser, advertiserService.findAdvertiserById(id));
    }

    @ApiOperation("Validate if advertiser may perform transaction")
    @GetMapping(value = "/advertisers/{id}/check_transaction", produces = MediaType.APPLICATION_JSON_VALUE)
    public TransactionValidity checkTransaction(@PathVariable("id") @Positive Long id,
                                               @RequestParam @PositiveOrZero @Digits(integer = 20, fraction = 2) BigDecimal order) {
        return advertiserService.hasEnoughCredit(id, order);
    }

    @ApiOperation("Delete advertiser by id")
    @DeleteMapping(value = "/advertisers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteAdvertiserById(@PathVariable("id") @Positive Long id) {
        advertiserService.deleteAdvertiserById(id);
    }

    @ApiOperation("Deduct credit from account")
    @PostMapping(value = "/advertisers/{id}/deduct", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Advertiser deductAmountFromCredit(@PathVariable("id") @Positive Long id, @Valid @RequestBody Deduction deduction) {
        return advertiserService.deductAmount(id, deduction.getAmount(), findAdvertiserById(id));
    }

}
