package controller;

import com.tracktainment.bookmanager.controller.BookController;
import com.tracktainment.bookmanager.domain.Book;
import com.tracktainment.bookmanager.domain.OrderBy;
import com.tracktainment.bookmanager.domain.OrderDirection;
import com.tracktainment.bookmanager.dto.BookCreate;
import com.tracktainment.bookmanager.dto.BookUpdate;
import com.tracktainment.bookmanager.exception.ParameterValidationFailedException;
import com.tracktainment.bookmanager.usecases.*;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import testutil.TestBookDataUtil;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private CreateUseCase createUseCase;

    @Mock
    private FindByIdUseCase findByIdUseCase;

    @Mock
    private ListByCriteriaUseCase listByCriteriaUseCase;

    @Mock
    private UpdateUseCase updateUseCase;

    @Mock
    private DeleteUseCase deleteUseCase;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private BookController bookController;

    private BookCreate bookCreate;
    private BookUpdate bookUpdate;
    private Book book;
    private String jwt;
    private String bookId;

    @BeforeEach
    void setUp() {
        bookId = UUID.randomUUID().toString();
        bookCreate = TestBookDataUtil.createTestBookCreate();
        bookUpdate = TestBookDataUtil.createTestBookUpdate();
        book = TestBookDataUtil.createTestBook();
        jwt = "Bearer token";

        lenient().when(httpServletRequest.getHeader("Authorization")).thenReturn(jwt);
    }

    @Test
    void shouldCreateBookSuccessfully() {
        // Arrange
        CreateUseCase.Output output = CreateUseCase.Output.builder()
                .book(book)
                .build();

        when(createUseCase.execute(any(CreateUseCase.Input.class))).thenReturn(output);

        // Act
        ResponseEntity<Book> response = bookController.create(bookCreate);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(book, response.getBody());

        verify(httpServletRequest).getHeader("Authorization");
        verify(createUseCase).execute(any(CreateUseCase.Input.class));
    }

    @Test
    void shouldFindBookByIdSuccessfully() {
        // Arrange
        FindByIdUseCase.Output output = FindByIdUseCase.Output.builder()
                .book(book)
                .build();

        when(findByIdUseCase.execute(any(FindByIdUseCase.Input.class))).thenReturn(output);

        // Act
        ResponseEntity<Book> response = bookController.findById(bookId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(book, response.getBody());

        verify(httpServletRequest).getHeader("Authorization");
        verify(findByIdUseCase).execute(any(FindByIdUseCase.Input.class));
    }

    @Test
    void shouldListBooksByCriteriaSuccessfully() {
        // Arrange
        List<Book> books = Arrays.asList(book, TestBookDataUtil.createTestBookWithUpdate());
        ListByCriteriaUseCase.Output output = ListByCriteriaUseCase.Output.builder()
                .books(books)
                .build();

        when(listByCriteriaUseCase.execute(any(ListByCriteriaUseCase.Input.class)))
                .thenReturn(output);

        // Act
        ResponseEntity<List<Book>> response = bookController.listByCriteria(
                0, 10, null, null, null, null, null,
                null, null, null, null, null, null,
                Collections.singletonList(OrderBy.TITLE),
                Collections.singletonList(OrderDirection.ASC)
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(books, response.getBody());
        assertEquals(2, response.getBody().size());

        verify(httpServletRequest).getHeader("Authorization");
        verify(listByCriteriaUseCase).execute(any(ListByCriteriaUseCase.Input.class));
    }

    @Test
    void shouldThrowExceptionWhenOrderByAndOrderDirectionSizesDoNotMatch() {
        // Arrange
        List<OrderBy> orderByList = Arrays.asList(OrderBy.TITLE, OrderBy.AUTHOR);
        List<OrderDirection> orderDirectionList = Collections.singletonList(OrderDirection.ASC);

        // Act & Assert
        assertThrows(ParameterValidationFailedException.class, () ->
                bookController.listByCriteria(
                        0, 10, null, null, null, null, null,
                        null, null, null, null, null, null,
                        orderByList, orderDirectionList
                )
        );

        verify(listByCriteriaUseCase, never()).execute(any());
    }

    @Test
    void shouldThrowExceptionWhenToDateIsBeforeFromDate() {
        // Arrange
        LocalDate from = LocalDate.now();
        LocalDate to = from.minusDays(1);

        // Act & Assert
        assertThrows(ParameterValidationFailedException.class, () ->
                bookController.listByCriteria(
                        0, 10, null, null, null, null, null,
                        null, null, null, null, from, to,
                        Collections.singletonList(OrderBy.TITLE),
                        Collections.singletonList(OrderDirection.ASC)
                )
        );

        verify(listByCriteriaUseCase, never()).execute(any());
    }

    @Test
    void shouldIgnoreFromAndToWhenCreatedAtIsProvided() {
        // Arrange
        LocalDate createdAt = LocalDate.now();
        LocalDate from = LocalDate.now().minusDays(7);
        LocalDate to = LocalDate.now();

        ListByCriteriaUseCase.Output output = ListByCriteriaUseCase.Output.builder()
                .books(Collections.singletonList(book))
                .build();

        when(listByCriteriaUseCase.execute(any(ListByCriteriaUseCase.Input.class)))
                .thenReturn(output);

        // Act
        ResponseEntity<List<Book>> response = bookController.listByCriteria(
                0, 10, null, null, null, null, null,
                null, null, null, createdAt, from, to,
                Collections.singletonList(OrderBy.TITLE),
                Collections.singletonList(OrderDirection.ASC)
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(httpServletRequest).getHeader("Authorization");
        verify(listByCriteriaUseCase).execute(argThat(input ->
                input.getCreatedAt() == createdAt &&
                        input.getFrom() == null &&
                        input.getTo() == null
        ));
    }

    @Test
    void shouldUpdateBookSuccessfully() {
        // Arrange
        Book updatedBook = TestBookDataUtil.createTestBookWithUpdate();
        UpdateUseCase.Output output = UpdateUseCase.Output.builder()
                .book(updatedBook)
                .build();

        when(updateUseCase.execute(any(UpdateUseCase.Input.class))).thenReturn(output);

        // Act
        ResponseEntity<Book> response = bookController.update(bookId, bookUpdate);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedBook, response.getBody());

        verify(httpServletRequest).getHeader("Authorization");
        verify(updateUseCase).execute(any(UpdateUseCase.Input.class));
    }

    @Test
    void shouldDeleteBookSuccessfully() {
        // Arrange
        doNothing().when(deleteUseCase).execute(any(DeleteUseCase.Input.class));

        // Act
        ResponseEntity<Void> response = bookController.delete(bookId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(httpServletRequest).getHeader("Authorization");
        verify(deleteUseCase).execute(any(DeleteUseCase.Input.class));
    }

    @Test
    void shouldPassCorrectInputToCreateUseCase() {
        // Arrange
        CreateUseCase.Output output = CreateUseCase.Output.builder()
                .book(book)
                .build();

        when(createUseCase.execute(any(CreateUseCase.Input.class))).thenReturn(output);

        // Act
        bookController.create(bookCreate);

        // Assert
        verify(createUseCase).execute(argThat(input ->
                input.getJwt().equals(jwt) &&
                        input.getBookCreate().equals(bookCreate)
        ));
    }

    @Test
    void shouldPassCorrectInputToFindByIdUseCase() {
        // Arrange
        FindByIdUseCase.Output output = FindByIdUseCase.Output.builder()
                .book(book)
                .build();

        when(findByIdUseCase.execute(any(FindByIdUseCase.Input.class))).thenReturn(output);

        // Act
        bookController.findById(bookId);

        // Assert
        verify(findByIdUseCase).execute(argThat(input ->
                input.getJwt().equals(jwt) &&
                        input.getId().equals(bookId)
        ));
    }

    @Test
    void shouldPassCorrectInputToUpdateUseCase() {
        // Arrange
        UpdateUseCase.Output output = UpdateUseCase.Output.builder()
                .book(book)
                .build();

        when(updateUseCase.execute(any(UpdateUseCase.Input.class))).thenReturn(output);

        // Act
        bookController.update(bookId, bookUpdate);

        // Assert
        verify(updateUseCase).execute(argThat(input ->
                input.getJwt().equals(jwt) &&
                        input.getId().equals(bookId) &&
                        input.getBookUpdate().equals(bookUpdate)
        ));
    }

    @Test
    void shouldPassCorrectInputToDeleteUseCase() {
        // Arrange
        doNothing().when(deleteUseCase).execute(any(DeleteUseCase.Input.class));

        // Act
        bookController.delete(bookId);

        // Assert
        verify(deleteUseCase).execute(argThat(input ->
                input.getJwt().equals(jwt) &&
                        input.getId().equals(bookId)
        ));
    }
}
