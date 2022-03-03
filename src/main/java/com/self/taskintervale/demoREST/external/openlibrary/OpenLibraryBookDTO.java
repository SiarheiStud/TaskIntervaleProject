package com.self.taskintervale.demoREST.external.openlibrary;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class OpenLibraryBookDTO {

    private String ISBN;
    private String title;
    private String author;
    private int numberOfPages;

}
