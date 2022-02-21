package com.self.taskintervale.demoREST.external.bank.Model;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import java.util.List;

public class RatesCurrencyList {

    @JsonProperty("rates")
    @Valid
    private List<RateСurrency> rateСurrencies;

    public List<RateСurrency> getRates() {
        return rateСurrencies;
    }

    public void setRates(List<RateСurrency> rateСurrencies) {
        this.rateСurrencies = rateСurrencies;
    }

    public List<RateСurrency> getRateСurrencies() {
        return rateСurrencies;
    }

    public void setRateСurrencies(List<RateСurrency> rateСurrencies) {
        this.rateСurrencies = rateСurrencies;
    }
}
