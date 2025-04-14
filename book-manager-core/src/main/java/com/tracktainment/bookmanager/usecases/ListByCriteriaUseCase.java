package com.tracktainment.bookmanager.usecases;

import com.tracktainment.bookmanager.dataprovider.BookDataProvider;
import com.tracktainment.bookmanager.dataprovider.DuxManagerDataProvider;
import com.tracktainment.bookmanager.domain.Book;
import com.tracktainment.bookmanager.domain.OrderBy;
import com.tracktainment.bookmanager.domain.OrderDirection;
import com.tracktainment.bookmanager.dto.duxmanager.response.AssetResponse;
import com.tracktainment.bookmanager.security.context.DigitalUser;
import com.tracktainment.bookmanager.security.util.SecurityUtil;
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
    private final SecurityUtil securityUtil;

    public Output execute(Input input) {
        // Get digital user from jwt
        DigitalUser digitalUser = securityUtil.getDigitalUser();

        // Get assets by criteria from dux-manger
        List<AssetResponse> assetResponseList = duxManagerDataProvider.listAssetsByCriteria(
                input.getJwt(),
                digitalUser.getId(),
                input.getIds(),
                "com.tracktainment",
                "book-manager",
                "book",
                input.getCreatedAt(),
                input.getFrom(),
                input.getTo()
        );

        // Convert list of assets to String so that it can be replaced in "ids" input
        String assetIds = assetResponseList.stream()
                .map(AssetResponse::getExternalId)
                .collect(Collectors.joining(","));

        // Check if "ids" input is empty because no assets match or, because that criteria was never inputted
        boolean idsInputIsEmpty = input.getIds() == null;
        if (!StringUtils.hasText(assetIds)) {
            if (!idsInputIsEmpty) {
                assetIds = input.getIds();
            } else {
                assetIds = null;
            }
        }

        input.setIds(assetIds);

        // List the books
        return Output.builder()
                .books(bookDataProvider.listByCriteria(input))
                .build();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Input {
        private String jwt;
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
