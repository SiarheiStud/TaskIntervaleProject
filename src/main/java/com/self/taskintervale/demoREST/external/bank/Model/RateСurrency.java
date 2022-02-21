package com.self.taskintervale.demoREST.external.bank.Model;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

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


    public BigDecimal getSellRate() {
        return sellRate;
    }

    public void setSellRate(BigDecimal sellRate) {
        this.sellRate = sellRate;
    }

    public String getSellIso() {
        return sellIso;
    }

    public void setSellIso(String sellIso) {
        this.sellIso = sellIso;
    }

    public Integer getSellCode() {
        return sellCode;
    }

    public void setSellCode(Integer sellCode) {
        this.sellCode = sellCode;
    }

    public BigDecimal getBuyRate() {
        return buyRate;
    }

    public void setBuyRate(BigDecimal buyRate) {
        this.buyRate = buyRate;
    }

    public String getBuyIso() {
        return buyIso;
    }

    public void setBuyIso(String buyIso) {
        this.buyIso = buyIso;
    }

    public Integer getBuyCode() {
        return buyCode;
    }

    public void setBuyCode(Integer buyCode) {
        this.buyCode = buyCode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
