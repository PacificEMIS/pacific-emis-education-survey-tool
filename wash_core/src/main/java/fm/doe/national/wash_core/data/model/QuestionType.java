package fm.doe.national.wash_core.data.model;

import java.io.Serializable;

public enum QuestionType implements Serializable {
    BINARY(Flag.BINARY.value),
    TERNARY(Flag.TERNARY.value),
    TEXT_INPUT(Flag.INPUT.value),
    NUMBER_INPUT(Flag.INPUT.value + Flag.NUMERIC.value),
    PHONE_INPUT(Flag.INPUT.value + Flag.PHONE.value),
    GEOLOCATION(Flag.GEO.value),
    PHOTO(Flag.PHOTO.value),
    SINGLE_SELECTION(Flag.CHOOSE.value + Flag.SINGLE.value),
    MULTI_SELECTION(Flag.CHOOSE.value + Flag.MULTIPLE.value),
    COMPLEX_BINARY(Flag.VAR.value + Flag.BINARY.value),
    COMPLEX_NUMBER_INPUT(Flag.VAR.value + Flag.INPUT.value + Flag.NUMERIC.value);

    private final int flags;

    public static QuestionType createFromFlags(int flags) {
        for (QuestionType questionType : QuestionType.values()) {
            if (flags == questionType.flags) {
                return questionType;
            }
        }
        return BINARY;
    }

    QuestionType(int flags) {
        this.flags = flags;
    }

    public int getFlags() {
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
