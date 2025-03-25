package com.tracktainment.bookmanager.usecases;

import com.tracktainment.bookmanager.dataprovider.BookDataProvider;
import com.tracktainment.bookmanager.domain.Book;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindByTitleUseCase {

    private final BookDataProvider bookDataProvider;

    public Output execute(Input input) {
        return Output.builder()
                .book(bookDataProvider.findByTitle(input.getTitle()))
                .build();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Input {
        private String title;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Output {
        private Book book;
    }
}
