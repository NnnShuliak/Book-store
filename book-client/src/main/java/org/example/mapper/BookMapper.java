package org.example.mapper;

import com.example.grpc.BookCreationRequest;
import com.example.grpc.BookResponse;
import com.example.grpc.BookUpdateData;
import com.example.grpc.BookUpdateRequest;
import org.example.dto.BookDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDTO toDTO(BookResponse request);

    BookCreationRequest toCreationRequest(BookDTO book);


    BookUpdateData toUpdateData(BookDTO bookDTO);



}