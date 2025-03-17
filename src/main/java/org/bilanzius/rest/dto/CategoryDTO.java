package org.bilanzius.rest.dto;

import java.math.BigDecimal;

public record CategoryDTO(String name, BigDecimal budget, BigDecimal amountSpent) {
}
