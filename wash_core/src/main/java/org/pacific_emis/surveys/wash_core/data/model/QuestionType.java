package org.pacific_emis.surveys.wash_core.data.model;

import java.io.Serializable;

public enum QuestionType implements Serializable {
    BINARY(Flag.BINARY),
    TERNARY(Flag.TERNARY),
    TEXT_INPUT(Flag.INPUT),
    NUMBER_INPUT(Flag.INPUT, Flag.NUMERIC),
    PHONE_INPUT(Flag.INPUT, Flag.PHONE),
    GEOLOCATION(Flag.GEO),
    PHOTO(Flag.PHOTO),
    SINGLE_SELECTION(Flag.CHOOSE, Flag.SINGLE),
    MULTI_SELECTION(Flag.CHOOSE, Flag.MULTIPLE),
    COMPLEX_BINARY(Flag.VAR, Flag.BINARY),
    COMPLEX_NUMBER_INPUT(Flag.VAR, Flag.INPUT, Flag.NUMERIC),
    COMPLEX_TEXT_INPUT(Flag.INPUT, Flag.CHOOSE);

    private final Flag[] flags;

    public static QuestionType createFromFlags(int flags) {
        for (QuestionType questionType : QuestionType.values()) {
            if (flags == questionType.getFlagsSum()) {
                return questionType;
            }
        }
        return BINARY;
    }

    QuestionType(Flag... flags) {
        this.flags = flags;
    }

    private int getFlagsSum() {
        int sum = 0;

        for (Flag flag : flags) {
            sum += flag.getValue();
        }

        return sum;
    }

    public Flag[] getFlags() {
        return flags;
    }

    public enum Flag {
        BINARY(2),
        INPUT(4),
        NUMERIC(8),
        CHOOSE(16),
        SINGLE(32),
        MULTIPLE(64),
        VAR(128),
        PHOTO(256),
        GEO(512),
        TERNARY(1024),
        PHONE(2048);

        final int value;

        Flag(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
