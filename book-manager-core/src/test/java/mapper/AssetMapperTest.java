package mapper;

import com.tracktainment.bookmanager.domain.Book;
import com.tracktainment.bookmanager.dto.duxmanager.request.AssetRequest;
import com.tracktainment.bookmanager.mapper.AssetMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import testutil.TestBookDataUtil;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AssetMapperTest {

    @Test
    void shouldMapGameToAssetRequest() {
        // Arrange
        String bookId = UUID.randomUUID().toString();
        Book book = TestBookDataUtil.createTestBookWithId(bookId);

        // Act
        AssetRequest result = AssetMapper.toAssetRequest(book);

        // Assert
        assertNotNull(result);
        assertEquals(bookId, result.getExternalId());
        assertEquals("book", result.getType());
        assertEquals(AssetRequest.PermissionPolicy.OWNER, result.getPermissionPolicy());

        assertNotNull(result.getArtifactInformation());
        assertEquals("com.tracktainment", result.getArtifactInformation().getGroupId());
        assertEquals("book-manager", result.getArtifactInformation().getArtifactId());
        assertEquals("0.0.1-SNAPSHOT", result.getArtifactInformation().getVersion());
    }

    @Test
    void shouldHandleNullGameId() {
        // Arrange
        Book book = Book.builder()
                .id(null)
                .title("Test Book")
                .build();

        // Act
        AssetRequest result = AssetMapper.toAssetRequest(book);

        // Assert
        assertNotNull(result);
        assertNull(result.getExternalId());
        assertEquals("book", result.getType());
        assertEquals(AssetRequest.PermissionPolicy.OWNER, result.getPermissionPolicy());
    }

    @Test
    void shouldSetCorrectPermissionPolicy() {
        // Arrange
        Book book = TestBookDataUtil.createTestBook();

        // Act
        AssetRequest result = AssetMapper.toAssetRequest(book);

        // Assert
        assertEquals(AssetRequest.PermissionPolicy.OWNER, result.getPermissionPolicy());
    }

    @Test
    void shouldSetCorrectArtifactInformation() {
        // Arrange
        Book book = TestBookDataUtil.createTestBook();

        // Act
        AssetRequest result = AssetMapper.toAssetRequest(book);

        // Assert
        assertNotNull(result.getArtifactInformation());
        assertEquals("com.tracktainment", result.getArtifactInformation().getGroupId());
        assertEquals("book-manager", result.getArtifactInformation().getArtifactId());
        assertEquals("0.0.1-SNAPSHOT", result.getArtifactInformation().getVersion());
    }
}