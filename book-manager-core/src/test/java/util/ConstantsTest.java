package util;

import com.tracktainment.bookmanager.util.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ConstantsTest {

    @Test
    void shouldThrowExceptionWhenInstantiated() throws NoSuchMethodException {
        // Arrange
        Constructor<Constants> constructor = Constants.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // Act & Assert
        Exception exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
        Throwable cause = exception.getCause();
        assertEquals(IllegalStateException.class, cause.getClass());
        assertEquals("Cannot instantiate an util class.", cause.getMessage());
    }

    @Test
    void shouldHaveCorrectDefaultValues() {
        // Assert
        assertEquals("0", Constants.DEFAULT_OFFSET);
        assertEquals("10", Constants.DEFAULT_LIMIT);
        assertEquals(0, Constants.MIN_OFFSET);
        assertEquals(1, Constants.MIN_LIMIT);
        assertEquals(100, Constants.MAX_LIMIT);
        assertEquals("TITLE", Constants.DEFAULT_ORDER);
        assertEquals("ASC", Constants.DEFAULT_DIRECTION);
    }

    @ParameterizedTest
    @MethodSource("provideValidRegexMatches")
    void shouldMatchValidRegexPatterns(String input, String pattern) {
        // Assert
        assertTrue(input.matches(pattern), "Input '" + input + "' should match pattern '" + pattern + "'");
    }

    @ParameterizedTest
    @MethodSource("provideInvalidRegexMatches")
    void shouldNotMatchInvalidRegexPatterns(String input, String pattern) {
        // Assert
        assertFalse(input.matches(pattern), "Input '" + input + "' should not match pattern '" + pattern + "'");
    }

    @Test
    void shouldHaveCorrectErrorMessages() {
        // Assert for mandatory field messages
        assertTrue(Constants.TITLE_MANDATORY_MSG.contains("mandatory"));
        assertTrue(Constants.AUTHOR_MANDATORY_MSG.contains("mandatory"));

        // Assert for validation error messages
        assertTrue(Constants.ID_INVALID_MSG.contains("must match"));
        assertTrue(Constants.TITLE_INVALID_MSG.contains("must match"));
        assertTrue(Constants.OFFSET_INVALID_MSG.contains("must be positive"));
        assertTrue(Constants.LIMIT_INVALID_MSG.contains("range"));
    }

    @Test
    void shouldHaveCorrectRegexPatterns() {
        // ID regex should match UUID format
        assertNotNull(Constants.ID_REGEX);
        assertTrue(UUID.randomUUID().toString().matches(Constants.ID_REGEX));

        // Other regexes should be defined
        assertNotNull(Constants.TITLE_REGEX);
        assertNotNull(Constants.AUTHOR_REGEX);
        assertNotNull(Constants.GENRE_REGEX);
        assertNotNull(Constants.ISBN_REGEX);
        assertNotNull(Constants.PUBLISHER_REGEX);
        assertNotNull(Constants.LANGUAGE_REGEX);
    }

    private static Stream<Arguments> provideValidRegexMatches() {
        return Stream.of(
                // ID_REGEX
                Arguments.of(UUID.randomUUID().toString(), Constants.ID_REGEX),

                // TITLE_REGEX
                Arguments.of("The Great Gatsby", Constants.TITLE_REGEX),
                Arguments.of("To Kill a Mockingbird", Constants.TITLE_REGEX),
                Arguments.of("1984", Constants.TITLE_REGEX),

                // AUTHOR_REGEX
                Arguments.of("F. Scott Fitzgerald", Constants.AUTHOR_REGEX),
                Arguments.of("Harper Lee", Constants.AUTHOR_REGEX),
                Arguments.of("George Orwell", Constants.AUTHOR_REGEX),

                // GENRE_REGEX
                Arguments.of("Fiction", Constants.GENRE_REGEX),
                Arguments.of("Drama", Constants.GENRE_REGEX),
                Arguments.of("Science-Fiction", Constants.GENRE_REGEX),

                // ISBN_REGEX
                Arguments.of("978-3-16-148410-0", Constants.ISBN_REGEX),
                Arguments.of("9780316148410", Constants.ISBN_REGEX),
                Arguments.of("978-0-316-14841-0", Constants.ISBN_REGEX),

                // PUBLISHER_REGEX
                Arguments.of("Penguin Books", Constants.PUBLISHER_REGEX),
                Arguments.of("HarperCollins", Constants.PUBLISHER_REGEX),
                Arguments.of("Random House", Constants.PUBLISHER_REGEX),

                // LANGUAGE_REGEX
                Arguments.of("English", Constants.LANGUAGE_REGEX),
                Arguments.of("Spanish", Constants.LANGUAGE_REGEX),
                Arguments.of("French", Constants.LANGUAGE_REGEX),

                // ID_LIST_REGEX
                Arguments.of(UUID.randomUUID().toString(), Constants.ID_LIST_REGEX),
                Arguments.of(UUID.randomUUID().toString() + "," + UUID.randomUUID().toString(), Constants.ID_LIST_REGEX)
        );
    }

    private static Stream<Arguments> provideInvalidRegexMatches() {
        return Stream.of(
                // ID_REGEX - not a UUID
                Arguments.of("not-a-uuid", Constants.ID_REGEX),

                // TITLE_REGEX - too long
                Arguments.of("a".repeat(201), Constants.TITLE_REGEX),

                // AUTHOR_REGEX - invalid characters
                Arguments.of("Author123", Constants.AUTHOR_REGEX),

                // GENRE_REGEX - invalid characters
                Arguments.of("Genre+", Constants.GENRE_REGEX),

                // ISBN_REGEX - invalid format
                Arguments.of("not-an-isbn", Constants.ISBN_REGEX),

                // PUBLISHER_REGEX - too long
                Arguments.of("a".repeat(151), Constants.PUBLISHER_REGEX),

                // LANGUAGE_REGEX - invalid characters
                Arguments.of("Language123", Constants.LANGUAGE_REGEX),

                // ID_LIST_REGEX - invalid format
                Arguments.of("not-a-uuid-list", Constants.ID_LIST_REGEX),
                Arguments.of("invalid,uuid,list", Constants.ID_LIST_REGEX)
        );
    }
}
