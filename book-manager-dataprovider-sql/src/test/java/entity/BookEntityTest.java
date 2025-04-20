package entity;

import com.tracktainment.bookmanager.entity.BaseEntity;
import com.tracktainment.bookmanager.entity.BookEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BookEntityTest {

    @Test
    void shouldCreateBookEntityUsingBuilder() {
        // Arrange
        Long dbId = 1L;
        String id = UUID.randomUUID().toString();
        String title = "The Great Gatsby";
        String author = "F. Scott Fitzgerald";
        String genre = "Drama";
        String isbn = "978-3-16-148410-0";
        String publisher = "Penguin Books";
        LocalDate publishedDate = LocalDate.of(1925, 4, 10);
        String language = "English";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now().plusDays(1);

        // Act
        BookEntity bookEntity = BookEntity.builder()
                .dbId(dbId)
                .id(id)
                .title(title)
                .author(author)
                .genre(genre)
                .isbn(isbn)
                .publisher(publisher)
                .publishedDate(publishedDate)
                .language(language)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // Assert
        assertEquals(dbId, bookEntity.getDbId());
        assertEquals(id, bookEntity.getId());
        assertEquals(title, bookEntity.getTitle());
        assertEquals(author, bookEntity.getAuthor());
        assertEquals(genre, bookEntity.getGenre());
        assertEquals(isbn, bookEntity.getIsbn());
        assertEquals(publisher, bookEntity.getPublisher());
        assertEquals(publishedDate, bookEntity.getPublishedDate());
        assertEquals(language, bookEntity.getLanguage());
        assertEquals(createdAt, bookEntity.getCreatedAt());
        assertEquals(updatedAt, bookEntity.getUpdatedAt());
    }

    @Test
    void shouldCreateEmptyBookEntityUsingNoArgsConstructor() {
        // Act
        BookEntity bookEntity = new BookEntity();

        // Assert
        assertNull(bookEntity.getDbId());
        assertNull(bookEntity.getId());
        assertNull(bookEntity.getTitle());
        assertNull(bookEntity.getAuthor());
        assertNull(bookEntity.getGenre());
        assertNull(bookEntity.getIsbn());
        assertNull(bookEntity.getPublisher());
        assertNull(bookEntity.getPublishedDate());
        assertNull(bookEntity.getLanguage());
        assertNull(bookEntity.getCreatedAt());
        assertNull(bookEntity.getUpdatedAt());
    }

    @Test
    void shouldUseSettersAndGetters() {
        // Arrange
        BookEntity bookEntity = new BookEntity();
        Long dbId = 2L;
        String id = UUID.randomUUID().toString();
        String title = "The Great Gatsby";
        String author = "F. Scott Fitzgerald";
        String genre = "Drama";
        String isbn = "978-3-16-148410-0";
        String publisher = "Penguin Books";
        LocalDate publishedDate = LocalDate.of(1925, 4, 10);
        String language = "English";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now().plusDays(1);

        // Act
        bookEntity.setDbId(dbId);
        bookEntity.setId(id);
        bookEntity.setTitle(title);
        bookEntity.setAuthor(author);
        bookEntity.setGenre(genre);
        bookEntity.setIsbn(isbn);
        bookEntity.setPublisher(publisher);
        bookEntity.setPublishedDate(publishedDate);
        bookEntity.setLanguage(language);
        bookEntity.setCreatedAt(createdAt);
        bookEntity.setUpdatedAt(updatedAt);

        // Assert
        assertEquals(dbId, bookEntity.getDbId());
        assertEquals(id, bookEntity.getId());
        assertEquals(title, bookEntity.getTitle());
        assertEquals(author, bookEntity.getAuthor());
        assertEquals(genre, bookEntity.getGenre());
        assertEquals(isbn, bookEntity.getIsbn());
        assertEquals(publisher, bookEntity.getPublisher());
        assertEquals(publishedDate, bookEntity.getPublishedDate());
        assertEquals(language, bookEntity.getLanguage());
        assertEquals(createdAt, bookEntity.getCreatedAt());
        assertEquals(updatedAt, bookEntity.getUpdatedAt());
    }

    @Test
    void shouldInheritFromBaseEntity() {
        // Arrange
        BookEntity bookEntity = new BookEntity();

        // Assert
        assertTrue(bookEntity instanceof BaseEntity);
    }

    @Test
    void shouldImplementEqualsAndHashCodeCorrectly() {
        // Arrange
        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();

        BookEntity entity1 = BookEntity.builder().id(id1).build();
        BookEntity entity2 = BookEntity.builder().id(id1).build(); // Same ID
        BookEntity entity3 = BookEntity.builder().id(id2).build(); // Different ID

        // Assert
        assertEquals(entity1, entity2); // Same ID should be equal
        assertNotEquals(entity1, entity3); // Different ID should not be equal
        assertEquals(entity1.hashCode(), entity2.hashCode()); // Same ID should have same hashCode
        assertNotEquals(entity1.hashCode(), entity3.hashCode()); // Different ID should have different hashCode
    }

    @Test
    void shouldImplementToString() {
        // Arrange
        String id = UUID.randomUUID().toString();
        BookEntity bookEntity = BookEntity.builder()
                .id(id)
                .title("The Great Gatsby")
                .build();

        // Act
        String toString = bookEntity.toString();

        // Assert
        assertNotNull(toString);
        assertFalse(toString.isEmpty());
        assertTrue(toString.contains("BookEntity"));
        assertTrue(toString.contains(id));
        assertTrue(toString.contains("The Great Gatsby"));
    }

    @Test
    void shouldCreateBookEntityWithAllArgsConstructor() {
        // Arrange
        Long dbId = 3L;
        String id = UUID.randomUUID().toString();
        String title = "To Kill a Mockingbird";
        String author = "Harper Lee";
        String genre = "Fiction";
        String isbn = "978-0-06-112008-4";
        String publisher = "HarperCollins";
        LocalDate publishedDate = LocalDate.of(1960, 7, 11);
        String language = "English";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now().plusDays(1);

        // Create a base entity first to pass to the constructor
        BaseEntity baseEntity = new BaseEntity(dbId, createdAt, updatedAt);

        // Act
        BookEntity bookEntity = new BookEntity(
                id,
                title,
                author,
                genre,
                isbn,
                publisher,
                publishedDate,
                language
        );

        // Set the base entity properties
        bookEntity.setDbId(baseEntity.getDbId());
        bookEntity.setCreatedAt(baseEntity.getCreatedAt());
        bookEntity.setUpdatedAt(baseEntity.getUpdatedAt());

        // Assert
        assertEquals(dbId, bookEntity.getDbId());
        assertEquals(id, bookEntity.getId());
        assertEquals(title, bookEntity.getTitle());
        assertEquals(author, bookEntity.getAuthor());
        assertEquals(genre, bookEntity.getGenre());
        assertEquals(isbn, bookEntity.getIsbn());
        assertEquals(publisher, bookEntity.getPublisher());
        assertEquals(publishedDate, bookEntity.getPublishedDate());
        assertEquals(language, bookEntity.getLanguage());
        assertEquals(createdAt, bookEntity.getCreatedAt());
        assertEquals(updatedAt, bookEntity.getUpdatedAt());
    }
}
