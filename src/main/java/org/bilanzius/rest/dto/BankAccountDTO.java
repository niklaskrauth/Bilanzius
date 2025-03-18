package org.bilanzius.rest.dto;


import java.math.BigDecimal;

public record BankAccountDTO(
        String name,
        BigDecimal balance)
{
}
