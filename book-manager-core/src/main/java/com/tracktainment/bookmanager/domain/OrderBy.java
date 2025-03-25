package com.tracktainment.bookmanager.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor
public enum OrderBy {

    ALPHABETICALLY_TITLE("alphabetically"),
    CREATED_AT("created");

    private final String value;
}
