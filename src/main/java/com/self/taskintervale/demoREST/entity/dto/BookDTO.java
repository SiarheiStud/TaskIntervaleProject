package com.self.taskintervale.demoREST.entity.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.self.taskintervale.demoREST.jacksonutil.BookDeserializer;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Objects;

@JsonDeserialize(using = BookDeserializer.class)
public class BookDTO {


    @NotBlank(message = "ISBN не должен быть null и не должен состоять из одних пробельных символов.")
    @Pattern(regexp = "[0-9]{13}", message = "ISBN должен состоять из 13 цифр и иметь " +
            "формат 1351735147093")
    private String ISBN; //уникальный 13-ти значный номер состоящий из цифр, формат ISBN 1351735147093


    @NotBlank(message = "Название книги не должно быть null и не должно состоять из одних пробельных символов.")
    @Size(min = 1, max = 200, message = "Количество букв в названии должно быть от 1 до 200.")
    private String title;


    @NotBlank(message = "Фамилия автора не должна быть null и не должна состоять из одних пробельных символов.")
    @Size(min = 2, max = 20, message = "Количество букв в фамилии должно быть от 2 до 20.")
    @Pattern(regexp = "^$|[a-zA-Zа-яА-яё]+$", message = "Фамилия не должна содержать цифры, " +
            "специальные символы и пробелы.")
    private String author;


    @Min(value = 5, message = "Минимальное количество страниц в книге 5.")
    @Max(value = 10000, message = "Максимальное количество страниц в книге 10000.")
    private int numberOfPages;


    @DecimalMin(value = "0.001", message = "Вес книги должн быть больше 0.001 кг.")
    @Digits(integer = 2, fraction = 3, message = "Вес книги должен быть меньше 99.999 кг " +
            "Максимальное количество цифр после запятой 3.")
    private double weight;


    @NotNull(message = "Цена не должна быть null.")
    @DecimalMin(value = "0.01", message = "Цена должна быть больше 0.01 .")
    @Digits(integer = 7, fraction = 2, message = "Максимальная цена книги 9999999.99 . " +
            "Максимальное количество цифр после запятой 2.")
    private BigDecimal price;


    public BookDTO() {
    }

    public BookDTO(String ISBN, String title, String author, int numberOfPages, double weight, BigDecimal price) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.numberOfPages = numberOfPages;
        this.weight = weight;
        this.price = price;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

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

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "BookDTO{" +
                "ISBN=" + ISBN +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", numberOfPages=" + numberOfPages +
                ", weight=" + weight +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookDTO bookDTO = (BookDTO) o;
        return numberOfPages == bookDTO.numberOfPages && Double.compare(bookDTO.weight, weight) == 0 && Objects.equals(ISBN, bookDTO.ISBN) && Objects.equals(title, bookDTO.title) && Objects.equals(author, bookDTO.author) && Objects.equals(price, bookDTO.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ISBN, title, author, numberOfPages, weight, price);
    }
}
