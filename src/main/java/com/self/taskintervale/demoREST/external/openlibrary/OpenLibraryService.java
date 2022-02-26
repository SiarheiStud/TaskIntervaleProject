package com.self.taskintervale.demoREST.external.openlibrary;

import com.self.taskintervale.demoREST.entity.BookEntity;
import com.self.taskintervale.demoREST.exeptions.OpenLibraryException;
import com.self.taskintervale.demoREST.external.openlibrary.model.BookInfo;
import com.self.taskintervale.demoREST.external.openlibrary.model.DocsList;
import com.self.taskintervale.demoREST.external.openlibrary.model.IsbnBookInfo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OpenLibraryService {

    private final RestTemplate restTemplate;

    private final String URL_GET_BOOK_INFO_BY_ISBN = "/api/books?bibkeys=ISBN:{isbn}&jscmd=data&format=json";
    private final String URL_GET_BOOK_INFO_BY_AUTHOR = "/search.json?author={authorName}";

    public OpenLibraryService(@Qualifier(value = "openLibraryRestTemplate") RestTemplate restTemplate) {

        this.restTemplate = restTemplate;
    }

    //Метод формирует и возвращает список книг найденных у автора
    public List<OpenLibraryBookDTO> getBooksByAuthorName(String authorName, List<BookEntity> booksEntityFromDB)
            throws OpenLibraryException {

        List<OpenLibraryBookDTO> allFindBooks = new ArrayList<>();

        //Получаем книги автора из локальной базы данных и добавляем их в итоговый список найденых книг
        List<OpenLibraryBookDTO> bookFromDB = booksEntityFromDB.stream()
                .map(EntityDtoMapper::bookEntityIntoOpenLibraryBookDTO)
                .collect(Collectors.toList());

        if(bookFromDB != null){
            allFindBooks.addAll(bookFromDB);
        }

        //Получаем книги автора из openlibrary.org
        List<OpenLibraryBookDTO> booksFromOpenLibrary = getBooksFromOpenLibrary(authorName);

        if(booksFromOpenLibrary != null){
            allFindBooks.addAll(booksFromOpenLibrary);
        }

        return allFindBooks;
    }

    public List<OpenLibraryBookDTO> getBooksFromOpenLibrary(String authorName) throws OpenLibraryException {

        List<OpenLibraryBookDTO> booksDTOList = new ArrayList<>();
        List<String> ISBNList = getISBNFromOpenLibrary(authorName);

        for (String element : ISBNList) {

            //Для каждого ISBN из списка делаем запрос на openlibrary.org
            IsbnBookInfo isbnBookInfo = restTemplate.getForObject(URL_GET_BOOK_INFO_BY_ISBN,
                    IsbnBookInfo.class,
                    element);

            OpenLibraryBookDTO openLibraryBookDTO = new OpenLibraryBookDTO();

            if(isbnBookInfo != null){
                if(isbnBookInfo.getBookInfoMap() != null){
                    BookInfo bookInfo = isbnBookInfo.getBookInfoMap().get("ISBN:" + element);
                    openLibraryBookDTO.setTitle(bookInfo.getTitle());
                    openLibraryBookDTO.setNumberOfPages(bookInfo.getNumberOfPages());
                    openLibraryBookDTO.setISBN(element);
                    openLibraryBookDTO.setAuthor(authorName);
                }
            }

            //Добавляем сформированную книгу openLibraryBookDTO в список книг автора booksDTOList
            booksDTOList.add(openLibraryBookDTO);
        }
        return booksDTOList;
    }

    //Метод возвращает список ISBN найденых в openlibrary.org у автора
    public List<String> getISBNFromOpenLibrary(String authorName) throws OpenLibraryException {

        //Получаем информацию о книгах автора из openlibrary.org
        DocsList docsList = restTemplate.getForObject(URL_GET_BOOK_INFO_BY_AUTHOR,
                DocsList.class,
                authorName);

        if(docsList == null || docsList.getDocsList() == null){
            throw new OpenLibraryException("Не корректный данные от openlibrary.org");
        }

        /*
         *оставляем 13-ти значные ISBN (так как на сайте openlibrary.org ISBN указаны
         *как в 10-ти так и в 13-ти значном формате)
         */
        return docsList.getDocsList().stream()
                .flatMap(doc -> doc.getIsbnList().stream())
                .filter(isbn -> isbn.length() == 13)
                .collect(Collectors.toList());
    }
}
