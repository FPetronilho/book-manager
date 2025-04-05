package com.tracktainment.bookmanager.mapper;

import com.tracktainment.bookmanager.domain.Book;
import com.tracktainment.bookmanager.dto.duxmanager.request.AssetRequest;

public class AssetMapper {

    public static AssetRequest toAssetRequest(Book book) {
        return AssetRequest.builder()
                .externalId(book.getId())
                .type("book")
                .permissionPolicy(AssetRequest.PermissionPolicy.OWNER)
                .artifactInformation(
                        new AssetRequest.ArtifactInformation(
                                "com.tracktainment",
                                "book-manager",
                                "0.0.1-SNAPSHOT"
                        )
                )
                .build();
    }
}
