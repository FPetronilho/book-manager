package domain;

import com.tracktainment.bookmanager.domain.OrderBy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderByTest {

    @Test
    void shouldHaveCorrectValues() {
        // Assert
        assertEquals("title", OrderBy.TITLE.getValue());
        assertEquals("author", OrderBy.AUTHOR.getValue());
        assertEquals("genre", OrderBy.GENRE.getValue());
        assertEquals("createdAt", OrderBy.CREATED_AT.getValue());
        assertEquals(4, OrderBy.values().length);
    }

    @Test
    void shouldImplementToString() {
        // Assert
        assertTrue(OrderBy.TITLE.toString().contains("TITLE"));
        assertTrue(OrderBy.AUTHOR.toString().contains("AUTHOR"));
        assertTrue(OrderBy.GENRE.toString().contains("GENRE"));
        assertTrue(OrderBy.CREATED_AT.toString().contains("CREATED_AT"));
    }
}
