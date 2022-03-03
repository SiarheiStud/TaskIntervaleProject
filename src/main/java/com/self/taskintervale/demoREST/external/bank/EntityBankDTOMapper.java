package com.self.taskintervale.demoREST.external.bank;

import com.self.taskintervale.demoREST.entity.BookEntity;

public class EntityBankDTOMapper {

    //преобразование BookEntity в BankDTO
    public static BankDTO bookEntityIntoBankDTO(BookEntity bookEntity) {

        BankDTO bankDTO = new BankDTO();
        bankDTO.setTitle(bookEntity.getTitle());
        bankDTO.setAuthor(bookEntity.getAuthor());
        bankDTO.setPrice(bookEntity.getPrice());

        return bankDTO;
    }
}
