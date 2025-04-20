package usecases;

import com.tracktainment.bookmanager.dataprovider.BookDataProvider;
import com.tracktainment.bookmanager.dataprovider.DuxManagerDataProvider;
import com.tracktainment.bookmanager.domain.Book;
import com.tracktainment.bookmanager.exception.AuthenticationFailedException;
import com.tracktainment.bookmanager.exception.ResourceNotFoundException;
import com.tracktainment.bookmanager.security.context.DigitalUser;
import com.tracktainment.bookmanager.security.util.SecurityUtil;
import com.tracktainment.bookmanager.usecases.DeleteUseCase;
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
class DeleteUseCaseTest {

    @Mock
    private BookDataProvider bookDataProvider;

    @Mock
    private DuxManagerDataProvider duxManagerDataProvider;

    @Mock
    private SecurityUtil securityUtil;

    @InjectMocks
    private DeleteUseCase deleteUseCase;

    private DigitalUser digitalUser;
    private String jwt;
    private String bookId;

    @BeforeEach
    void setUp() {
        bookId = UUID.randomUUID().toString();
        digitalUser = TestBookDataUtil.createTestDigitalUser();
        jwt = "Bearer token";
    }

    @Test
    void shouldDeleteBookSuccessfully() {
        // Arrange
        when(securityUtil.getDigitalUser()).thenReturn(digitalUser);
        doNothing().when(duxManagerDataProvider).deleteAssetByExternalId(jwt, digitalUser.getId(), bookId);
        doNothing().when(bookDataProvider).delete(bookId);

        DeleteUseCase.Input input = DeleteUseCase.Input.builder()
                .jwt(jwt)
                .id(bookId)
                .build();

        // Act & Assert
        assertDoesNotThrow(() -> deleteUseCase.execute(input));

        verify(securityUtil).getDigitalUser();
        verify(duxManagerDataProvider).deleteAssetByExternalId(jwt, digitalUser.getId(), bookId);
        verify(bookDataProvider).delete(bookId);
    }

    @Test
    void shouldPropagateExceptionFromDuxManagerDataProvider() {
        // Arrange
        when(securityUtil.getDigitalUser()).thenReturn(digitalUser);
        doThrow(new RuntimeException("Failed to delete asset")).when(duxManagerDataProvider)
                .deleteAssetByExternalId(jwt, digitalUser.getId(), bookId);

        DeleteUseCase.Input input = DeleteUseCase.Input.builder()
                .jwt(jwt)
                .id(bookId)
                .build();

        // Act & Assert
        assertThrows(RuntimeException.class, () -> deleteUseCase.execute(input));

        verify(securityUtil).getDigitalUser();
        verify(duxManagerDataProvider).deleteAssetByExternalId(jwt, digitalUser.getId(), bookId);
        verify(bookDataProvider, never()).delete(any());
    }

    @Test
    void shouldPropagateExceptionFromBookDataProvider() {
        // Arrange
        when(securityUtil.getDigitalUser()).thenReturn(digitalUser);
        doNothing().when(duxManagerDataProvider).deleteAssetByExternalId(jwt, digitalUser.getId(), bookId);
        doThrow(new ResourceNotFoundException(Book.class, bookId)).when(bookDataProvider).delete(bookId);

        DeleteUseCase.Input input = DeleteUseCase.Input.builder()
                .jwt(jwt)
                .id(bookId)
                .build();

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> deleteUseCase.execute(input));

        verify(securityUtil).getDigitalUser();
        verify(duxManagerDataProvider).deleteAssetByExternalId(jwt, digitalUser.getId(), bookId);
        verify(bookDataProvider).delete(bookId);
    }

    @Test
    void shouldPropagateExceptionFromSecurityUtil() {
        // Arrange
        when(securityUtil.getDigitalUser()).thenThrow(new AuthenticationFailedException("JWT not found in security context"));

        DeleteUseCase.Input input = DeleteUseCase.Input.builder()
                .jwt(jwt)
                .id(bookId)
                .build();

        // Act & Assert
        assertThrows(AuthenticationFailedException.class, () -> deleteUseCase.execute(input));

        verify(securityUtil).getDigitalUser();
        verify(duxManagerDataProvider, never()).deleteAssetByExternalId(any(), any(), any());
        verify(bookDataProvider, never()).delete(any());
    }
}
