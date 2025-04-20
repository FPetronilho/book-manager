package dto;

import com.tracktainment.bookmanager.dto.BookCreate;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BookCreateTest {

    @Test
    void shouldCreateBookCreateUsingBuilder() {
        // Arrange
        String title = "The Great Gatsby";
        String author = "F. Scott Fitzgerald";
        String isbn = "978-3-16-148410-0";
        String publisher = "Penguin Books";
        LocalDate publishedDate = LocalDate.of(1925, 4, 10);
        String language = "English";

        // Act
        BookCreate bookCreate = BookCreate.builder()
                .title(title)
                .author(author)
                .isbn(isbn)
                .publisher(publisher)
                .publishedDate(publishedDate)
                .language(language)
                .build();

        // Assert
        assertEquals(title, bookCreate.getTitle());
        assertEquals(author, bookCreate.getAuthor());
        assertEquals(isbn, bookCreate.getIsbn());
        assertEquals(publisher, bookCreate.getPublisher());
        assertEquals(publishedDate, bookCreate.getPublishedDate());
        assertEquals(language, bookCreate.getLanguage());
    }

    @Test
    void shouldCreateEmptyBookCreateUsingNoArgsConstructor() {
        // Act
        BookCreate bookCreate = new BookCreate();

        // Assert
        assertNull(bookCreate.getTitle());
        assertNull(bookCreate.getAuthor());
        assertNull(bookCreate.getIsbn());
        assertNull(bookCreate.getPublisher());
        assertNull(bookCreate.getPublishedDate());
        assertNull(bookCreate.getLanguage());
    }

    @Test
    void shouldUseSettersAndGetters() {
        // Arrange
        BookCreate bookCreate = new BookCreate();
        String title = "The Great Gatsby";
        String author = "F. Scott Fitzgerald";
        String isbn = "978-3-16-148410-0";
        String publisher = "Penguin Books";
        LocalDate publishedDate = LocalDate.of(1925, 4, 10);
        String language = "English";

        // Act
        bookCreate.setTitle(title);
        bookCreate.setAuthor(author);
        bookCreate.setIsbn(isbn);
        bookCreate.setPublisher(publisher);
        bookCreate.setPublishedDate(publishedDate);
        bookCreate.setLanguage(language);

        // Assert
        assertEquals(title, bookCreate.getTitle());
        assertEquals(author, bookCreate.getAuthor());
        assertEquals(isbn, bookCreate.getIsbn());
        assertEquals(publisher, bookCreate.getPublisher());
        assertEquals(publishedDate, bookCreate.getPublishedDate());
        assertEquals(language, bookCreate.getLanguage());
    }

    @Test
    void shouldImplementToString() {
        // Arrange
        BookCreate bookCreate = BookCreate.builder()
                .title("The Great Gatsby")
                .author("F. Scott Fitzgerald")
                .build();

        // Act
        String toString = bookCreate.toString();

        // Assert
        assertNotNull(toString);
        assertFalse(toString.isEmpty());
        assertTrue(toString.contains("BookCreate"));
        assertTrue(toString.contains("The Great Gatsby"));
        assertTrue(toString.contains("F. Scott Fitzgerald"));
    }
}