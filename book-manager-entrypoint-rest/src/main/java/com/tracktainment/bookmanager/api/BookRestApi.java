package com.tracktainment.bookmanager.api;

import com.tracktainment.bookmanager.domain.Book;
import com.tracktainment.bookmanager.domain.OrderBy;
import com.tracktainment.bookmanager.domain.OrderDirection;
import com.tracktainment.bookmanager.dto.BookCreate;
import com.tracktainment.bookmanager.dto.BookUpdate;
import com.tracktainment.bookmanager.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("api/v1/books")
@Validated
@Tag(name = "Books", description = "Book Manager APIs")
public interface BookRestApi {

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "Create a new book",
            description = "Creates a new book with the provided details"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book created successfully",
                    content = @Content(schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Book already exists")
    })
    ResponseEntity<Book> create(@RequestBody @Valid BookCreate bookCreate);

    @GetMapping(
            path ="/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "Find a book by ID",
            description = "Returns a book based on its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book found",
                    content = @Content(schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    ResponseEntity<Book> findById(
            @Parameter(description = "Book ID", required = true)
            @PathVariable @Pattern(regexp = Constants.ID_REGEX, message = Constants.ID_INVALID_MSG) String id
    );

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "List books by criteria",
            description = "Returns a list of books filtered by various criteria"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of books",
                    content = @Content(schema = @Schema(implementation = Book.class)))
    })
    ResponseEntity<List<Book>> listByCriteria(
            @Parameter(description = "Result offset (pagination)")
            @RequestParam(required = false, defaultValue = Constants.DEFAULT_OFFSET)
            @Min(value = Constants.MIN_OFFSET, message = Constants.OFFSET_INVALID_MSG) Integer offset,

            @Parameter(description = "Maximum number of results to return")
            @RequestParam(required = false, defaultValue = Constants.DEFAULT_LIMIT)
            @Min(value = Constants.MIN_LIMIT, message = Constants.LIMIT_INVALID_MSG)
            @Max(value = Constants.MAX_LIMIT, message = Constants.LIMIT_INVALID_MSG) Integer limit,

            @Parameter(description = "Filter by IDs (comma-separated)")
            @RequestParam(required = false)
            @Pattern(regexp = Constants.ID_LIST_REGEX, message = Constants.IDS_INVALID_MSG) String ids,

            @Parameter(description = "Filter by title")
            @RequestParam(required = false)
            @Pattern(regexp = Constants.TITLE_REGEX, message = Constants.TITLE_INVALID_MSG) String title,

            @Parameter(description = "Filter by author")
            @RequestParam(required = false)
            @Pattern(regexp = Constants.AUTHOR_REGEX, message = Constants.AUTHOR_INVALID_MSG) String author,

            @Parameter(description = "Filter by genre")
            @RequestParam(required = false)
            @Pattern(regexp = Constants.GENRE_REGEX, message = Constants.GENRE_INVALID_MSG) String genre,

            @Parameter(description = "Filter by ISBN")
            @RequestParam(required = false)
            @Pattern(regexp = Constants.ISBN_REGEX, message = Constants.ISBN_INVALID_MSG) String isbn,

            @Parameter(description = "Filter by publisher")
            @RequestParam(required = false)
            @Pattern(regexp = Constants.PUBLISHER_REGEX, message = Constants.PUBLISHER_INVALID_MSG) String publisher,

            @Parameter(description = "Filter by publication date")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publishedDate,

            @Parameter(description = "Filter by language")
            @RequestParam(required = false)
            @Pattern(regexp = Constants.LANGUAGE_REGEX, message = Constants.LANGUAGE_INVALID_MSG) String language,

            @Parameter(description = "Filter by creation date")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAt,

            @Parameter(description = "Filter by date range start")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,

            @Parameter(description = "Filter by date range end")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,

            @Parameter(description = "Order by fields")
            @RequestParam(required = false, defaultValue = Constants.DEFAULT_ORDER) List<OrderBy> orderByList,

            @Parameter(description = "Order direction for each field")
            @RequestParam(required = false, defaultValue = Constants.DEFAULT_DIRECTION) List<OrderDirection> orderDirectionList
    );

    @PatchMapping(
            path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "Update a book",
            description = "Updates an existing book with the provided details"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated successfully",
                    content = @Content(schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    ResponseEntity<Book> update(
            @Parameter(description = "Book ID", required = true)
            @PathVariable @Pattern(regexp = Constants.ID_REGEX, message = Constants.ID_INVALID_MSG) String id,

            @RequestBody @Valid BookUpdate bookUpdate
    );
    @DeleteMapping(path = "/{id}")
    @Operation(
            summary = "Delete a book",
            description = "Deletes a book based on its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    ResponseEntity<Void> delete(
            @Parameter(description = "Book ID", required = true)
            @PathVariable @Pattern(regexp = Constants.ID_REGEX, message = Constants.ID_INVALID_MSG) String id
    );
}
