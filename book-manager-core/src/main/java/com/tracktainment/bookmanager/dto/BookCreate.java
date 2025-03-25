package com.tracktainment.bookmanager.dto;

import com.tracktainment.bookmanager.util.Constants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookCreate {

    @NotNull(message = Constants.TITLE_MANDATORY_MSG)
    @Pattern(regexp = Constants.TITLE_REGEX, message = Constants.TITLE_INVALID_MSG)
    private String title;

    @NotNull(message = Constants.AUTHOR_MANDATORY_MSG)
    @Pattern(regexp = Constants.AUTHOR_REGEX, message = Constants.AUTHOR_INVALID_MSG)
    private String author;
}
