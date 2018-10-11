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
        checkForNegativeCount(answersCount);
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

    private void checkForNegativeCount(int count) throws RuntimeException {
        if (count < 0) {
            throw new RuntimeException(Constants.Errors.NEGATIVE_ANSWERS_COUNT);
        }
    }

    private void updateColor() {
        if (maxAnswers == 0) {
            setBackgroundResource(R.color.white);
            return;
        }

        if (positiveAnswers + negativeAnswers < maxAnswers) {
            setBackgroundResource(R.color.pink);
            return;
        }

        float positiveAnswersPercent = positiveAnswers / maxAnswers * 100;

        if (positiveAnswersPercent <= 25) {
            setBackgroundResource(R.color.pink);
        } else if (positiveAnswersPercent < 75) {
            setBackgroundResource(R.color.yellow);
        } else if (positiveAnswersPercent < 100) {
            setBackgroundResource(R.color.light_mint);
        } else {
            setBackgroundResource(R.color.dark_mint);
        }

        invalidate();
    }
}
