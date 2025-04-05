package com.tracktainment.bookmanager.usecases;

import com.tracktainment.bookmanager.dataprovider.BookDataProvider;
import com.tracktainment.bookmanager.domain.Book;
import com.tracktainment.bookmanager.dto.BookUpdate;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateUseCase {

    private final BookDataProvider bookDataProvider;
    private final FindByIdUseCase findByIdUseCase;

    public Output execute(Input input) {
        Book book = findByIdUseCase.execute(FindByIdUseCase.Input.builder()
                        .id(input.getId())
                .build()
        ).getBook();

        return Output.builder()
                .book(bookDataProvider.update(
                        input.getId(),
                        input.getBookUpdate()
                ))
                .build();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Input {
        private String id;
        private BookUpdate bookUpdate;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Output {
        private Book book;
    }
}
