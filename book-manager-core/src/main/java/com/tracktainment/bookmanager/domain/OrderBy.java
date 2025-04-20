package com.tracktainment.bookmanager.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
@Schema(description = "Field sorting options")
public enum OrderBy {

    @Schema(description = "Sort by book title")
    TITLE("title"),

    @Schema(description = "Sort by book author")
    AUTHOR("author"),

    @Schema(description = "Sort by book genre")
    GENRE("genre"),

    @Schema(description = "Sort by creation date")
    CREATED_AT("createdAt");

    private final String value;
}