package usecases;

import com.tracktainment.bookmanager.dataprovider.BookDataProvider;
import com.tracktainment.bookmanager.dataprovider.DuxManagerDataProvider;
import com.tracktainment.bookmanager.domain.Book;
import com.tracktainment.bookmanager.dto.duxmanager.response.AssetResponse;
import com.tracktainment.bookmanager.exception.ResourceNotFoundException;
import com.tracktainment.bookmanager.security.context.DigitalUser;
import com.tracktainment.bookmanager.security.util.SecurityUtil;
import com.tracktainment.bookmanager.usecases.FindByIdUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import testutil.TestBookDataUtil;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindByIdUseCaseTest {

    @Mock
    private BookDataProvider bookDataProvider;

    @Mock
    private DuxManagerDataProvider duxManagerDataProvider;

    @Mock
    private SecurityUtil securityUtil;

    @InjectMocks
    private FindByIdUseCase findByIdUseCase;

    private Book book;
    private DigitalUser digitalUser;
    private AssetResponse assetResponse;
    private String jwt;
    private String bookId;

    @BeforeEach
    void setUp() {
        bookId = UUID.randomUUID().toString();
        book = TestBookDataUtil.createTestBook();
        digitalUser = TestBookDataUtil.createTestDigitalUser();
        assetResponse = TestBookDataUtil.createTestAssetResponse();
        jwt = "Bearer token";
    }

    @Test
    void shouldFindBookByIdSuccessfully() {
        // Arrange
        when(securityUtil.getDigitalUser()).thenReturn(digitalUser);
        when(duxManagerDataProvider.findAssetsByCriteria(
                eq(jwt),
                eq(digitalUser.getId()),
                eq(bookId),
                eq("com.tracktainment"),
                eq("book-manager"),
                eq("book"),
                isNull(),
                isNull(),
                isNull()
        )).thenReturn(Collections.singletonList(assetResponse));
        when(bookDataProvider.findById(bookId)).thenReturn(book);

        FindByIdUseCase.Input input = FindByIdUseCase.Input.builder()
                .jwt(jwt)
                .id(bookId)
                .build();

        // Act
        FindByIdUseCase.Output output = findByIdUseCase.execute(input);

        // Assert
        assertNotNull(output);
        assertEquals(book, output.getBook());

        verify(securityUtil).getDigitalUser();
        verify(duxManagerDataProvider).findAssetsByCriteria(
                eq(jwt),
                eq(digitalUser.getId()),
                eq(bookId),
                eq("com.tracktainment"),
                eq("book-manager"),
                eq("book"),
                isNull(),
                isNull(),
                isNull()
        );
        verify(bookDataProvider).findById(bookId);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenAssetNotFound() {
        // Arrange
        when(securityUtil.getDigitalUser()).thenReturn(digitalUser);
        when(duxManagerDataProvider.findAssetsByCriteria(
                eq(jwt),
                eq(digitalUser.getId()),
                eq(bookId),
                eq("com.tracktainment"),
                eq("book-manager"),
                eq("book"),
                isNull(),
                isNull(),
                isNull()
        )).thenReturn(Collections.emptyList());

        FindByIdUseCase.Input input = FindByIdUseCase.Input.builder()
                .jwt(jwt)
                .id(bookId)
                .build();

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> findByIdUseCase.execute(input));

        verify(securityUtil).getDigitalUser();
        verify(duxManagerDataProvider).findAssetsByCriteria(
                eq(jwt),
                eq(digitalUser.getId()),
                eq(bookId),
                eq("com.tracktainment"),
                eq("book-manager"),
                eq("book"),
                isNull(),
                isNull(),
                isNull()
        );
        verify(bookDataProvider, never()).findById(any());
    }

    @Test
    void shouldPropagateBookDataProviderExceptions() {
        // Arrange
        when(securityUtil.getDigitalUser()).thenReturn(digitalUser);
        when(duxManagerDataProvider.findAssetsByCriteria(
                eq(jwt),
                eq(digitalUser.getId()),
                eq(bookId),
                eq("com.tracktainment"),
                eq("book-manager"),
                eq("book"),
                isNull(),
                isNull(),
                isNull()
        )).thenReturn(Collections.singletonList(assetResponse));
        when(bookDataProvider.findById(bookId)).thenThrow(new ResourceNotFoundException(Book.class, bookId));

        FindByIdUseCase.Input input = FindByIdUseCase.Input.builder()
                .jwt(jwt)
                .id(bookId)
                .build();

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> findByIdUseCase.execute(input));

        verify(securityUtil).getDigitalUser();
        verify(duxManagerDataProvider).findAssetsByCriteria(
                eq(jwt),
                eq(digitalUser.getId()),
                eq(bookId),
                eq("com.tracktainment"),
                eq("book-manager"),
                eq("book"),
                isNull(),
                isNull(),
                isNull()
        );
        verify(bookDataProvider).findById(bookId);
    }
}
