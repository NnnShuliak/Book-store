package org.example.service.Impl;

import com.example.grpc.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.example.dto.BookDTO;
import org.example.mapper.BookMapper;
import org.example.service.BookService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    @GrpcClient("book-client")
    private BookServiceGrpc.BookServiceBlockingStub bookServiceStub;
    private final BookMapper mapper;


    @Override
    public List<BookDTO> findAll(){
        List<BookDTO> finalResponse = new ArrayList<>();
        Iterator<BookResponse> responses = bookServiceStub.findAllBooks(null);
        while (responses.hasNext()){
            finalResponse.add(mapper.toDTO(responses.next()));
        }
        return finalResponse;

    }
    @Override
    public BookDTO findByIsbn(String isbn){

        BookFindRequest request = BookFindRequest.newBuilder()
                .setIsbn(isbn)
                .build();

        return mapper.toDTO(bookServiceStub.findBook(request));
    }


    @Override
    public BookDTO create(BookDTO bookDTO){
        BookCreationRequest request = mapper.toCreationRequest(bookDTO);
        return mapper.toDTO(bookServiceStub.createBook(request));
    }

    @Override
    public BookDTO update(String isbn, BookDTO bookDTO){
        BookUpdateData updateData = mapper.toUpdateData(bookDTO);

        BookUpdateRequest updateRequest = BookUpdateRequest.newBuilder()
                .setIsbn(isbn)
                .setData(updateData)
                .build();
        return mapper.toDTO(bookServiceStub.updateBook(updateRequest));
    }
    @Override
    public void deleteById(String isbn){
        BookDeleteRequest request = BookDeleteRequest.newBuilder()
                .setIsbn(isbn)
                .build();
        bookServiceStub.deleteBook(request);
    }


}
