package com.self.taskintervale.demoREST.external.bank.Model;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import java.util.List;

public class RatesCurrencyList {

    @JsonProperty("rates")
    @Valid
    private List<RateСurrency> ratesCurrencyFromBank;

    public List<RateСurrency> getRatesCurrencyFromBank() {
        return ratesCurrencyFromBank;
    }

    public void setRatesCurrencyFromBank(List<RateСurrency> ratesCurrencyFromBank) {
        this.ratesCurrencyFromBank = ratesCurrencyFromBank;
    }
}

