package com.tracktainment.bookmanager.util;

public class Constants {

    public Constants() {
        throw new IllegalStateException("Cannot instantiate an util class.");
    }

    // Default values;
    public static final String DEFAULT_OFFSET = "0";
    public static final String DEFAULT_LIMIT = "10";
    public static final int MIN_OFFSET = 0;
    public static final int MIN_LIMIT = 1;
    public static final int MAX_LIMIT = 100;
    public static final String DEFAULT_ORDER = "ALPHABETICALLY_TITLE";
    public static final String DEFAULT_DIRECTION = "ASC";


    // Required fields validation
    public static final String TITLE_MANDATORY_MSG = "'title' is mandatory.";
    public static final String AUTHOR_MANDATORY_MSG = "'author' is mandatory.";


    // Regex
    public static final String TITLE_REGEX = "^[A-Za-z0-9\\s\\-,\\.\\'\\\";!?()&]{1,200}$";
    public static final String AUTHOR_REGEX = "^[A-Za-z\\\\s\\\\-,\\\\.\\\\'\\\\\\\";!?&]{1,100}$";
    public static final String ISBN_REGEX =
            "^(?:\\d{3}[-\\s]?)?(\\d{1,5}[-\\s]?)?\\d{1,7}[-\\s]?\\d{1,7}[-\\s]?\\d{1,7}[-\\s]?(\\d{1})$";
    public static final String PUBLISHER_REGEX = "^[A-Za-z0-9\\s\\-,\\.\\'\\\";!?&()]+.{1,150}$";
    public static final String LANGUAGE_REGEX = "^[A-Za-z\\s\\-\\']{1,50}$";


    // Fields validation
    public static final String TITLE_INVALID_MSG = "'title' must match: " + TITLE_REGEX + " .";
    public static final String AUTHOR_INVALID_MSG = "'author' must match: " + AUTHOR_REGEX + " .";
    public static final String ISBN_INVALID_MSG = "'isbn' must match: " + ISBN_REGEX + " .";
    public static final String PUBLISHER_INVALID_MSG = "'publisher' must match: " + PUBLISHER_REGEX + " .";
    public static final String LANGUAGE_INVALID_MSG = "'language' must match: " + LANGUAGE_REGEX + " .";
}
