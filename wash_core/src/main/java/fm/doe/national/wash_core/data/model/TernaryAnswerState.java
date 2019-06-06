package fm.doe.national.wash_core.data.model;

import android.content.Context;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.wash_core.R;

public enum TernaryAnswerState {
    YES(Text.from(R.string.answer_yes)),
    NO(Text.from(R.string.answer_no)),
    UNKNOWN(Text.from(R.string.answer_unknown));

    private final Text text;

    public static TernaryAnswerState createFromText(Context context, Text text) {
        for (TernaryAnswerState state : TernaryAnswerState.values()) {
            if (state.text.getString(context).equals(text.getString(context))) {
                return state;
            }
        }
        throw new IllegalStateException();
    }

    TernaryAnswerState(Text text) {
        this.text = text;
    }

    public Text getText() {
        return text;
    }
}
