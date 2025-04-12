package com.tracktainment.bookmanager.security.context;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DigitalUser {

    private String id;
    private String subject;
    private IdentityProvider identityProvider;
    private String tenantId;

    @ToString
    @Getter
    @RequiredArgsConstructor
    public enum IdentityProvider {

        AMAZON_COGNITO("amazonCognito"),
        APPLE_ID("appleId"),
        GOOGLE_IDENTITY_PLATFORM("googleIdentityPlatform"),
        KEY_CLOAK("keyCloak"),
        MICROSOFT_ENTRA_ID("microsoftEntraId");

        private final String value;
    }
}
