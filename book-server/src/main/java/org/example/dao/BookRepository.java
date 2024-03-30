package org.example.dao;


import jakarta.transaction.Transactional;
import org.example.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


public interface BookRepository extends JpaRepository<Book, UUID> {

    Optional<Book> findByIsbn(String isbn);
    @Transactional
    void deleteByIsbn(String isbn);
}
