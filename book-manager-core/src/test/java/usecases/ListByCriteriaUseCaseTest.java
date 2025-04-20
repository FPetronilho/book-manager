package usecases;

import com.tracktainment.bookmanager.dataprovider.BookDataProvider;
import com.tracktainment.bookmanager.dataprovider.DuxManagerDataProvider;
import com.tracktainment.bookmanager.domain.Book;
import com.tracktainment.bookmanager.domain.OrderBy;
import com.tracktainment.bookmanager.domain.OrderDirection;
import com.tracktainment.bookmanager.dto.duxmanager.response.AssetResponse;
import com.tracktainment.bookmanager.security.context.DigitalUser;
import com.tracktainment.bookmanager.security.util.SecurityUtil;
import com.tracktainment.bookmanager.usecases.ListByCriteriaUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import testutil.TestBookDataUtil;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListByCriteriaUseCaseTest {

    @Mock
    private BookDataProvider bookDataProvider;

    @Mock
    private DuxManagerDataProvider duxManagerDataProvider;

    @Mock
    private SecurityUtil securityUtil;

    @InjectMocks
    private ListByCriteriaUseCase listByCriteriaUseCase;

    private Book book1;
    private Book book2;
    private DigitalUser digitalUser;
    private AssetResponse assetResponse1;
    private AssetResponse assetResponse2;
    private String jwt;
    private List<Book> books;
    private List<AssetResponse> assetResponses;

    @BeforeEach
    void setUp() {
        book1 = TestBookDataUtil.createTestBook();
        book2 = Book.builder()
                .id(UUID.randomUUID().toString())
                .title("To Kill a Mockingbird")
                .author("Harper Lee")
                .genre("Fiction")
                .publisher("HarperCollins")
                .publishedDate(LocalDate.of(1960, 7, 11))
                .build();

        digitalUser = TestBookDataUtil.createTestDigitalUser();

        assetResponse1 = AssetResponse.builder()
                .externalId(book1.getId())
                .type("book")
                .build();

        assetResponse2 = AssetResponse.builder()
                .externalId(book2.getId())
                .type("book")
                .build();

        jwt = "Bearer token";

        books = Arrays.asList(book1, book2);
        assetResponses = Arrays.asList(assetResponse1, assetResponse2);
    }

    @Test
    void shouldListBooksByCriteriaSuccessfully() {
        // Arrange
        when(securityUtil.getDigitalUser()).thenReturn(digitalUser);
        when(duxManagerDataProvider.findAssetsByCriteria(
                eq(jwt),
                eq(digitalUser.getId()),
                isNull(),
                eq("com.tracktainment"),
                eq("book-manager"),
                eq("book"),
                isNull(),
                isNull(),
                isNull())
        ).thenReturn(assetResponses);

        when(bookDataProvider.listByCriteria(any(ListByCriteriaUseCase.Input.class))).thenReturn(books);

        List<OrderBy> orderByList = Collections.singletonList(OrderBy.TITLE);
        List<OrderDirection> orderDirectionList = Collections.singletonList(OrderDirection.ASC);

        ListByCriteriaUseCase.Input input = ListByCriteriaUseCase.Input.builder()
                .jwt(jwt)
                .offset(0)
                .limit(10)
                .orderByList(orderByList)
                .orderDirectionList(orderDirectionList)
                .build();

        // Act
        ListByCriteriaUseCase.Output output = listByCriteriaUseCase.execute(input);

        // Assert
        assertNotNull(output);
        assertEquals(books, output.getBooks());
        assertEquals(2, output.getBooks().size());

        verify(securityUtil).getDigitalUser();
        verify(duxManagerDataProvider).findAssetsByCriteria(
                eq(jwt),
                eq(digitalUser.getId()),
                isNull(),
                eq("com.tracktainment"),
                eq("book-manager"),
                eq("book"),
                isNull(),
                isNull(),
                isNull()
        );
        verify(bookDataProvider).listByCriteria(any(ListByCriteriaUseCase.Input.class));
    }

    @Test
    void shouldHandleEmptyAssetsList() {
        // Arrange
        when(securityUtil.getDigitalUser()).thenReturn(digitalUser);
        when(duxManagerDataProvider.findAssetsByCriteria(
                any(), any(), any(), any(), any(), any(), any(), any(), any())
        ).thenReturn(Collections.emptyList());

        when(bookDataProvider.listByCriteria(any(ListByCriteriaUseCase.Input.class))).thenReturn(Collections.emptyList());

        ListByCriteriaUseCase.Input input = ListByCriteriaUseCase.Input.builder()
                .jwt(jwt)
                .offset(0)
                .limit(10)
                .build();

        // Act
        ListByCriteriaUseCase.Output output = listByCriteriaUseCase.execute(input);

        // Assert
        assertNotNull(output);
        assertNotNull(output.getBooks());
        assertTrue(output.getBooks().isEmpty());

        verify(securityUtil).getDigitalUser();
        verify(duxManagerDataProvider).findAssetsByCriteria(
                eq(jwt),
                eq(digitalUser.getId()),
                isNull(),
                eq("com.tracktainment"),
                eq("book-manager"),
                eq("book"),
                isNull(),
                isNull(),
                isNull()
        );
        verify(bookDataProvider).listByCriteria(any(ListByCriteriaUseCase.Input.class));
    }

    @Test
    void shouldFilterBySpecificId() {
        // Arrange
        String specificBookId = book1.getId();
        when(securityUtil.getDigitalUser()).thenReturn(digitalUser);
        when(duxManagerDataProvider.findAssetsByCriteria(
                eq(jwt),
                eq(digitalUser.getId()),
                eq(specificBookId),
                eq("com.tracktainment"),
                eq("book-manager"),
                eq("book"),
                isNull(),
                isNull(),
                isNull())
        ).thenReturn(Collections.singletonList(assetResponse1));

        when(bookDataProvider.listByCriteria(any(ListByCriteriaUseCase.Input.class))).thenReturn(Collections.singletonList(book1));

        ListByCriteriaUseCase.Input input = ListByCriteriaUseCase.Input.builder()
                .jwt(jwt)
                .offset(0)
                .limit(10)
                .ids(specificBookId)
                .build();

        // Act
        ListByCriteriaUseCase.Output output = listByCriteriaUseCase.execute(input);

        // Assert
        assertNotNull(output);
        assertEquals(1, output.getBooks().size());
        assertEquals(book1, output.getBooks().get(0));

        verify(securityUtil).getDigitalUser();
        verify(duxManagerDataProvider).findAssetsByCriteria(
                eq(jwt),
                eq(digitalUser.getId()),
                eq(specificBookId),
                eq("com.tracktainment"),
                eq("book-manager"),
                eq("book"),
                isNull(),
                isNull(),
                isNull()
        );
        verify(bookDataProvider).listByCriteria(any(ListByCriteriaUseCase.Input.class));
    }

    @Test
    void shouldFilterByDateRange() {
        // Arrange
        LocalDate from = LocalDate.now().minusDays(7);
        LocalDate to = LocalDate.now();

        when(securityUtil.getDigitalUser()).thenReturn(digitalUser);
        when(duxManagerDataProvider.findAssetsByCriteria(
                eq(jwt),
                eq(digitalUser.getId()),
                isNull(),
                eq("com.tracktainment"),
                eq("book-manager"),
                eq("book"),
                isNull(),
                eq(from),
                eq(to))
        ).thenReturn(assetResponses);

        when(bookDataProvider.listByCriteria(any(ListByCriteriaUseCase.Input.class))).thenReturn(books);

        ListByCriteriaUseCase.Input input = ListByCriteriaUseCase.Input.builder()
                .jwt(jwt)
                .offset(0)
                .limit(10)
                .from(from)
                .to(to)
                .build();

        // Act
        ListByCriteriaUseCase.Output output = listByCriteriaUseCase.execute(input);

        // Assert
        assertNotNull(output);
        assertEquals(2, output.getBooks().size());

        verify(securityUtil).getDigitalUser();
        verify(duxManagerDataProvider).findAssetsByCriteria(
                eq(jwt),
                eq(digitalUser.getId()),
                isNull(),
                eq("com.tracktainment"),
                eq("book-manager"),
                eq("book"),
                isNull(),
                eq(from),
                eq(to)
        );
        verify(bookDataProvider).listByCriteria(any(ListByCriteriaUseCase.Input.class));
    }
}
