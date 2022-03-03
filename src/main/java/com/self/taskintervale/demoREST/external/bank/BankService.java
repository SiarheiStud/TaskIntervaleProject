package com.self.taskintervale.demoREST.external.bank;


import com.self.taskintervale.demoREST.entity.BookEntity;
import com.self.taskintervale.demoREST.exeptions.BankException;
import com.self.taskintervale.demoREST.exeptions.BookNotFoundException;
import com.self.taskintervale.demoREST.external.bank.Model.RateСurrency;
import com.self.taskintervale.demoREST.external.bank.Model.RatesCurrencyList;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BankService {

    private final RestTemplate restTemplate;

    private final String URL_GET_RATES = "/partner/1.0.1/public/rates";

    public BankService(@Qualifier(value = "bankRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<BankDTO> getPrice(String title, List<BookEntity> bookEntityList)
            throws BookNotFoundException, BankException {

        if (bookEntityList.isEmpty()) {
            throw new BookNotFoundException("Книга с названием: \"" + title + "\" не найдена в базе.");
        }

        List<BankDTO> bankDTOList = bookEntityList.stream()
                .map(EntityBankDTOMapper::bookEntityIntoBankDTO)
                .collect(Collectors.toList());

        //Получаем актуальные курсы валют
        RatesCurrencyList ratesCurrencyList = getRateList();
        if(ratesCurrencyList == null){
            throw new BankException("Не корректные данные от ibapi.alfabank.by:8273");
        }

        //Проставляем цену книги в других валютаx
        bankDTOList.forEach(r -> BankService.setDifferentPriceCurrency(ratesCurrencyList, r));

        return bankDTOList;
    }

    //Метод возвращает курсы основных валют
    public RatesCurrencyList getRateList() {

        return restTemplate.getForObject(URL_GET_RATES, RatesCurrencyList.class);
    }

    //Метод устанвалвивает цену в основных валютах
    private static void setDifferentPriceCurrency(RatesCurrencyList ratesCurrencyList, BankDTO bankDTO) {

        Map<String, BigDecimal> priceInCurrencyMap = new HashMap<>();
        List<RateСurrency> ratesCurrencyListFromBank = ratesCurrencyList.getRatesCurrencyFromBank().stream()
                .filter(rate -> rate.getName() != null)             // Отфильтровываем конверсии не относящиеся к BYN
                .collect(Collectors.toList());

        for (RateСurrency rate : ratesCurrencyListFromBank) {

            BigDecimal resultPrice = bankDTO.getPrice()
                    .divide(rate.getBuyRate(), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(rate.getQuantity()));

            priceInCurrencyMap.put(rate.getSellIso(), resultPrice);
            bankDTO.setDateActualPrice(rate.getDate());
        }

        priceInCurrencyMap.put("BYN", bankDTO.getPrice());
        bankDTO.setPriceInCurrency(priceInCurrencyMap);
    }
}
