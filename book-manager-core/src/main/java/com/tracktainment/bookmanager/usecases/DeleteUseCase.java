package com.tracktainment.bookmanager.usecases;

import com.tracktainment.bookmanager.dataprovider.BookDataProvider;
import com.tracktainment.bookmanager.dataprovider.DuxManagerDataProvider;
import com.tracktainment.bookmanager.security.context.DigitalUser;
import com.tracktainment.bookmanager.security.util.SecurityUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteUseCase {

    private final BookDataProvider bookDataProvider;
    private final DuxManagerDataProvider duxManagerDataProvider;
    private final SecurityUtil securityUtil;

    public void execute(Input input) {
        // Get digital user from jwt
        DigitalUser digitalUser = securityUtil.getDigitalUser();

        // Delete asset in dux-manager
        duxManagerDataProvider.deleteAsset(
                input.getJwt(),
                digitalUser.getId(),
                input.getId()
        );

        bookDataProvider.delete(input.getId());
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Input {
        private String jwt;
        private String id;
    }
}
