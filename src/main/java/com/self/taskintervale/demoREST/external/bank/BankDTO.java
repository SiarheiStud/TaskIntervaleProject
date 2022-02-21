package com.self.taskintervale.demoREST.external.bank;

import com.fasterxml.jackson.annotation.JsonIgnore;


import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

public class BankDTO {

    private String title;
    private String author;
    @JsonIgnore
    private BigDecimal price;

    private String dateActualPrice;
    private Map<String, BigDecimal> priceInCurrency;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDateActualPrice() {
        return dateActualPrice;
    }

    public void setDateActualPrice(String dateActualPrice) {
        this.dateActualPrice = dateActualPrice;
    }

    public Map<String, BigDecimal> getPriceInCurrency() {
        return priceInCurrency;
    }

    public void setPriceInCurrency(Map<String, BigDecimal> priceInCurrency) {
        this.priceInCurrency = priceInCurrency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankDTO bankDTO = (BankDTO) o;
        return Objects.equals(title, bankDTO.title) && Objects.equals(author, bankDTO.author) && Objects.equals(price, bankDTO.price) && Objects.equals(dateActualPrice, bankDTO.dateActualPrice) && Objects.equals(priceInCurrency, bankDTO.priceInCurrency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, price, dateActualPrice, priceInCurrency);
    }

    @Override
    public String toString() {
        return "BankDTO{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                ", dateActualPrice='" + dateActualPrice + '\'' +
                ", priceInCurrency=" + priceInCurrency +
                '}';
    }
}
