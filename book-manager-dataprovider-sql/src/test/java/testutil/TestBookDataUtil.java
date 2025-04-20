package testutil;

import com.tracktainment.bookmanager.domain.Book;
import com.tracktainment.bookmanager.dto.BookCreate;
import com.tracktainment.bookmanager.dto.BookUpdate;
import com.tracktainment.bookmanager.dto.duxmanager.request.AssetRequest;
import com.tracktainment.bookmanager.dto.duxmanager.response.AssetResponse;
import com.tracktainment.bookmanager.security.context.DigitalUser;

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

    public static Book createTestBookWithUpdate() {
        return Book.builder()
                .id(UUID.randomUUID().toString())
                .title("The Great Gatsby: Special Edition")
                .author("F. Scott Fitzgerald")
                .isbn("978-3-16-148410-1")
                .publisher("Penguin Classics")
                .publishedDate(LocalDate.of(1925, 4, 10))
                .language("English")
                .createdAt(LocalDateTime.now().minusDays(10))
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static Book createTestBookWithId(String id) {
        return Book.builder()
                .id(id)
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

    public static DigitalUser createTestDigitalUser() {
        return DigitalUser.builder()
                .id(UUID.randomUUID().toString())
                .subject("auth2|123456789")
                .build();
    }

    public static AssetResponse createTestAssetResponse() {
        return AssetResponse.builder()
                .id(UUID.randomUUID().toString())
                .externalId(UUID.randomUUID().toString())
                .type("book")
                .permissionPolicy(AssetResponse.PermissionPolicy.OWNER)
                .artifactInformation(AssetResponse.ArtifactInformation.builder()
                        .groupId("com.tracktainment")
                        .artifactId("book-manager")
                        .version("0.0.1-SNAPSHOT")
                        .build())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static AssetRequest createTestAssetRequest(String bookId) {
        return AssetRequest.builder()
                .externalId(bookId)
                .type("book")
                .permissionPolicy(AssetRequest.PermissionPolicy.OWNER)
                .artifactInformation(AssetRequest.ArtifactInformation.builder()
                        .groupId("com.tracktainment")
                        .artifactId("book-manager")
                        .version("0.0.1-SNAPSHOT")
                        .build())
                .build();
    }
}
