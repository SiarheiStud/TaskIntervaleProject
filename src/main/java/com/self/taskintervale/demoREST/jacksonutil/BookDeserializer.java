package com.self.taskintervale.demoREST.jacksonutil;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.self.taskintervale.demoREST.entity.dto.BookDTO;

import java.io.IOException;
import java.math.BigDecimal;


public class BookDeserializer extends StdDeserializer<BookDTO> {

    protected BookDeserializer() {
        this (null);
    }

    protected BookDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public BookDTO deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                                                                            throws IOException{
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        String title = node.get("title").asText();
        String author = node.get("author").asText();
        int numberOfPages = node.get("numberOfPages").asInt();
        double weight = node.get("weight").asDouble();
        BigDecimal price = node.get("price").decimalValue();
        String ISBN = node.get("isbn").asText();

        return new BookDTO(ISBN, title, author, numberOfPages, weight, price);
    }
}
