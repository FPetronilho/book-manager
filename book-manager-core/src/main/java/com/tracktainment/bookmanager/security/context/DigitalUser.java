package com.tracktainment.bookmanager.security.context;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DigitalUser {

    private String id;
    private String subject;
}
