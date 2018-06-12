package com.code.api.advertise.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public @Data class TransactionValidity {

    @NotNull
    @Digits(integer = 20, fraction = 2)
    @PositiveOrZero
    @Getter @Setter private BigDecimal amount;

    @NotNull
    @Getter @Setter private Boolean valid;

}
