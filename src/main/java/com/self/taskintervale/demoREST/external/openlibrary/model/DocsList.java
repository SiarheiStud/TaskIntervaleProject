package com.self.taskintervale.demoREST.external.openlibrary.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties
public class DocsList {

    @JsonProperty("docs")
    private List<Docs> docsList;
}
