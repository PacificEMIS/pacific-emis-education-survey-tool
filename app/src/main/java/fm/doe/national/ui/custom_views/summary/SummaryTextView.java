package fm.doe.national.ui.custom_views.summary;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import fm.doe.national.R;
import fm.doe.national.utils.Constants;

public class SummaryTextView extends AppCompatTextView {

    public enum AnswerType {
        POSITIVE,
        NEGATIVE,
        MAX
    }

    private float positiveAnswers;
    private float negativeAnswers;
    private float maxAnswers;

    public SummaryTextView(Context context) {
        super(context);
        updateColor();
    }

    public SummaryTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        updateColor();
    }

    public SummaryTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        updateColor();
    }

    public void setAnswersCount(int answersCount, AnswerType type) throws RuntimeException {
        throwErrorIfNegativeCount(answersCount);
        switch (type) {
            case NEGATIVE:
                this.negativeAnswers = answersCount;
                break;
            case POSITIVE:
                this.positiveAnswers = answersCount;
                break;
            case MAX:
                this.maxAnswers = answersCount;
                break;
        }
        updateColor();
    }

    private void throwErrorIfNegativeCount(int count) throws RuntimeException {
        if (count < 0) {
            throw new RuntimeException(Constants.Errors.NEGATIVE_ANSWERS_COUNT);
        }
    }

    private void updateColor() {
        if (maxAnswers == 0) {
            setBackgroundColor(getResources().getColor(R.color.white));
            return;
        }

        if (positiveAnswers + negativeAnswers < maxAnswers) {
            setBackgroundColor(getResources().getColor(R.color.pink));
            return;
        }

        float positiveAnswersPercent = positiveAnswers / maxAnswers * 100;

        if (positiveAnswersPercent <= 25) {
            setBackgroundColor(getResources().getColor(R.color.pink));
        } else if (positiveAnswersPercent < 75) {
            setBackgroundColor(getResources().getColor(R.color.yellow));
        } else if (positiveAnswersPercent < 100) {
            setBackgroundColor(getResources().getColor(R.color.light_mint));
        } else {
            setBackgroundColor(getResources().getColor(R.color.dark_mint));
        }

        invalidate();
    }
}
