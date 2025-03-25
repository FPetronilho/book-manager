package com.tracktainment.bookmanager.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookUpdate {

    private String id;
    private String title;
    private String author;
    private String isbn;
    private String publisher;
    private LocalDate publishedDate;
    private String language;
}
