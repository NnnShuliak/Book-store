package org.example.service;

import com.example.grpc.*;
import com.google.protobuf.Empty;
import org.example.dao.BookRepository;
import org.example.domain.Book;
import org.example.mapper.BookMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@Testcontainers
@Import(BookServiceTestConfig.class)
public class BookServiceTest {

    @Autowired
    BookRepository repository;


    @Autowired
    BookServiceGrpc.BookServiceBlockingStub bookStub;

    @MockBean
    BookMapper bookMapper;

    @Container
    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:latest");

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.datasource.url", container::getJdbcUrl);
        propertyRegistry.add("spring.datasource.username", container::getUsername);
        propertyRegistry.add("spring.datasource.password", container::getPassword);
    }


    @AfterEach
    void databaseClean() {
        repository.deleteAll();

    }


    @Test
    void testFindBook() {
        //Given
        Book book = Book.builder()
                .isbn("isbn")
                .title("title")
                .author("author")
                .quantity(3)
                .build();

        repository.save(book);

        when(bookMapper.toResponse(book)).thenReturn(
                BookResponse.newBuilder()
                        .setIsbn(book.getIsbn())
                        .setTitle(book.getTitle())
                        .setAuthor(book.getAuthor())
                        .setQuantity(book.getQuantity())
                        .build());

        BookFindRequest request = BookFindRequest.newBuilder()
                .setIsbn(book.getIsbn())
                .build();
        //When
        BookResponse response = bookStub.findBook(request);

        //Then
        assertEquals(response.getIsbn(), book.getIsbn());
    }

    @Test
    void testFindAllBooks() {
        //Given
        List<Book> books = new ArrayList<>();
        books.add(Book.builder()
                .isbn("isbn")
                .title("title")
                .author("author")
                .quantity(1)
                .build());

        books.add(Book.builder()
                .isbn("isbn2")
                .title("title2")
                .author("author2")
                .quantity(2)
                .build());

        repository.saveAll(books);
        Iterator<Book> bookIterator = books.iterator();

        when(bookMapper.toResponse(any())).thenAnswer(invocation -> {
            Book book = invocation.getArgument(0);
            return BookResponse.newBuilder()
                    .setIsbn(book.getIsbn())
                    .setTitle(book.getTitle())
                    .setAuthor(book.getAuthor())
                    .setQuantity(book.getQuantity())
                    .build();
        });

        //When
        Iterator<BookResponse> response = bookStub.findAllBooks(Empty.newBuilder().build());

        //Then
        while (response.hasNext()) {
            assertTrue(bookIterator.hasNext());
            Book expectedBook = bookIterator.next();
            BookResponse actualBook = response.next();

            assertEquals(expectedBook.getIsbn(), actualBook.getIsbn());
            assertEquals(expectedBook.getTitle(), actualBook.getTitle());
            assertEquals(expectedBook.getAuthor(), actualBook.getAuthor());
            assertEquals(expectedBook.getQuantity(), actualBook.getQuantity());
        }
        assertFalse(bookIterator.hasNext());
    }


    @Test
    void testCreateBook() {
        //Given
        String isbn = "new-isbn";
        String title = "New Book";
        String author = "New Author";
        int quantity = 5;

        BookCreationRequest request = BookCreationRequest.newBuilder()
                .setIsbn(isbn)
                .setTitle(title)
                .setAuthor(author)
                .setQuantity(quantity)
                .build();

        Book book = Book.builder()
                .isbn(isbn)
                .title(title)
                .author(author)
                .quantity(quantity)
                .build();

        when(bookMapper.toEntity(request)).thenReturn(book);
        when(bookMapper.toResponse(book)).thenReturn(
                BookResponse.newBuilder()
                        .setIsbn(isbn)
                        .setTitle(title)
                        .setAuthor(author)
                        .setQuantity(quantity)
                        .build());

        //When
        BookResponse response = bookStub.createBook(request);

        //Then
        assertEquals(isbn, response.getIsbn());
        assertEquals(title, response.getTitle());
        assertEquals(author, response.getAuthor());
        assertEquals(quantity, response.getQuantity());

        Book savedBook = repository.findByIsbn(isbn).orElseThrow();

        assertEquals(isbn, savedBook.getIsbn());
        assertEquals(title, savedBook.getTitle());
        assertEquals(author, savedBook.getAuthor());
        assertEquals(quantity, savedBook.getQuantity());
    }

    @Test
    void testUpdateBook() {
        //Given
        Book book = Book.builder()
                .isbn("isbn-to-update")
                .title("Title to Update")
                .author("Author to Update")
                .quantity(10)
                .build();

        Book savedBook = repository.save(book);

        String newTitle = "Updated Title";
        String newAuthor = "Updated Author";
        int newQuantity = 20;

        BookUpdateRequest request = BookUpdateRequest.newBuilder()
                .setIsbn(book.getIsbn())
                .setData(BookUpdateData.newBuilder()
                        .setTitle(newTitle)
                        .setAuthor(newAuthor)
                        .setQuantity(newQuantity)
                        .build())
                .build();

        Book expectedBook = Book.builder()
                .id(savedBook.getId())
                .isbn(book.getIsbn())
                .title(newTitle)
                .author(newAuthor)
                .quantity(newQuantity)
                .build();

        when(bookMapper.toEntity(request)).thenReturn(expectedBook);
        when(bookMapper.toResponse(expectedBook)).thenReturn(
                BookResponse.newBuilder()
                        .setIsbn(book.getIsbn())
                        .setTitle(newTitle)
                        .setAuthor(newAuthor)
                        .setQuantity(newQuantity)
                        .build());

        //When
        BookResponse response = bookStub.updateBook(request);

        //Then
        assertEquals(book.getIsbn(), response.getIsbn());
        assertEquals(newTitle, response.getTitle());
        assertEquals(newAuthor, response.getAuthor());
        assertEquals(newQuantity, response.getQuantity());

        Book updatedBook = repository.findByIsbn(book.getIsbn()).orElseThrow();

        assertEquals(book.getIsbn(), updatedBook.getIsbn());
        assertEquals(newTitle, updatedBook.getTitle());
        assertEquals(newAuthor, updatedBook.getAuthor());
        assertEquals(newQuantity, updatedBook.getQuantity());
    }

    @Test
    void testDeleteBook() {
        //Given
        Book book = Book.builder()
                .isbn("isbn")
                .title("Book")
                .author("Author")
                .quantity(30)
                .build();
        repository.save(book);

        BookDeleteRequest request = BookDeleteRequest.newBuilder()
                .setIsbn(book.getIsbn())
                .build();

        //When
        bookStub.deleteBook(request);

        //Then
        Book validationBook = repository.findByIsbn(book.getIsbn()).orElse(null);
        assertNull(validationBook);


    }
}
