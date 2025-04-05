package com.tracktainment.bookmanager.usecases;

import com.tracktainment.bookmanager.dataprovider.BookDataProvider;
import com.tracktainment.bookmanager.dataprovider.DuxManagerDataProvider;
import com.tracktainment.bookmanager.domain.Book;
import com.tracktainment.bookmanager.dto.duxmanager.response.AssetResponse;
import com.tracktainment.bookmanager.exception.ResourceNotFoundException;
import com.tracktainment.bookmanager.security.context.DigitalUser;
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

    public Output execute(Input input) {
        DigitalUser digitalUser = new DigitalUser();
        digitalUser.setId("1b63e584-8921-4bfe-bbcd-c04caa3e0790");

        List<AssetResponse> assetResponseList = duxManagerDataProvider.listAssetsByCriteria(
                0,
                10,
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
