package com.self.taskintervale.demoREST.repository;

import com.self.taskintervale.demoREST.entity.BookEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class BooksRepositoryImpl {

    private Map<Long, BookEntity> booksMap = new HashMap<>();

    private static Long countId = 3L; // искусственный счётчик, стартовое количество элементов в booksMap = 3 (так как
                                      // в booksMap заранее добавляем 3 элемента

    {
        booksMap.put(1L, new BookEntity(1L, 1234567891234L, "Безумство человека", "Малышева",
                230, 0.255, BigDecimal.valueOf(120.15)));
        booksMap.put(2L, new BookEntity(2L, 1114567891234L, "Русский язык", "Бортник",
                130, 1.550, BigDecimal.valueOf(10.55)));
        booksMap.put(3L, new BookEntity(3L, 1222567891234L, "Очистные сооружения", "Соколов",
                50, 0.890, BigDecimal.valueOf(730.70)));
    }

    public Map<Long, BookEntity> getBooksMap() {
        return booksMap;
    }

    public void save(BookEntity bookEntity) {
        countId = countId + 1;
        bookEntity.setId(countId);
        booksMap.put(countId, bookEntity);
    }

    public void updateBook(BookEntity bookEntity) {
        booksMap.put(bookEntity.getId(), bookEntity);
    }

    public void delete(Long id) {
        booksMap.remove(id);
    }

    public BookEntity getBookById(Long id) {
        return booksMap.get(id);
    }

    // возвращает True если ISBN передаваемой книги book совпадает с ISBN книги уже имеющейся в каталоге
    public boolean containsISBN(BookEntity bookEntity) {
        return booksMap.values().stream().anyMatch(b -> bookEntity.getISBN().equals(b.getISBN()));
    }

    // возвращает True если ISBN книги book (передаваемой в параметре метода), совпадает с ISBN книги уже имеющейся
    // в каталоге (ситуация когда при редактировании книги, мы ввели ISBN который уже присвоен другой книге)
    public boolean otherBookContainsISBN(BookEntity bookEntity) {
        return booksMap.values().stream().
                filter(b -> !b.getId().equals(bookEntity.getId())).
                anyMatch(b -> bookEntity.getISBN().equals(b.getISBN()));
    }

    public boolean containsId(BookEntity bookEntity) {
        return booksMap.containsKey(bookEntity.getId());
    }
}
