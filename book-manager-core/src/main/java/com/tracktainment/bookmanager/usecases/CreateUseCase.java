package com.tracktainment.bookmanager.usecases;

import com.tracktainment.bookmanager.dataprovider.BookDataProvider;
import com.tracktainment.bookmanager.dataprovider.DuxManagerDataProvider;
import com.tracktainment.bookmanager.domain.Book;
import com.tracktainment.bookmanager.dto.BookCreate;
import com.tracktainment.bookmanager.mapper.AssetMapper;
import com.tracktainment.bookmanager.security.context.DigitalUser;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateUseCase {

    private final BookDataProvider bookDataProvider;
    private final DuxManagerDataProvider duxManagerDataProvider;

    public Output execute(Input input) {
        Book book = bookDataProvider.create(input.getBookCreate());

        try {
            DigitalUser digitalUser = new DigitalUser();
            digitalUser.setId("1b63e584-8921-4bfe-bbcd-c04caa3e0790");

            duxManagerDataProvider.createAsset(
                    digitalUser.getId(),
                    AssetMapper.toAssetRequest(book)
            );
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
