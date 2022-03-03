package com.self.taskintervale.demoREST.external.bank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class BankDTO {

    private String title;
    private String author;
    @JsonIgnore
    private BigDecimal price;
    private String dateActualPrice;
    private Map<String, BigDecimal> priceInCurrency;

}
