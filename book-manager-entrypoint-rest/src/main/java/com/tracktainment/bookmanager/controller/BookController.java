package com.tracktainment.bookmanager.controller;

import com.tracktainment.bookmanager.api.BookRestApi;
import com.tracktainment.bookmanager.domain.Book;
import com.tracktainment.bookmanager.domain.OrderBy;
import com.tracktainment.bookmanager.domain.OrderDirection;
import com.tracktainment.bookmanager.dto.BookCreate;
import com.tracktainment.bookmanager.dto.BookUpdate;
import com.tracktainment.bookmanager.exception.ParameterValidationFailedException;
import com.tracktainment.bookmanager.usecases.*;
import jakarta.servlet.http.HttpServletRequest;
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
    private final HttpServletRequest httpServletRequest;

    @Override
    public ResponseEntity<Book> create(BookCreate bookCreate) {
        log.info("Creating book: {}.", bookCreate);
        String jwt = httpServletRequest.getHeader("Authorization");

        CreateUseCase.Input input = CreateUseCase.Input.builder()
                .jwt(jwt)
                .bookCreate(bookCreate)
                .build();

        CreateUseCase.Output output = createUseCase.execute(input);
        return new ResponseEntity<>(output.getBook(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Book> findById(String id) {
        log.info("Finding book: {}.", id);
        String jwt = httpServletRequest.getHeader("Authorization");

        FindByIdUseCase.Input input = FindByIdUseCase.Input.builder()
                .jwt(jwt)
                .id(id)
                .build();

        FindByIdUseCase.Output output = findByIdUseCase.execute(input);
        return new ResponseEntity<>(output.getBook(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Book>> listByCriteria(
            Integer offset,
            Integer limit,
            String ids,
            String title,
            String author,
            String genre,
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
        // Input treatment
        if (createdAt != null) {
            from = null;
            to = null;
        }

        // Input validation
        if (to != null && from != null && to.isBefore(from)) {
            throw new ParameterValidationFailedException("Invalid dates input: 'to' must be 'later' than from");
        }

        if (orderByList.size() != orderDirectionList.size()) {
            throw new ParameterValidationFailedException(String.format(
                    "Invalid orderBy and orderDirection pair. " +
                            "'orderBy' size is %s and orderDirection size is %s. Both sizes must match",
                    orderByList.size(),
                    orderDirectionList.size()
            ));
        }

        // Get JWT
        String jwt = httpServletRequest.getHeader("Authorization");

        ListByCriteriaUseCase.Input input = ListByCriteriaUseCase.Input.builder()
                .jwt(jwt)
                .offset(offset)
                .limit(limit)
                .ids(ids)
                .title(title)
                .author(author)
                .genre(genre)
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
        String jwt = httpServletRequest.getHeader("Authorization");

        UpdateUseCase.Input input = UpdateUseCase.Input.builder()
                .jwt(jwt)
                .id(id)
                .bookUpdate(bookUpdate)
                .build();

        UpdateUseCase.Output output = updateUseCase.execute(input);
        return new ResponseEntity<>(output.getBook(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> delete(String id) {
        log.info("Deleting book: {}.", id);
        String jwt = httpServletRequest.getHeader("Authorization");

        DeleteUseCase.Input input = DeleteUseCase.Input.builder()
                .jwt(jwt)
                .id(id)
                .build();

        deleteUseCase.execute(input);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
