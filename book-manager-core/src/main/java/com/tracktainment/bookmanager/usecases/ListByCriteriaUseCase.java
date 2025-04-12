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
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListByCriteriaUseCase {

    private final BookDataProvider bookDataProvider;
    private final DuxManagerDataProvider duxManagerDataProvider;

    public Output execute(Input input) {
        DigitalUser digitalUser = new DigitalUser();
        digitalUser.setId("bd30e6d3-d51f-4548-910f-c93a25437259");

        List<AssetResponse> assetResponseList = duxManagerDataProvider.listAssetsByCriteria(
                digitalUser.getId(),
                input.getIds(),
                "com.tracktainment",
                "book-manager",
                "book",
                input.getCreatedAt(),
                input.getFrom(),
                input.getTo()
        );

        String assetIds = assetResponseList.stream()
                .map(AssetResponse::getExternalId)
                .collect(Collectors.joining(","));

        boolean idsInputIsEmpty = input.getIds() == null;
        if (!StringUtils.hasText(assetIds)) {
            if (!idsInputIsEmpty) {
                assetIds = input.getIds();
            } else {
                assetIds = null;
            }
        }

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
