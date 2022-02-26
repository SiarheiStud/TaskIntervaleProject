package com.self.taskintervale.demoREST.services;

import com.self.taskintervale.demoREST.entity.BookEntity;
import com.self.taskintervale.demoREST.entity.dto.BookDTO;
import com.self.taskintervale.demoREST.entity.dto.BookListDTO;
import com.self.taskintervale.demoREST.exeptions.BookNotFoundException;
import com.self.taskintervale.demoREST.exeptions.ISBNAlreadyExistsException;
import com.self.taskintervale.demoREST.repository.BooksRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public BookListDTO getBooks() {

        List<BookEntity> bookEntityList = booksRepositoryImpl.getBooks();

        BookListDTO bookListDTO = new BookListDTO();
        bookListDTO.setBookDtoList(bookEntityList.stream()
                .map(BookService::bookEntityIntoDTO)
                .collect(Collectors.toList()));

        return bookListDTO;
    }

    public void saveBook(BookDTO bookDTO) throws ISBNAlreadyExistsException {

        BookEntity bookEntity = BookService.bookDTOIntoEntity(bookDTO);
        if (booksRepositoryImpl.isContainsISBN(bookEntity)) {
            throw new ISBNAlreadyExistsException("Не корректный ISBN! Книга с ISBN = " + bookEntity.getISBN() +
                    " уже содержится в каталоге.");
        }

        booksRepositoryImpl.saveBook(bookEntity);
    }

    public void updateBook(BookDTO bookDTO, Long id) throws ISBNAlreadyExistsException, BookNotFoundException {

        BookEntity bookEntity = BookService.bookDTOIntoEntity(bookDTO);
        bookEntity.setId(id);
        if (booksRepositoryImpl.otherBookContainsISBN(bookEntity)) {
            throw new ISBNAlreadyExistsException("Обновить данные книги не удалось! ISBN завизирован! Книга с ISBN = "
                    + bookEntity.getISBN() + " уже содержится в базе.");
        }
        if (!booksRepositoryImpl.isContainsId(id)) {
            throw new BookNotFoundException("Редактируемая книга не найдена в базе. Id книги = " + bookEntity.getId() +
                    "(книги с этим ID нет в каталоге книг).");
        }

        booksRepositoryImpl.updateBook(bookEntity);
    }

    public void deleteBook(Long id) throws BookNotFoundException {

        if (!booksRepositoryImpl.isContainsId(id)) {
            throw new BookNotFoundException("Процедура удаления не произведена! Книга c id = " + id + " не найдена.");
        }
        booksRepositoryImpl.deleteBook(id);
    }

    public List<BookEntity> getBooksByAuthorName(String authorName) {
        return booksRepositoryImpl.getBooksByAuthorName(authorName);
    }

    public List<BookEntity> getBooksByTitle(String title) {
        return booksRepositoryImpl.getBooksByAuthorName(title);
    }
}
