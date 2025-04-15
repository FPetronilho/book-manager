package com.tracktainment.bookmanager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tracktainment.bookmanager.util.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "Data for creating a new book")
public class BookCreate {

    @NotNull(message = Constants.TITLE_MANDATORY_MSG)
    @Pattern(regexp = Constants.TITLE_REGEX, message = Constants.TITLE_INVALID_MSG)
    @Schema(description = "Title of the book", example = "The Great Gatsby")
    private String title;

    @NotNull(message = Constants.AUTHOR_MANDATORY_MSG)
    @Pattern(regexp = Constants.AUTHOR_REGEX, message = Constants.AUTHOR_INVALID_MSG)
    @Schema(description = "Author of the book", example = "F. Scott Fitzgerald")
    private String author;

    @Pattern(regexp = Constants.ISBN_REGEX, message = Constants.ISBN_INVALID_MSG)
    @Schema(description = "ISBN of the book", example = "978-3-16-148410-0")
    private String isbn;

    @Pattern(regexp = Constants.PUBLISHER_REGEX, message = Constants.PUBLISHER_INVALID_MSG)
    @Schema(description = "Publisher of the book", example = "Penguin Books")
    private String publisher;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(description = "Publication date", example = "1925-04-10")
    private LocalDate publishedDate;

    @Pattern(regexp = Constants.LANGUAGE_REGEX, message = Constants.LANGUAGE_INVALID_MSG)
    @Schema(description = "Language of the book", example = "English")
    private String language;
}
