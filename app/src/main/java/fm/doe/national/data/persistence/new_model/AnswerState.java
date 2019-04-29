package fm.doe.national.data.persistence.new_model;

import android.util.SparseArray;

public enum AnswerState {
    NOT_ANSWERED(0), POSITIVE(1), NEGATIVE(-1);

    private int value;

    private static SparseArray<AnswerState> map = new SparseArray<>();

    AnswerState(int value) {
        this.value = value;
    }

    static {
        for (AnswerState state : AnswerState.values()) {
            map.put(state.value, state);
        }
    }

    public static AnswerState valueOf(int raw) {
        return map.get(raw);
    }

    public int getValue() {
        return value;
    }
}
