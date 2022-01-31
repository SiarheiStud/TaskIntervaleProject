package com.self.taskintervale.demoREST.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.self.taskintervale.demoREST.entity.BookEntity;
import com.self.taskintervale.demoREST.entity.dto.BookDTO;
import com.self.taskintervale.demoREST.entity.dto.BookOpenLibraryDTO;
import com.self.taskintervale.demoREST.exeptions.BookNotFoundException;
import com.self.taskintervale.demoREST.exeptions.ISBNAlreadyExistsException;
import com.self.taskintervale.demoREST.repository.BooksRepositoryImpl;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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


    public List<BookDTO> getBooks() {

        List<BookEntity> bookEntityList = booksRepositoryImpl.getBooks();
        List<BookDTO> bookDTOList = bookEntityList.stream().
                map(BookService::bookEntityIntoDTO).
                collect(Collectors.toList());

        return bookDTOList;
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


    public List<BookOpenLibraryDTO> getBooksByAuthorName(String authorName) {
        //Получаем книги автора из локальной базы данных
        List<BookEntity> bookEntityList = booksRepositoryImpl.getBooksByAuthorName(authorName);

        //Получаем книги автора из openlibrary.org
        RestTemplate restTemplate = new RestTemplate();
        //Получем Json содержащий информацию о книгах автора из openlibrary.org
        ResponseEntity<String> response = restTemplate.exchange(
                "http://openlibrary.org/search.json?author={authorName}",
                HttpMethod.GET,
                null,
                String.class,
                authorName);

        //получаем ISBN всех книг данного автора
        List<String> isbnList = getISBNFromJson(response.getBody());
        //оставляем 13-ти значные ISBN (так как на сайте openlibrary.org ISBN указаны
        //как в 10-ти так и в 13-ти значном формате)
        isbnList = isbnList.stream()
                            .filter(isbn -> isbn.length() == 13)
                            .collect(Collectors.toList());

        for(String element : isbnList){
            //Для каждого ISBN из списка делаем запрос на openlibrary.org
            response = restTemplate.exchange(
                    "http://openlibrary.org/api/books?bibkeys=ISBN:{isbn}&jscmd=data&format=json",
                    HttpMethod.GET,
                    null,
                    String.class,
                    element
            );

            //Достаём информацию о книге
            BookEntity book = getBookInfoFromJson(response.getBody(), element);
            book.setISBN(element);
            book.setAuthor(authorName);

            //Добавляем сформированную книгу book в список книг автора bookEntityList
            bookEntityList.add(book);
        }

        return bookEntityList.stream()
                .map(BookService::bookEntityIntoOpenLibraryDTO)
                .collect(Collectors.toList());
    }

    //Метод для извлечения всех ISBN из Json (который приходит от openlibrary.org)
    public List<String> getISBNFromJson (String json) {
        List<String> isbnFromJson = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode arrayDocsNode = rootNode.path("docs");
            for(JsonNode docsNode : arrayDocsNode){
                JsonNode arrayIsbnNode = docsNode.path("isbn");
                    for(JsonNode isbnNode : arrayIsbnNode){
                        isbnFromJson.add(isbnNode.textValue());
                    }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return isbnFromJson;
    }

    //Метод для получения BookInfo из json полученного от сервера openlibrary.org для каждого ISBN
    public BookEntity getBookInfoFromJson(String json , String isbn){
        BookEntity bookEntityFromJson = new BookEntity();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode isbnNode = rootNode.path("ISBN:" + isbn);

            String title = isbnNode.path("title").asText();
            bookEntityFromJson.setTitle(title);
            int countOfPages = isbnNode.path("number_of_pages").asInt();
            bookEntityFromJson.setNumberOfPages(countOfPages);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return bookEntityFromJson;
    }

    //преобразование BookEntity в BookOpenLibraryDTO
    public static BookOpenLibraryDTO bookEntityIntoOpenLibraryDTO(BookEntity bookEntity) {

        BookOpenLibraryDTO bookOpenLibraryDTO = new BookOpenLibraryDTO();
        bookOpenLibraryDTO.setISBN(bookEntity.getISBN());
        bookOpenLibraryDTO.setTitle(bookEntity.getTitle());
        bookOpenLibraryDTO.setAuthor(bookEntity.getAuthor());
        bookOpenLibraryDTO.setNumberOfPages(bookEntity.getNumberOfPages());

        return bookOpenLibraryDTO;
    }
}
