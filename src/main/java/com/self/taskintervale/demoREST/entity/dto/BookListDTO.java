package com.self.taskintervale.demoREST.entity.dto;

import lombok.*;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class BookListDTO {

    private List<BookDTO> bookDtoList;

}
