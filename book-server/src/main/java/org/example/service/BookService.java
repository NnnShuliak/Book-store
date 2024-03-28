package org.example.service;

import com.example.grpc.*;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.dao.BookRepository;
import org.example.domain.Book;
import org.example.mapper.BookMapper;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class BookService extends BookServiceGrpc.BookServiceImplBase {

    private final BookRepository bookRepository;
    private final BookMapper mapper;

    @Override
    public void findAllBooks(Empty request, StreamObserver<BookResponse> responseObserver) {
        List<Book> books = bookRepository.findAll();
        books.stream().map(mapper::toResponse).forEach(responseObserver::onNext);
        responseObserver.onCompleted();
    }

    @Override
    public void findBook(BookFindRequest request, StreamObserver<BookResponse> responseObserver) {
        String isbn = request.getIsbn();
        Book findedBook = bookRepository.findByIsbn(isbn).orElse(null);
        if (findedBook == null) {
            responseObserver.onError(new NoSuchElementException("There is no Book with ISBN:" + isbn));
        }
        BookResponse response = mapper.toResponse(findedBook);
        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

    @Override
    public void createBook(BookCreationRequest request, StreamObserver<BookResponse> responseObserver) {
        Book bookToCreate = mapper.toEntity(request);
        Book createdBook = bookRepository.save(bookToCreate);

        BookResponse response = mapper.toResponse(createdBook);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateBook(BookUpdateRequest request, StreamObserver<BookResponse> responseObserver) {

        String isbn = request.getIsbn();
        Book foundBook = bookRepository.findByIsbn(isbn).orElse(null);
        if (foundBook==null) {
            responseObserver.onError(new NoSuchElementException("There is no Book with ISBN:" + isbn));
        }
        BookUpdateData updateData = request.getData();

        foundBook.setTitle(updateData.getTitle());
        foundBook.setAuthor(updateData.getAuthor());
        foundBook.setQuantity(updateData.getQuantity());

        Book updatedBook = bookRepository.save(foundBook);

        BookResponse response = mapper.toResponse(updatedBook);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteBook(BookDeleteRequest request, StreamObserver<Empty> responseObserver) {
        String isbn = request.getIsbn();
        bookRepository.deleteByIsbn(isbn);
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }
}
