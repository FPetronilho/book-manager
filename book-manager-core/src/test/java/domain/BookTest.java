package domain;

import com.tracktainment.bookmanager.domain.Book;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void shouldCreateBookUsingBuilder() {
        // Arrange
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
        Book book = Book.builder()
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
        assertEquals(id, book.getId());
        assertEquals(title, book.getTitle());
        assertEquals(author, book.getAuthor());
        assertEquals(genre, book.getGenre());
        assertEquals(isbn, book.getIsbn());
        assertEquals(publisher, book.getPublisher());
        assertEquals(publishedDate, book.getPublishedDate());
        assertEquals(language, book.getLanguage());
        assertEquals(createdAt, book.getCreatedAt());
        assertEquals(updatedAt, book.getUpdatedAt());
    }

    @Test
    void shouldCreateEmptyBookUsingNoArgsConstructor() {
        // Act
        Book book = new Book();

        // Assert
        assertNull(book.getId());
        assertNull(book.getTitle());
        assertNull(book.getAuthor());
        assertNull(book.getGenre());
        assertNull(book.getIsbn());
        assertNull(book.getPublisher());
        assertNull(book.getPublishedDate());
        assertNull(book.getLanguage());
        assertNull(book.getCreatedAt());
        assertNull(book.getUpdatedAt());
    }

    @Test
    void shouldUseSettersAndGetters() {
        // Arrange
        Book book = new Book();
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
        book.setId(id);
        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);
        book.setIsbn(isbn);
        book.setPublisher(publisher);
        book.setPublishedDate(publishedDate);
        book.setLanguage(language);
        book.setCreatedAt(createdAt);
        book.setUpdatedAt(updatedAt);

        // Assert
        assertEquals(id, book.getId());
        assertEquals(title, book.getTitle());
        assertEquals(author, book.getAuthor());
        assertEquals(genre, book.getGenre());
        assertEquals(isbn, book.getIsbn());
        assertEquals(publisher, book.getPublisher());
        assertEquals(publishedDate, book.getPublishedDate());
        assertEquals(language, book.getLanguage());
        assertEquals(createdAt, book.getCreatedAt());
        assertEquals(updatedAt, book.getUpdatedAt());
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        // Arrange
        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();

        Book book1 = Book.builder().id(id1).title("Book 1").build();
        Book book2 = Book.builder().id(id1).title("Book 1").build(); // Same ID
        Book book3 = Book.builder().id(id2).title("Book 1").build(); // Different ID

        // Assert
        assertEquals(book1, book2); // Same ID should be equal
        assertNotEquals(book1, book3); // Different ID should not be equal
        assertEquals(book1.hashCode(), book2.hashCode()); // Same ID should have same hashCode
        assertNotEquals(book1.hashCode(), book3.hashCode()); // Different ID should have different hashCode
    }

    @Test
    void shouldImplementToString() {
        // Arrange
        String id = UUID.randomUUID().toString();
        Book book = Book.builder()
                .id(id)
                .title("The Great Gatsby")
                .build();

        // Act
        String toString = book.toString();

        // Assert
        assertNotNull(toString);
        assertFalse(toString.isEmpty());
        assertTrue(toString.contains("Book"));
        assertTrue(toString.contains(id));
        assertTrue(toString.contains("The Great Gatsby"));
    }
}
