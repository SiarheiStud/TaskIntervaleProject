package com.self.taskintervale.demoREST.external.bank;


import com.self.taskintervale.demoREST.entity.BookEntity;
import com.self.taskintervale.demoREST.exeptions.BookNotFoundException;
import com.self.taskintervale.demoREST.external.bank.Model.RateСurrency;
import com.self.taskintervale.demoREST.external.bank.Model.RatesCurrencyList;
import com.self.taskintervale.demoREST.repository.BooksRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BankService {

    private final BooksRepositoryImpl booksRepositoryImpl;
    private final RestTemplate restTemplate;

    private final String BASE_URL = "https://ibapi.alfabank.by:8273";
    private final String URL_GET_RATES = BASE_URL + "/partner/1.0.1/public/rates";

    @Autowired
    public BankService(BooksRepositoryImpl booksRepositoryImpl, RestTemplate restTemplate) {
        this.booksRepositoryImpl = booksRepositoryImpl;
        this.restTemplate = restTemplate;
    }


    public List<BankDTO> getPrice(String title) throws BookNotFoundException {

        List<BookEntity> bookEntityList = booksRepositoryImpl.getBooksByTitle(title);

        if (CollectionUtils.isEmpty(bookEntityList)) {
            throw new BookNotFoundException("Книга с названием: \"" + title + "\" не найдена в базе.");

        }

        List<BankDTO> bankDTOList = bookEntityList.stream()
                .map(BankService::bookEntityIntoBankDTO)
                .collect(Collectors.toList());

        //Получаем актуальные курсы валют
        RatesCurrencyList ratesCurrencyList = getRateList();

        //Проставляем цену книги в других валютач
        bankDTOList.forEach(r -> BankService.setDifferentPriceCurrency(ratesCurrencyList, r));

        return bankDTOList;

    }

    //Метод возвращает курсы основных валют
    public RatesCurrencyList getRateList() {

        return restTemplate.getForObject(URL_GET_RATES, RatesCurrencyList.class);
    }


    //Метод устанвалвивает цену в основных валютах
    private static BankDTO setDifferentPriceCurrency(RatesCurrencyList ratesCurrencyList, BankDTO bankDTO) {

        Map<String, BigDecimal> priceInCurrencyMap = new HashMap<>();
        Map<String, String> isoMap = Map.of("RUB", "российских рублей",
                "USD", "доллар США",
                "EUR", "евро");

        //Для каждой валюты находим соответствующий актуальный курс, и по нему рассчитываем стоимость книги
        for (Map.Entry<String, String> entry : isoMap.entrySet()) {
            Optional<RateСurrency> rateOptional = ratesCurrencyList.getRates().stream()
                    .filter(r -> Objects.equals(r.getName(), entry.getValue()))
                    .findAny();

            if (rateOptional.isPresent()) {
                RateСurrency rateСurrency = rateOptional.get();
                BigDecimal resultPrice = bankDTO.getPrice()
                        .divide(rateСurrency.getBuyRate(), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(rateСurrency.getQuantity()));

                priceInCurrencyMap.put(entry.getKey(), resultPrice);
                bankDTO.setDateActualPrice(rateСurrency.getDate());
            }
        }

        priceInCurrencyMap.put("BYN", bankDTO.getPrice());
        bankDTO.setPriceInCurrency(priceInCurrencyMap);

        return bankDTO;
    }

    //преобразование BookEntity в BankDTO
    private static BankDTO bookEntityIntoBankDTO(BookEntity bookEntity) {

        BankDTO bankDTO = new BankDTO();
        bankDTO.setTitle(bookEntity.getTitle());
        bankDTO.setAuthor(bookEntity.getAuthor());
        bankDTO.setPrice(bookEntity.getPrice());

        return bankDTO;
    }
}
