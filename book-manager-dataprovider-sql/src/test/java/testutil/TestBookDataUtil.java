package testutil;

import com.tracktainment.bookmanager.domain.Book;
import com.tracktainment.bookmanager.dto.BookCreate;
import com.tracktainment.bookmanager.dto.BookUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class TestBookDataUtil {

    public static BookCreate createTestBookCreate() {
        return BookCreate.builder()
                .title("The Great Gatsby")
                .author("F. Scott Fitzgerald")
                .isbn("978-3-16-148410-0")
                .publisher("Penguin Books")
                .publishedDate(LocalDate.of(1925, 4, 10))
                .language("English")
                .build();
    }

    public static BookUpdate createTestBookUpdate() {
        return BookUpdate.builder()
                .title("The Great Gatsby: Special Edition")
                .author("F. Scott Fitzgerald")
                .isbn("978-3-16-148410-1")
                .publisher("Penguin Classics")
                .publishedDate(LocalDate.of(1925, 4, 10))
                .language("English")
                .build();
    }

    public static Book createTestBook() {
        return Book.builder()
                .id(UUID.randomUUID().toString())
                .title("The Great Gatsby")
                .author("F. Scott Fitzgerald")
                .isbn("978-3-16-148410-0")
                .publisher("Penguin Books")
                .publishedDate(LocalDate.of(1925, 4, 10))
                .language("English")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
