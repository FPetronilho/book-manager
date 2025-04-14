package com.tracktainment.bookmanager.usecases;

import com.tracktainment.bookmanager.dataprovider.BookDataProvider;
import com.tracktainment.bookmanager.dataprovider.DuxManagerDataProvider;
import com.tracktainment.bookmanager.domain.Book;
import com.tracktainment.bookmanager.dto.duxmanager.response.AssetResponse;
import com.tracktainment.bookmanager.exception.ResourceNotFoundException;
import com.tracktainment.bookmanager.security.context.DigitalUser;
import com.tracktainment.bookmanager.security.util.SecurityUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindByIdUseCase {

    private final BookDataProvider bookDataProvider;
    private final DuxManagerDataProvider duxManagerDataProvider;
    private final SecurityUtil securityUtil;

    public Output execute(Input input) {
        // Get digital user from jwt
        DigitalUser digitalUser = securityUtil.getDigitalUser();

        // Get asset from dux-manager
        List<AssetResponse> assetResponseList = duxManagerDataProvider.listAssetsByCriteria(
                input.getJwt(),
                digitalUser.getId(),
                input.getId(),
                "com.tracktainment",
                "book-manager",
                "book",
                null,
                null,
                null
        );

        if (CollectionUtils.isEmpty(assetResponseList)) {
            throw new ResourceNotFoundException(Book.class, input.getId());
        }

        return Output.builder()
                .book(bookDataProvider.findById(input.getId()))
                .build();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Input {
        private String jwt;
        private String id;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Output {
        private Book book;
    }
}
