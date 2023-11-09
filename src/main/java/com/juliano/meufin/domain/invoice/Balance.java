package com.juliano.meufin.domain.invoice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Balance {
    private BigDecimal income;
    private BigDecimal expense;
    private BigDecimal total;

}
