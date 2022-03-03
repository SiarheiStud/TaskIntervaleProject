package com.self.taskintervale.demoREST.external.openlibrary;

import com.self.taskintervale.demoREST.entity.BookEntity;

public class EntityDtoMapper {

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
