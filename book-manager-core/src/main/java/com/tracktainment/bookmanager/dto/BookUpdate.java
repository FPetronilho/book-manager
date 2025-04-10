package com.tracktainment.bookmanager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tracktainment.bookmanager.util.Constants;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookUpdate {

    @Pattern(regexp = Constants.TITLE_REGEX, message = Constants.TITLE_INVALID_MSG)
    private String title;

    @Pattern(regexp = Constants.AUTHOR_REGEX, message = Constants.AUTHOR_INVALID_MSG)
    private String author;

    @Pattern(regexp = Constants.ISBN_REGEX, message = Constants.ISBN_INVALID_MSG)
    private String isbn;

    @Pattern(regexp = Constants.PUBLISHER_REGEX, message = Constants.PUBLISHER_INVALID_MSG)
    private String publisher;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate publishedDate;

    @Pattern(regexp = Constants.LANGUAGE_REGEX, message = Constants.LANGUAGE_INVALID_MSG)
    private String language;
}
