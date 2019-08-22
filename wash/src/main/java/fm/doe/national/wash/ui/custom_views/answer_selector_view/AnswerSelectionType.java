package fm.doe.national.wash.ui.custom_views.answer_selector_view;

public enum AnswerSelectionType {
    SINGLE(0), MULTIPLE(1);

    private final int value;

    public static AnswerSelectionType createFromValue(int value) {
        for (AnswerSelectionType type : AnswerSelectionType.values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalStateException();
    }

    AnswerSelectionType(int value) {
        this.value = value;
    }
}
