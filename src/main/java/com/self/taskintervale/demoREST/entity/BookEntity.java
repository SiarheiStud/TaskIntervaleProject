package com.self.taskintervale.demoREST.entity;

import com.self.taskintervale.demoREST.violation.CountDigits;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Objects;


public class BookEntity {

    private Long id;

    @CountDigits(message = "ISBN должно состоять из 13 цифр.") // Самопальная аннатация валидирующая то чтобы ISBN
                                                               // состоял ровно из 13 цифр
    @NotNull(message = "Значение ISBN не должно быть null.")
    @Positive(message = "Число ISBN не может быть отрицательным.")
    private Long ISBN; //уникальный 13-ти значный номер состоящий из цифр


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

    public BookEntity() {
    }

    public BookEntity(Long id, Long ISBN, String title, String author, int numberOfPages, double weight, BigDecimal price) {
        this.id = id;
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.numberOfPages = numberOfPages;
        this.weight = weight;
        this.price = price;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setISBN(Long ISBN) {
        this.ISBN = ISBN;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public Long getISBN() {
        return ISBN;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public double getWeight() {
        return weight;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", ISBN=" + ISBN +
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
        BookEntity bookEntity = (BookEntity) o;
        return numberOfPages == bookEntity.numberOfPages && Double.compare(bookEntity.weight, weight) == 0 && Objects.equals(id, bookEntity.id) && Objects.equals(ISBN, bookEntity.ISBN) && Objects.equals(title, bookEntity.title) && Objects.equals(author, bookEntity.author) && Objects.equals(price, bookEntity.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ISBN, title, author, numberOfPages, weight, price);
    }

}
