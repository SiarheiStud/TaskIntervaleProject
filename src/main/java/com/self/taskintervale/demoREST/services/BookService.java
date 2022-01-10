package com.self.taskintervale.demoREST.services;

import com.self.taskintervale.demoREST.entity.BookEntity;
import com.self.taskintervale.demoREST.entity.dto.BookDTO;
import com.self.taskintervale.demoREST.exeptions.BookNotFoundException;
import com.self.taskintervale.demoREST.exeptions.ISBNAlreadyExistsException;
import com.self.taskintervale.demoREST.repository.BooksRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BookService {

    private final BooksRepositoryImpl booksRepositoryImpl;

    public BookService(BooksRepositoryImpl booksRepositoryImpl) {
        this.booksRepositoryImpl = booksRepositoryImpl;
    }

    //преобразование BookEntity в BookDTO
    public static BookDTO bookEntityIntoDTO(BookEntity bookEntity) {

        BookDTO bookDTO = new BookDTO();
        bookDTO.setISBN(bookEntity.getISBN());
        bookDTO.setTitle(bookEntity.getTitle());
        bookDTO.setAuthor(bookEntity.getAuthor());
        bookDTO.setNumberOfPages(bookEntity.getNumberOfPages());
        bookDTO.setWeight(bookEntity.getWeight());
        bookDTO.setPrice(bookEntity.getPrice());

        return bookDTO;
    }


    //преобразование BookDTO в BookEntity
    public static BookEntity bookDTOIntoEntity(BookDTO bookDTO) {

        BookEntity bookEntity = new BookEntity();
        bookEntity.setISBN(bookDTO.getISBN());
        bookEntity.setTitle(bookDTO.getTitle());
        bookEntity.setAuthor(bookDTO.getAuthor());
        bookEntity.setNumberOfPages(bookDTO.getNumberOfPages());
        bookEntity.setWeight(bookDTO.getWeight());
        bookEntity.setPrice(bookDTO.getPrice());

        return bookEntity;
    }


    public Map<Long, BookDTO> getBooks() {

        Map<Long, BookEntity> bookEntityMap = booksRepositoryImpl.getBooksMap();
        Map<Long, BookDTO> bookDTOMap = new HashMap<>();
        bookEntityMap.forEach((key, bookEntity) -> bookDTOMap.put(key, BookService.bookEntityIntoDTO(bookEntity)));

        return bookDTOMap;
    }


    public void saveBook(BookDTO bookDTO) throws ISBNAlreadyExistsException {

        BookEntity bookEntity = BookService.bookDTOIntoEntity(bookDTO);
        if (booksRepositoryImpl.containsISBN(bookEntity)) {
            throw new ISBNAlreadyExistsException("Не корректный ISBN! Книга с ISBN = " + bookEntity.getISBN() +
                    " уже содержится в каталоге.");
        }
        booksRepositoryImpl.save(bookEntity);
    }


    public void updateBook(BookDTO bookDTO, Long id) throws ISBNAlreadyExistsException, BookNotFoundException {

        BookEntity bookEntity = BookService.bookDTOIntoEntity(bookDTO);
        bookEntity.setId(id);
        if (booksRepositoryImpl.otherBookContainsISBN(bookEntity)) {
            throw new ISBNAlreadyExistsException("Не корректный ISBN! Книга с ISBN = " + bookEntity.getISBN() +
                    " уже содержится в каталоге.");
        }
        if (!booksRepositoryImpl.containsId(bookEntity)) {
            throw new BookNotFoundException("Редактируемая книга не найдена в каталоге. Id книги = " + bookEntity.getId() +
                    "(книги с этим ID нет в каталоге книг)");
        }
        booksRepositoryImpl.updateBook(bookEntity);
    }


    public void deleteBook(Long id) throws BookNotFoundException {

        if (booksRepositoryImpl.getBookById(id) == null) {
            throw new BookNotFoundException("Книга c id = " + id + " не найдена");
        }
        booksRepositoryImpl.delete(id);
    }


    public BookEntity findBookById(Long id) {
        return booksRepositoryImpl.getBookById(id);
    }

}
