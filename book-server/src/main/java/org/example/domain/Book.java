package org.example.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.UUID;

@Entity
@Data
@Table(name = "books")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;
    @Column(name = "title")
    private String title;
    @Column(name = "author")
    private String author;
    @NaturalId
    @Column(name = "isbn",unique = true,nullable = false)
    private String isbn;
    @Column(name = "quantity")
    private Integer quantity;


}
