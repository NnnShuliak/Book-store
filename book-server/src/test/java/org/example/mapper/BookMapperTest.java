//package org.example.mapper;
//
//import com.example.grpc.BookCreationRequest;
//import com.example.grpc.BookResponse;
//import com.example.grpc.BookUpdateData;
//import com.example.grpc.BookUpdateRequest;
//import org.example.domain.Book;
//import org.example.mapper.BookMapper;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//@SpringBootTest
//public class BookMapperTest {
//
//
//
//    @Autowired
//    BookMapper bookMapper;
//
//    @Test
//    public void testToEntityFromUpdateRequest() {
//        //Given
//        BookUpdateData data = BookUpdateData.newBuilder()
//                .setTitle("Title")
//                .setAuthor("Updated Author")
//                .setQuantity(20)
//                .build();
//        BookUpdateRequest request = BookUpdateRequest.newBuilder()
//                .setIsbn("isbn")
//                .setData(data)
//                .build();
//
//
//        // When
//        Book book = bookMapper.toEntity(request);
//
//        // Then
//        assertEquals(data.getTitle(), book.getTitle());
//        assertEquals(data.getAuthor(), book.getAuthor());
//        assertEquals(data.getQuantity(), book.getQuantity());
//        assertEquals(request.getIsbn(), book.getIsbn());
//        assertNull(book.getId());
//    }
//
//    @Test
//    public void testToEntityFromCreationRequest() {
//        // Given
//        BookCreationRequest request = BookCreationRequest.newBuilder()
//                .setTitle("New Book")
//                .setAuthor("Author")
//                .setIsbn("isbn123")
//                .setQuantity(5)
//                .build();
//
//        // When
//        Book book = bookMapper.toEntity(request);
//
//        // Then
//        assertEquals(request.getTitle(), book.getTitle());
//        assertEquals(request.getAuthor(), book.getAuthor());
//        assertEquals(request.getIsbn(), book.getIsbn());
//        assertEquals(request.getQuantity(), book.getQuantity());
//        assertNull(book.getId());
//    }
//
//    @Test
//    public void testToResponse() {
//        // Given
//        Book book = new Book();
//        book.setTitle("Test Book");
//        book.setAuthor("Test Author");
//        book.setIsbn("isbn");
//        book.setQuantity(10);
//        book.setId(UUID.randomUUID());
//
//        // When
//        BookResponse response = bookMapper.toResponse(book);
//
//        // Then
//        assertEquals(book.getTitle(), response.getTitle());
//        assertEquals(book.getAuthor(), response.getAuthor());
//        assertEquals(book.getQuantity(), response.getQuantity());
//        assertEquals(book.getIsbn(), response.getIsbn());
//
//    }
//}