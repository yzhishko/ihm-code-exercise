package com.code.api.advertise.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public @Data class Deduction {

    @NotNull
    @Digits(integer = 20, fraction = 2)
    @PositiveOrZero
    @Getter @Setter private BigDecimal amount;

}
