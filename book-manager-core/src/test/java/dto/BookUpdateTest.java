package dto;

import com.tracktainment.bookmanager.dto.BookUpdate;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BookUpdateTest {

    @Test
    void shouldCreateBookUpdateUsingBuilder() {
        // Arrange
        String title = "The Great Gatsby";
        String author = "F. Scott Fitzgerald";
        String isbn = "978-3-16-148410-0";
        String publisher = "Penguin Books";
        LocalDate publishedDate = LocalDate.of(1925, 4, 10);
        String language = "English";

        // Act
        BookUpdate bookUpdate = BookUpdate.builder()
                .title(title)
                .author(author)
                .isbn(isbn)
                .publisher(publisher)
                .publishedDate(publishedDate)
                .language(language)
                .build();

        // Assert
        assertEquals(title, bookUpdate.getTitle());
        assertEquals(author, bookUpdate.getAuthor());
        assertEquals(isbn, bookUpdate.getIsbn());
        assertEquals(publisher, bookUpdate.getPublisher());
        assertEquals(publishedDate, bookUpdate.getPublishedDate());
        assertEquals(language, bookUpdate.getLanguage());
    }

    @Test
    void shouldCreateEmptyBookUpdateUsingNoArgsConstructor() {
        // Act
        BookUpdate bookUpdate = new BookUpdate();

        // Assert
        assertNull(bookUpdate.getTitle());
        assertNull(bookUpdate.getAuthor());
        assertNull(bookUpdate.getIsbn());
        assertNull(bookUpdate.getPublisher());
        assertNull(bookUpdate.getPublishedDate());
        assertNull(bookUpdate.getLanguage());
    }

    @Test
    void shouldUseSettersAndGetters() {
        // Arrange
        BookUpdate bookUpdate = new BookUpdate();
        String title = "The Great Gatsby";
        String author = "F. Scott Fitzgerald";
        String isbn = "978-3-16-148410-0";
        String publisher = "Penguin Books";
        LocalDate publishedDate = LocalDate.of(1925, 4, 10);
        String language = "English";

        // Act
        bookUpdate.setTitle(title);
        bookUpdate.setAuthor(author);
        bookUpdate.setIsbn(isbn);
        bookUpdate.setPublisher(publisher);
        bookUpdate.setPublishedDate(publishedDate);
        bookUpdate.setLanguage(language);

        // Assert
        assertEquals(title, bookUpdate.getTitle());
        assertEquals(author, bookUpdate.getAuthor());
        assertEquals(isbn, bookUpdate.getIsbn());
        assertEquals(publisher, bookUpdate.getPublisher());
        assertEquals(publishedDate, bookUpdate.getPublishedDate());
        assertEquals(language, bookUpdate.getLanguage());
    }

    @Test
    void shouldImplementToString() {
        // Arrange
        BookUpdate bookUpdate = BookUpdate.builder()
                .title("The Great Gatsby: Special Edition")
                .author("F. Scott Fitzgerald")
                .build();

        // Act
        String toString = bookUpdate.toString();

        // Assert
        assertNotNull(toString);
        assertFalse(toString.isEmpty());
        assertTrue(toString.contains("BookUpdate"));
        assertTrue(toString.contains("The Great Gatsby: Special Edition"));
        assertTrue(toString.contains("F. Scott Fitzgerald"));
    }
}
