package com.self.taskintervale.demoREST.external.openlibrary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.self.taskintervale.demoREST.entity.BookEntity;
import com.self.taskintervale.demoREST.repository.BooksRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@PropertySource("classpath:application.properties")
public class OpenLibraryService {

    private final BooksRepositoryImpl booksRepositoryImpl;
    private final RestTemplate restTemplate;

    private final String BASE_URL = "http://openlibrary.org";
    private final String URL_GET_ISBN = BASE_URL + "/api/books?bibkeys=ISBN:{isbn}&jscmd=data&format=json"; //"http://openlibrary.org/api/books?bibkeys=ISBN:{isbn}&jscmd=data&format=json";
    private final String URL_GET_BOOK_INFO = BASE_URL + "/search.json?author={authorName}";   //"http://openlibrary.org/search.json?author={authorName}"

    @Autowired
    public OpenLibraryService(BooksRepositoryImpl booksRepositoryImpl, RestTemplate restTemplate) {

        this.booksRepositoryImpl = booksRepositoryImpl;
        this.restTemplate = restTemplate;
    }

    //Метод формирует и возвращает список книг найденных у автора
    public List<OpenLibraryBookDTO> getBooksByAuthorName(String authorName) {

        //Получаем книги автора из локальной базы данных
        List<BookEntity> booksEntityFromDB = booksRepositoryImpl.getBooksByAuthorName(authorName);

        //Получаем книги автора из openlibrary.org
        List<OpenLibraryBookDTO> booksFromOpenLibrary = getBooksFromOpenLibrary(authorName);

        //Формируем итоговый список найденых книг (из локальной базы данных и из openlibrary.org)
        List<OpenLibraryBookDTO> allFindBooks = booksEntityFromDB.stream()
                .map(OpenLibraryService::bookEntityIntoOpenLibraryBookDTO)
                .collect(Collectors.toList());

        allFindBooks.addAll(booksFromOpenLibrary);

        return allFindBooks;
    }

    public List<OpenLibraryBookDTO> getBooksFromOpenLibrary(String authorName) {

        List<OpenLibraryBookDTO> booksDTOList = new ArrayList<>();
        List<String> ISBNList = getISBNFromOpenLibrary(authorName);

        for (String element : ISBNList) {
            //Для каждого ISBN из списка делаем запрос на openlibrary.org
            ResponseEntity<String> response = restTemplate.exchange(
                    URL_GET_ISBN,
                    HttpMethod.GET,
                    null,
                    String.class,
                    element
            );

            //Достаём информацию о книге
            OpenLibraryBookDTO book = getBookInfoFromJson(response.getBody(), element);
            book.setISBN(element);
            book.setAuthor(authorName);

            //Добавляем сформированную книгу book в список книг автора booksDTOList
            booksDTOList.add(book);
        }
        return booksDTOList;
    }

    //Метод возвращает список ISBN найденых в openlibrary.org у автора
    public List<String> getISBNFromOpenLibrary(String authorName) {

        //Получем Json содержащий информацию о книгах автора из openlibrary.org
        ResponseEntity<String> response = restTemplate.exchange(
                URL_GET_BOOK_INFO,
                HttpMethod.GET,
                null,
                String.class,
                authorName);

        //получаем ISBN всех книг данного автора
        List<String> isbnList = getISBNFromJson(response.getBody());

        /*
         *оставляем 13-ти значные ISBN (так как на сайте openlibrary.org ISBN указаны
         *как в 10-ти так и в 13-ти значном формате)
         */
        return isbnList.stream()
                .filter(isbn -> isbn.length() == 13)
                .collect(Collectors.toList());
    }

    //Метод для извлечения всех ISBN из Json (который приходит от openlibrary.org)
    public List<String> getISBNFromJson(String json) {

        List<String> isbnFromJson = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode arrayDocsNode = rootNode.path("docs");
            for (JsonNode docsNode : arrayDocsNode) {
                JsonNode arrayIsbnNode = docsNode.path("isbn");
                for (JsonNode isbnNode : arrayIsbnNode) {
                    isbnFromJson.add(isbnNode.textValue());
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return isbnFromJson;
    }

    //Метод для получения BookInfo из json полученного от сервера openlibrary.org для каждого ISBN
    public OpenLibraryBookDTO getBookInfoFromJson(String json, String isbn) {

        OpenLibraryBookDTO bookDTOFromJson = new OpenLibraryBookDTO();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode isbnNode = rootNode.path("ISBN:" + isbn);

            String title = isbnNode.path("title").asText();
            bookDTOFromJson.setTitle(title);
            int countOfPages = isbnNode.path("number_of_pages").asInt();
            bookDTOFromJson.setNumberOfPages(countOfPages);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return bookDTOFromJson;
    }

    //преобразование BookEntity в BookOpenLibraryDTO
    public static OpenLibraryBookDTO bookEntityIntoOpenLibraryBookDTO(BookEntity bookEntity) {

        OpenLibraryBookDTO openLibraryBookDTO = new OpenLibraryBookDTO();
        openLibraryBookDTO.setISBN(bookEntity.getISBN());
        openLibraryBookDTO.setTitle(bookEntity.getTitle());
        openLibraryBookDTO.setAuthor(bookEntity.getAuthor());
        openLibraryBookDTO.setNumberOfPages(bookEntity.getNumberOfPages());

        return openLibraryBookDTO;
    }
}
