package com.self.taskintervale.demoREST.external.openlibrary.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties
public class BookInfo {

    @JsonProperty("title")
    private String title;

    @JsonProperty("number_of_pages")
    private int numberOfPages;
}
