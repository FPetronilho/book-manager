package com.tracktainment.bookmanager.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@Table(name = "books")
public class BookEntity extends BaseEntity {

    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "author", length = 100)
    private String author;

    @Column(name = "genre", length = 50)
    private String genre;

    @Column(name = "isbn", length = 17)
    private String isbn;

    @Column(name = "publisher", length = 150)
    private String publisher;

    @Column(name = "published_date")
    private LocalDate publishedDate;

    @Column(name = "language", length = 50)
    private String language;
}
