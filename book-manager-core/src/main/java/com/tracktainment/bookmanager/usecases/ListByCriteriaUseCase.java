package com.tracktainment.bookmanager.usecases;

import com.tracktainment.bookmanager.dataprovider.BookDataProvider;
import com.tracktainment.bookmanager.domain.Book;
import com.tracktainment.bookmanager.domain.OrderBy;
import com.tracktainment.bookmanager.domain.OrderDirection;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListByCriteriaUseCase {

    private final BookDataProvider bookDataProvider;

    public Output execute(Input input) {
        return Output.builder()
                .books(bookDataProvider.listByCriteria(input))
                .build();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Input {
        private Integer offset;
        private Integer limit;
        private String ids;
        private String title;
        private String author;
        private String isbn;
        private String publisher;
        private LocalDate publishedDate;
        private String language;
        private LocalDate createdAt;
        private LocalDate from;
        private LocalDate to;
        private List<OrderBy> orderByList;
        private List<OrderDirection> orderDirectionList;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Output {
        private List<Book> books;
    }
}
