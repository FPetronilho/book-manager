package mapper;

import com.tracktainment.bookmanager.domain.Book;
import com.tracktainment.bookmanager.dto.BookCreate;
import com.tracktainment.bookmanager.dto.BookUpdate;
import com.tracktainment.bookmanager.entity.BookEntity;
import com.tracktainment.bookmanager.mapper.BookMapperDataProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;
import testutil.TestBookDataUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookMapperDataProviderTest {

    private final BookMapperDataProvider mapper = Mappers.getMapper(BookMapperDataProvider.class);

    // UUID pattern matcher
    private static final Pattern UUID_PATTERN =
            Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    @Test
    void shouldMapBookEntityToBook() {
        // Arrange
        String id = UUID.randomUUID().toString();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now().plusDays(1);
        LocalDate publishedDate = LocalDate.of(1925, 4, 10);

        BookEntity bookEntity = BookEntity.builder()
                .id(id)
                .title("The Great Gatsby")
                .author("F. Scott Fitzgerald")
                .genre("Drama")
                .isbn("978-3-16-148410-0")
                .publisher("Penguin Books")
                .publishedDate(publishedDate)
                .language("English")
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // Act
        Book result = mapper.toBook(bookEntity);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("The Great Gatsby", result.getTitle());
        assertEquals("F. Scott Fitzgerald", result.getAuthor());
        assertEquals("Drama", result.getGenre());
        assertEquals("978-3-16-148410-0", result.getIsbn());
        assertEquals("Penguin Books", result.getPublisher());
        assertEquals(publishedDate, result.getPublishedDate());
        assertEquals("English", result.getLanguage());
        assertEquals(createdAt, result.getCreatedAt());
        assertEquals(updatedAt, result.getUpdatedAt());
    }

    @Test
    void shouldMapBookCreateToBookEntity() {
        // Arrange
        BookCreate bookCreate = TestBookDataUtil.createTestBookCreate();

        // Act
        BookEntity result = mapper.toBookEntity(bookCreate);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertTrue(UUID_PATTERN.matcher(result.getId()).matches(), "ID should be a valid UUID");
        assertEquals(bookCreate.getTitle(), result.getTitle());
        assertEquals(bookCreate.getAuthor(), result.getAuthor());
        assertEquals(bookCreate.getIsbn(), result.getIsbn());
        assertEquals(bookCreate.getPublisher(), result.getPublisher());
        assertEquals(bookCreate.getPublishedDate(), result.getPublishedDate());
        assertEquals(bookCreate.getLanguage(), result.getLanguage());

        // These should be null as specified in @Mapping annotations
        assertNull(result.getDbId());
        assertNull(result.getCreatedAt());
        assertNull(result.getUpdatedAt());
    }

    @Test
    void shouldUpdateBookEntityFromBookUpdate() {
        // Arrange
        String id = UUID.randomUUID().toString();
        BookEntity bookEntity = BookEntity.builder()
                .id(id)
                .title("The Great Gatsby")
                .author("F. Scott Fitzgerald")
                .genre("Drama")
                .isbn("978-3-16-148410-0")
                .publisher("Penguin Books")
                .publishedDate(LocalDate.of(1925, 4, 10))
                .language("English")
                .build();

        BookUpdate bookUpdate = TestBookDataUtil.createTestBookUpdate();

        // Act
        mapper.updateBookEntity(bookEntity, bookUpdate);

        // Assert
        assertEquals(id, bookEntity.getId()); // ID should not change
        assertEquals(bookUpdate.getTitle(), bookEntity.getTitle());
        assertEquals(bookUpdate.getAuthor(), bookEntity.getAuthor());
        assertEquals(bookUpdate.getIsbn(), bookEntity.getIsbn());
        assertEquals(bookUpdate.getPublisher(), bookEntity.getPublisher());
        assertEquals(bookUpdate.getPublishedDate(), bookEntity.getPublishedDate());
        assertEquals(bookUpdate.getLanguage(), bookEntity.getLanguage());

        // These should remain unchanged as specified in @Mapping annotations
        assertNull(bookEntity.getDbId());
        assertNull(bookEntity.getCreatedAt());
        assertNull(bookEntity.getUpdatedAt());
    }

    @Test
    void shouldHandleNullBookUpdateValues() {
        // Arrange
        String id = UUID.randomUUID().toString();
        LocalDate publishedDate = LocalDate.of(1925, 4, 10);
        BookEntity bookEntity = BookEntity.builder()
                .id(id)
                .title("The Great Gatsby")
                .author("F. Scott Fitzgerald")
                .genre("Drama")
                .isbn("978-3-16-148410-0")
                .publisher("Penguin Books")
                .publishedDate(publishedDate)
                .language("English")
                .build();

        BookUpdate bookUpdate = BookUpdate.builder()
                .title("The Great Gatsby: Special Edition")
                // All other fields null
                .build();

        // Act
        mapper.updateBookEntity(bookEntity, bookUpdate);

        // Assert
        assertEquals(id, bookEntity.getId()); // ID should not change
        assertEquals("The Great Gatsby: Special Edition", bookEntity.getTitle());
        assertEquals("F. Scott Fitzgerald", bookEntity.getAuthor()); // Should not change
        assertEquals("Drama", bookEntity.getGenre()); // Should not change
        assertEquals("978-3-16-148410-0", bookEntity.getIsbn()); // Should not change
        assertEquals("Penguin Books", bookEntity.getPublisher()); // Should not change
        assertEquals(publishedDate, bookEntity.getPublishedDate()); // Should not change
        assertEquals("English", bookEntity.getLanguage()); // Should not change
    }

    @Test
    void shouldHandleNullInput() {
        // Act & Assert
        assertNull(mapper.toBook(null));
    }

    @Test
    void shouldGenerateDifferentIdsForDifferentCalls() {
        // Arrange
        BookCreate bookCreate = TestBookDataUtil.createTestBookCreate();

        // Act
        BookEntity result1 = mapper.toBookEntity(bookCreate);
        BookEntity result2 = mapper.toBookEntity(bookCreate);

        // Assert
        assertNotNull(result1.getId());
        assertNotNull(result2.getId());
        assertNotEquals(result1.getId(), result2.getId(), "Generated IDs should be different");
    }
}
