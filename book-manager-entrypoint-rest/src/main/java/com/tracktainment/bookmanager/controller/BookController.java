package com.tracktainment.bookmanager.controller;

import com.tracktainment.bookmanager.api.BookRestApi;
import com.tracktainment.bookmanager.domain.Book;
import com.tracktainment.bookmanager.domain.OrderBy;
import com.tracktainment.bookmanager.domain.OrderDirection;
import com.tracktainment.bookmanager.dto.BookCreate;
import com.tracktainment.bookmanager.dto.BookUpdate;
import com.tracktainment.bookmanager.usecases.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class BookController implements BookRestApi {

    private final CreateUseCase createUseCase;
    private final FindByIdUseCase findByIdUseCase;
    private final ListByCriteriaUseCase listByCriteriaUseCase;
    private final UpdateUseCase updateUseCase;
    private final DeleteUseCase deleteUseCase;

    @Override
    public ResponseEntity<Book> create(BookCreate bookCreate) {
        log.info("Creating book: {}.", bookCreate);
        CreateUseCase.Input input = CreateUseCase.Input.builder()
                .bookCreate(bookCreate)
                .build();

        CreateUseCase.Output output = createUseCase.execute(input);
        return new ResponseEntity<>(output.getBook(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Book> findById(String id) {
        log.info("Finding book: {}.", id);
        FindByIdUseCase.Input input = FindByIdUseCase.Input.builder()
                .id(id)
                .build();

        FindByIdUseCase.Output output = findByIdUseCase.execute(input);
        return new ResponseEntity<>(output.getBook(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Book>> listByCriteria(
            Integer offset,
            Integer limit,
            String title,
            String author,
            String isbn,
            String publisher,
            LocalDate publishedDate,
            String language,
            LocalDate createdAt,
            LocalDate from,
            LocalDate to,
            List<OrderBy> orderByList,
            List<OrderDirection> orderDirectionList
    ) {
        ListByCriteriaUseCase.Input input = ListByCriteriaUseCase.Input.builder()
                .offset(offset)
                .limit(limit)
                .title(title)
                .author(author)
                .isbn(isbn)
                .publisher(publisher)
                .publishedDate(publishedDate)
                .language(language)
                .createdAt(createdAt)
                .from(from)
                .to(to)
                .orderByList(orderByList)
                .orderDirectionList(orderDirectionList)
                .build();

        log.info("Listing books by criteria: {}.", input);
        ListByCriteriaUseCase.Output output = listByCriteriaUseCase.execute(input);
        return new ResponseEntity<>(output.getBooks(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Book> update(String id, BookUpdate bookUpdate) {
        log.info("Updating book: {}. Updated book data: {}.", id, bookUpdate);
        UpdateUseCase.Input input = UpdateUseCase.Input.builder()
                .id(id)
                .bookUpdate(bookUpdate)
                .build();

        UpdateUseCase.Output output = updateUseCase.execute(input);
        return new ResponseEntity<>(output.getBook(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> delete(String id) {
        log.info("Deleting book: {}.", id);
        DeleteUseCase.Input input = DeleteUseCase.Input.builder()
                .id(id)
                .build();

        deleteUseCase.execute(input);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
