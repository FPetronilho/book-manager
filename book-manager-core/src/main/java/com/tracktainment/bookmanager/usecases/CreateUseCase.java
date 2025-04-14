package com.tracktainment.bookmanager.usecases;

import com.tracktainment.bookmanager.dataprovider.BookDataProvider;
import com.tracktainment.bookmanager.dataprovider.DuxManagerDataProvider;
import com.tracktainment.bookmanager.domain.Book;
import com.tracktainment.bookmanager.dto.BookCreate;
import com.tracktainment.bookmanager.mapper.AssetMapper;
import com.tracktainment.bookmanager.security.context.DigitalUser;
import com.tracktainment.bookmanager.security.util.SecurityUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateUseCase {

    private final BookDataProvider bookDataProvider;
    private final DuxManagerDataProvider duxManagerDataProvider;
    private final SecurityUtil securityUtil;

    public Output execute(Input input) {
        // Create book
        Book book = bookDataProvider.create(input.getBookCreate());

        // Get digital user from jwt and create book asset in dux-manager
        try {
            DigitalUser digitalUser = securityUtil.getDigitalUser();

            duxManagerDataProvider.createAsset(
                    input.getJwt(),
                    digitalUser.getId(),
                    AssetMapper.toAssetRequest(book)
            );

        // If asset cannot be created on dux-manager then rollback create book
        } catch (Exception e) {
            log.error("Could not create book in Dux Manager. Reason: {}", e.getMessage());
            bookDataProvider.delete(book.getId());
            throw e;
        }

        return Output.builder()
                .book(book)
                .build();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Input {
        private String jwt;
        private BookCreate bookCreate;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Output {
        private Book book;
    }
}
