package usecases;

import com.tracktainment.bookmanager.dataprovider.BookDataProvider;
import com.tracktainment.bookmanager.domain.Book;
import com.tracktainment.bookmanager.dto.BookUpdate;
import com.tracktainment.bookmanager.exception.ResourceNotFoundException;
import com.tracktainment.bookmanager.usecases.FindByIdUseCase;
import com.tracktainment.bookmanager.usecases.UpdateUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import testutil.TestBookDataUtil;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUseCaseTest {

    @Mock
    private BookDataProvider bookDataProvider;

    @Mock
    private FindByIdUseCase findByIdUseCase;

    @InjectMocks
    private UpdateUseCase updateUseCase;

    private Book book;
    private Book updatedBook;
    private BookUpdate bookUpdate;
    private String jwt;
    private String bookId;

    @BeforeEach
    void setUp() {
        bookId = UUID.randomUUID().toString();
        book = TestBookDataUtil.createTestBook();
        bookUpdate = TestBookDataUtil.createTestBookUpdate();
        updatedBook = TestBookDataUtil.createTestBookWithUpdate();
        jwt = "Bearer token";
    }

    @Test
    void shouldUpdateBookSuccessfully() {
        // Arrange
        when(findByIdUseCase.execute(any(FindByIdUseCase.Input.class))).thenReturn(
                FindByIdUseCase.Output.builder().book(book).build()
        );

        when(bookDataProvider.update(eq(bookId), eq(bookUpdate))).thenReturn(updatedBook);

        UpdateUseCase.Input input = UpdateUseCase.Input.builder()
                .jwt(jwt)
                .id(bookId)
                .bookUpdate(bookUpdate)
                .build();

        // Act
        UpdateUseCase.Output output = updateUseCase.execute(input);

        // Assert
        assertNotNull(output);
        assertEquals(updatedBook, output.getBook());

        verify(findByIdUseCase).execute(any(FindByIdUseCase.Input.class));
        verify(bookDataProvider).update(bookId, bookUpdate);
    }

    @Test
    void shouldPropagateExceptionWhenBookNotFound() {
        // Arrange
        when(findByIdUseCase.execute(any(FindByIdUseCase.Input.class)))
                .thenThrow(new ResourceNotFoundException(Book.class, bookId));

        UpdateUseCase.Input input = UpdateUseCase.Input.builder()
                .jwt(jwt)
                .id(bookId)
                .bookUpdate(bookUpdate)
                .build();

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> updateUseCase.execute(input));

        verify(findByIdUseCase).execute(any(FindByIdUseCase.Input.class));
        verify(bookDataProvider, never()).update(any(), any());
    }

    @Test
    void shouldPropagateExceptionFromBookDataProvider() {
        // Arrange
        when(findByIdUseCase.execute(any(FindByIdUseCase.Input.class))).thenReturn(
                FindByIdUseCase.Output.builder().book(book).build()
        );

        when(bookDataProvider.update(eq(bookId), eq(bookUpdate)))
                .thenThrow(new RuntimeException("Database error"));

        UpdateUseCase.Input input = UpdateUseCase.Input.builder()
                .jwt(jwt)
                .id(bookId)
                .bookUpdate(bookUpdate)
                .build();

        // Act & Assert
        assertThrows(RuntimeException.class, () -> updateUseCase.execute(input));

        verify(findByIdUseCase).execute(any(FindByIdUseCase.Input.class));
        verify(bookDataProvider).update(bookId, bookUpdate);
    }

    @Test
    void shouldPassTheCorrectInputToFindByIdUseCase() {
        // Arrange
        when(findByIdUseCase.execute(any(FindByIdUseCase.Input.class))).thenReturn(
                FindByIdUseCase.Output.builder().book(book).build()
        );

        when(bookDataProvider.update(eq(bookId), eq(bookUpdate))).thenReturn(updatedBook);

        UpdateUseCase.Input input = UpdateUseCase.Input.builder()
                .jwt(jwt)
                .id(bookId)
                .bookUpdate(bookUpdate)
                .build();

        // Act
        updateUseCase.execute(input);

        // Assert
        verify(findByIdUseCase).execute(argThat(findByIdInput ->
                findByIdInput.getJwt().equals(jwt) &&
                        findByIdInput.getId().equals(bookId)
        ));
    }
}
