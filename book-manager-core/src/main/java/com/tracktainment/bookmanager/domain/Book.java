package com.tracktainment.bookmanager.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Book information")
public class Book {

    @Schema(description = "Unique identifier of the book", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Title of the book", example = "The Great Gatsby")
    private String title;

    @Schema(description = "Author of the book", example = "F. Scott Fitzgerald")
    private String author;

    @Schema(description = "ISBN of the book", example = "978-3-16-148410-0")
    private String isbn;

    @Schema(description = "Publisher of the book", example = "Penguin Books")
    private String publisher;

    @Schema(description = "Publication date", example = "1925-04-10")
    private LocalDate publishedDate;

    @Schema(description = "Language of the book", example = "English")
    private String language;

    @Schema(description = "Book creation timestamp", example = "2023-01-15T12:34:56")
    private LocalDateTime createdAt;

    @Schema(description = "Book last update timestamp", example = "2023-02-20T15:45:30")
    private LocalDateTime updatedAt;
}
