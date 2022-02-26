package com.self.taskintervale.demoREST.external.openlibrary.model;


import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties
public class IsbnBookInfo {

    private Map<String, BookInfo> bookInfoMap = new HashMap<>();

    @JsonAnySetter
    public void setBookInfo(String differentStringFromJson, BookInfo bookInfo) {
        this.bookInfoMap.put(differentStringFromJson, bookInfo);
    }

}
