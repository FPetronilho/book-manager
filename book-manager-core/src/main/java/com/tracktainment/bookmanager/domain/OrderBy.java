package com.tracktainment.bookmanager.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor
public enum OrderBy {

    TITLE("title"),
    AUTHOR("author"),
    CREATED_AT("createdAt");

    private final String value;
}
