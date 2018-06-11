package com.code.api.advertise.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public @Data class TransactionValidity {

    @Getter @Setter private BigDecimal amount;

    @Getter @Setter private Boolean valid;

}
