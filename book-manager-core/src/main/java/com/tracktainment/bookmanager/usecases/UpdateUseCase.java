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
        /* Finds the book to update. Conditions necessary to retrieve the asset from Dux Manager, i.e. authenticate the
        digital user, etc. are already being processed in FindByIdUseCase
         */
        Book book = findByIdUseCase.execute(FindByIdUseCase.Input.builder()
                        .jwt(input.getJwt())
                        .id(input.getId())
                        .build()
        ).getBook();

        /* Update and return the updated book. No action is necessary on Dux Manager as it only stores information on
        what assets each digital user has. It does not contain information of the book itself.
         */
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
        private String jwt;
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
