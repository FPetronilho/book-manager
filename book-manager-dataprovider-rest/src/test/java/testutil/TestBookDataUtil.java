package testutil;

import com.tracktainment.bookmanager.dto.duxmanager.request.AssetRequest;
import com.tracktainment.bookmanager.dto.duxmanager.response.AssetResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public class TestBookDataUtil {

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
