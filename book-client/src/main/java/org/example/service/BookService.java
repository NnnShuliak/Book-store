package org.example.service;

import org.example.dto.BookDTO;

import java.util.List;

public interface BookService {
    List<BookDTO> findAll();

    BookDTO findByIsbn(String isbn);

    BookDTO create(BookDTO bookDTO);

    BookDTO update(String isbn, BookDTO bookDTO);

    void deleteById(String isbn);
}
