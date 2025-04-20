package usecases;

import com.tracktainment.bookmanager.dataprovider.BookDataProvider;
import com.tracktainment.bookmanager.dataprovider.DuxManagerDataProvider;
import com.tracktainment.bookmanager.domain.Book;
import com.tracktainment.bookmanager.dto.BookCreate;
import com.tracktainment.bookmanager.dto.duxmanager.request.AssetRequest;
import com.tracktainment.bookmanager.dto.duxmanager.response.AssetResponse;
import com.tracktainment.bookmanager.exception.ResourceAlreadyExistsException;
import com.tracktainment.bookmanager.security.context.DigitalUser;
import com.tracktainment.bookmanager.security.util.SecurityUtil;
import com.tracktainment.bookmanager.usecases.CreateUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import testutil.TestBookDataUtil;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUseCaseTest {

    @Mock
    private BookDataProvider bookDataProvider;

    @Mock
    private DuxManagerDataProvider duxManagerDataProvider;

    @Mock
    private SecurityUtil securityUtil;

    @InjectMocks
    private CreateUseCase createUseCase;

    private BookCreate bookCreate;
    private Book book;
    private DigitalUser digitalUser;
    private AssetResponse assetResponse;
    private String jwt;

    @BeforeEach
    void setUp() {
        bookCreate = TestBookDataUtil.createTestBookCreate();
        book = TestBookDataUtil.createTestBook();
        digitalUser = TestBookDataUtil.createTestDigitalUser();
        assetResponse = TestBookDataUtil.createTestAssetResponse();
        jwt = "Bearer token";
    }

    @Test
    void shouldCreateBookSuccessfully() {
        // Arrange
        when(bookDataProvider.create(any(BookCreate.class)))
                .thenReturn(book);
        when(securityUtil.getDigitalUser())
                .thenReturn(digitalUser);
        when(duxManagerDataProvider.createAsset(eq(jwt), eq(digitalUser.getId()), any(AssetRequest.class)))
                .thenReturn(assetResponse);

        CreateUseCase.Input input = CreateUseCase.Input.builder()
                .jwt(jwt)
                .bookCreate(bookCreate)
                .build();

        // Act
        CreateUseCase.Output output = createUseCase.execute(input);

        // Assert
        assertNotNull(output);
        assertEquals(book, output.getBook());

        verify(bookDataProvider).create(bookCreate);
        verify(securityUtil).getDigitalUser();
        verify(duxManagerDataProvider).createAsset(eq(jwt), eq(digitalUser.getId()), any(AssetRequest.class));
    }

    @Test
    void shouldRollbackBookCreationWhenAssetCreationFails() {
        // Arrange
        when(bookDataProvider.create(any(BookCreate.class)))
                .thenReturn(book);
        when(securityUtil.getDigitalUser())
                .thenReturn(digitalUser);
        when(duxManagerDataProvider.createAsset(eq(jwt), eq(digitalUser.getId()), any(AssetRequest.class)))
                .thenThrow(new RuntimeException("Failed to create asset"));

        CreateUseCase.Input input = CreateUseCase.Input.builder()
                .jwt(jwt)
                .bookCreate(bookCreate)
                .build();

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> createUseCase.execute(input));
        assertEquals("Failed to create asset", exception.getMessage());

        verify(bookDataProvider).create(bookCreate);
        verify(securityUtil).getDigitalUser();
        verify(duxManagerDataProvider).createAsset(eq(jwt), eq(digitalUser.getId()), any(AssetRequest.class));
        verify(bookDataProvider).delete(book.getId());
    }

    @Test
    void shouldPropagateBookDataProviderExceptions() {
        // Arrange
        when(bookDataProvider.create(any(BookCreate.class)))
                .thenThrow(new ResourceAlreadyExistsException(Book.class, bookCreate.getTitle()));

        CreateUseCase.Input input = CreateUseCase.Input.builder()
                .jwt(jwt)
                .bookCreate(bookCreate)
                .build();

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> createUseCase.execute(input));

        verify(bookDataProvider).create(bookCreate);
        verify(securityUtil, never()).getDigitalUser();
        verify(duxManagerDataProvider, never()).createAsset(any(), any(), any());
        verify(bookDataProvider, never()).delete(any());
    }
}
