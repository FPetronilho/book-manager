package com.tracktainment.bookmanager.usecases;

import com.tracktainment.bookmanager.dataprovider.BookDataProvider;
import com.tracktainment.bookmanager.dataprovider.DuxManagerDataProvider;
import com.tracktainment.bookmanager.domain.Book;
import com.tracktainment.bookmanager.domain.OrderBy;
import com.tracktainment.bookmanager.domain.OrderDirection;
import com.tracktainment.bookmanager.dto.duxmanager.response.AssetResponse;
import com.tracktainment.bookmanager.security.context.DigitalUser;
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
    private final DuxManagerDataProvider duxManagerDataProvider;

    public Output execute(Input input) {
        DigitalUser digitalUser = new DigitalUser();
        digitalUser.setId("1b63e584-8921-4bfe-bbcd-c04caa3e0790");

        List<AssetResponse> assetResponseList = duxManagerDataProvider.listAssetsByCriteria(
                0,
                10,
                digitalUser.getId(),
                String.join(",", input.getIds()),
                "com.tracktainment",
                "book-manager",
                "book",
                null,
                null,
                null
        );

        List<String> assetIds = assetResponseList.stream()
                .map(AssetResponse::getExternalId)
                .toList();

        input.setIds(assetIds);
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
        private List<String> ids;
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
