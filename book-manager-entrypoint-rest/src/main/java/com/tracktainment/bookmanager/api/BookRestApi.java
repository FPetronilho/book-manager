package com.tracktainment.bookmanager.api;

import com.tracktainment.bookmanager.domain.Book;
import com.tracktainment.bookmanager.domain.OrderBy;
import com.tracktainment.bookmanager.domain.OrderDirection;
import com.tracktainment.bookmanager.dto.BookCreate;
import com.tracktainment.bookmanager.dto.BookUpdate;
import com.tracktainment.bookmanager.util.Constants;
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
public interface BookRestApi {

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Book> create(@RequestBody @Valid BookCreate bookCreate);

    @GetMapping(
            path ="/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Book> findById(@PathVariable @Min(1) Long id);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Book>> listByCriteria(
            @RequestParam(required = false, defaultValue = Constants.DEFAULT_OFFSET)
            @Min(value = Constants.MIN_OFFSET, message = Constants.OFFSET_INVALID_MSG) Integer offset,

            @RequestParam(required = false, defaultValue = Constants.DEFAULT_LIMIT)
            @Min(value = Constants.MIN_LIMIT, message = Constants.LIMIT_INVALID_MSG)
            @Max(value = Constants.MAX_LIMIT, message = Constants.LIMIT_INVALID_MSG) Integer limit,

            @RequestParam(required = false)
            @Pattern(regexp = Constants.TITLE_REGEX, message = Constants.TITLE_INVALID_MSG) String title,

            @RequestParam(required = false)
            @Pattern(regexp = Constants.AUTHOR_REGEX, message = Constants.AUTHOR_INVALID_MSG) String author,

            @RequestParam(required = false)
            @Pattern(regexp = Constants.ISBN_REGEX, message = Constants.ISBN_INVALID_MSG) String isbn,

            @RequestParam(required = false)
            @Pattern(regexp = Constants.PUBLISHER_REGEX, message = Constants.PUBLISHER_INVALID_MSG) String publisher,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publishedDate,

            @RequestParam(required = false)
            @Pattern(regexp = Constants.LANGUAGE_REGEX, message = Constants.LANGUAGE_INVALID_MSG) String language,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAt,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,

            @RequestParam(required = false, defaultValue = Constants.DEFAULT_ORDER) List<OrderBy> orderByList,

            @RequestParam(required = false, defaultValue = Constants.DEFAULT_DIRECTION) List<OrderDirection> orderDirectionList
    );

    @PatchMapping(
            path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Book> update(
            @PathVariable @Min(1) Long id,
            @RequestBody @Valid BookUpdate bookUpdate
    );

    @DeleteMapping(path = "/{id}")
    ResponseEntity<Void> delete(@PathVariable @Min(1) Long id);
}
