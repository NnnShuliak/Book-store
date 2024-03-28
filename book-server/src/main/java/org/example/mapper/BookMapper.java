package org.example.mapper;

import org.example.domain.Book;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import com.example.grpc.*;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {




    @Mapping(target = "title", source = "data.title")
    @Mapping(target = "author", source = "data.author")
    @Mapping(target = "quantity", source = "data.quantity")
    Book toEntity(BookUpdateRequest request);

    Book toEntity(BookCreationRequest request);

    BookResponse toResponse(Book book);




}