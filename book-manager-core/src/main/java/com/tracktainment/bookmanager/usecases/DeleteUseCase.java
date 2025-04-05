package com.tracktainment.bookmanager.usecases;

import com.tracktainment.bookmanager.dataprovider.BookDataProvider;
import com.tracktainment.bookmanager.dataprovider.DuxManagerDataProvider;
import com.tracktainment.bookmanager.security.context.DigitalUser;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteUseCase {

    private final BookDataProvider bookDataProvider;
    private final DuxManagerDataProvider duxManagerDataProvider;

    public void execute(Input input) {
        DigitalUser digitalUser = new DigitalUser();
        digitalUser.setId("1b63e584-8921-4bfe-bbcd-c04caa3e0790");

        duxManagerDataProvider.deleteAsset(digitalUser.getId(), input.getId());
        bookDataProvider.delete(input.getId());
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Input {
        private String id;
    }
}
