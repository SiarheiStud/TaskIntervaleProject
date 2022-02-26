package com.self.taskintervale.demoREST.external.bank.Model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RateСurrency {
    @JsonProperty("sellRate")
    private BigDecimal sellRate = null;

    @JsonProperty("sellIso")
    private String sellIso = null;

    @JsonProperty("sellCode")
    private Integer sellCode = null;

    @JsonProperty("buyRate")
    private BigDecimal buyRate = null;

    @JsonProperty("buyIso")
    private String buyIso = null;

    @JsonProperty("buyCode")
    private Integer buyCode = null;

    @JsonProperty("quantity")
    private Integer quantity = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("date")
    private String date = null;

}
