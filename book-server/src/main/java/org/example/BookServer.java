package org.example;


import com.example.grpc.BookCreationRequest;
import com.example.grpc.BookResponse;
import org.example.domain.Book;
import org.example.mapper.BookMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

@SpringBootApplication
public class BookServer {
    public static void main(String[] args) {
        SpringApplication.run(BookServer.class);
    }

    @Bean
    public CommandLineRunner commandLineRunner(BookMapper bookMapper){
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
//                Book book = new Book();
//                book.setId(UUID.randomUUID());
//
//                book.setAuthor("asd");
//                book.setIsbn("asd");
//
//
//                System.out.println(bookMapper.toResponse(book));
            }
        };
    }
}