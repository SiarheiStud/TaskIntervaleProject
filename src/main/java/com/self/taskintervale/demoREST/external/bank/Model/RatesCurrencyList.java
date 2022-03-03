package com.self.taskintervale.demoREST.external.bank.Model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.Valid;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RatesCurrencyList {

    @JsonProperty("rates")
    @Valid
    private List<RateСurrency> ratesCurrencyFromBank;

}

