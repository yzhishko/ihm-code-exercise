package com.code.api.advertise.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public @Data class Advertiser {

    @Getter @Setter private Long id;

    @Getter @Setter private String name;

    @Getter @Setter private String contactName;

    @Getter @Setter private BigDecimal creditLimit;

}
