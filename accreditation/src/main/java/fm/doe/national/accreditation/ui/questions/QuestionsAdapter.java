package fm.doe.national.accreditation.ui.questions;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Text;
import com.omega_r.libs.views.OmegaTextView;

import fm.doe.national.accreditation.R;
import fm.doe.national.accreditation_core.data.model.AnswerState;
import fm.doe.national.accreditation_core.data.model.Criteria;
import fm.doe.national.accreditation_core.data.model.SubCriteria;
import fm.doe.national.accreditation_core.data.model.mutable.MutableAnswer;
import fm.doe.national.survey_core.ui.custom_views.BinaryAnswerSelectorView;
import fm.doe.national.survey_core.ui.survey.BaseQuestionsAdapter;

public class QuestionsAdapter extends BaseQuestionsAdapter<Question> {

    private static final int VIEW_TYPE_CRITERIA = 0;
    private static final int VIEW_TYPE_SUB_CRITERIA = 1;

    private QuestionsListener questionsListener;

    public QuestionsAdapter(BaseQuestionsAdapter.Listener baseListener, QuestionsListener questionsListener) {
        super(baseListener);
        this.questionsListener = questionsListener;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).isCriteriaOnly() ? VIEW_TYPE_CRITERIA : VIEW_TYPE_SUB_CRITERIA;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_CRITERIA:
                return new HeaderViewHolder(parent);
            case VIEW_TYPE_SUB_CRITERIA:
                return new QuestionViewHolder(parent);
        }
        throw new IllegalStateException();
    }

    class QuestionViewHolder extends BaseQuestionViewHolder implements BinaryAnswerSelectorView.StateChangedListener {

        private final View popupView;
        private final TextView hintView;

        private TextView prefixTextView;
        private TextView titleTextView;
        private TextView questionTextView;
        private BinaryAnswerSelectorView binaryAnswerSelectorView;

        QuestionViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_question);
            popupView = LayoutInflater.from(parent.getContext()).inflate(R.layout.popup_hint, parent, false);
            hintView = popupView.findViewById(R.id.textview_hint);
            bindViews();
            binaryAnswerSelectorView.setListener(this);
            titleTextView.setOnLongClickListener(this);
        }

        private void bindViews() {
            prefixTextView = findViewById(R.id.textview_prefix);
            titleTextView = findViewById(R.id.textview_title);
            questionTextView = findViewById(R.id.textview_question);
            binaryAnswerSelectorView = findViewById(R.id.binaryanswerselectorview);
        }

        @Override
        protected void onBind(Question item) {
            super.onBind(item);
            SubCriteria subCriteria = item.getSubCriteria();
            prefixTextView.setText(getString(R.string.format_subcriteria, subCriteria.getSuffix()));
            titleTextView.setText(subCriteria.getTitle());
            questionTextView.setText(subCriteria.getInterviewQuestions());
            binaryAnswerSelectorView.setStateNotNotifying(convertToUiState(subCriteria.getAnswer().getState()));

        }

        @Override
        public boolean onLongClick(View v) {
            if (v.getId() == R.id.textview_title) {
                String hint = getItem().getSubCriteria().getHint();
                if (!TextUtils.isEmpty(hint)) showHint(hint);
                return true;
            } else {
                return super.onLongClick(v);
            }
        }

        private void showHint(String hint) {
            hintView.setText(hint);
            PopupWindow popupWindow = new PopupWindow(
                    popupView,
                    itemView.getMeasuredWidth(),
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setClippingEnabled(false);

            // need to measure view before it rendered
            // showAsDropDown cannot draw popup above anchor in ViewHolder
            // so just offset it manually
            popupView.measure(View.MeasureSpec.makeMeasureSpec(titleTextView.getMeasuredWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            popupWindow.showAsDropDown(titleTextView,
                    0,
                    -titleTextView.getMeasuredHeight() - popupView.getMeasuredHeight(),
                    Gravity.TOP);
        }

        @Override
        public void onStateChanged(BinaryAnswerSelectorView view, BinaryAnswerSelectorView.State state) {
            ((MutableAnswer)getItem().getSubCriteria().getAnswer()).setState(convertFromUiState(state));
            questionsListener.onAnswerStateChanged(getItem());
        }

        private BinaryAnswerSelectorView.State convertToUiState(AnswerState state) {
            switch (state) {
                case NOT_ANSWERED:
                    return BinaryAnswerSelectorView.State.NEUTRAL;
                case NEGATIVE:
                    return BinaryAnswerSelectorView.State.NEGATIVE;
                case POSITIVE:
                    return BinaryAnswerSelectorView.State.POSITIVE;
            }
            throw new IllegalStateException();
        }

        private AnswerState convertFromUiState(BinaryAnswerSelectorView.State state) {
            switch (state) {
                case NEUTRAL:
                    return AnswerState.NOT_ANSWERED;
                case NEGATIVE:
                    return AnswerState.NEGATIVE;
                case POSITIVE:
                    return AnswerState.POSITIVE;
            }
            throw new IllegalStateException();
        }
    }

    class HeaderViewHolder extends ViewHolder {

        private OmegaTextView titleOmegaTextView;

        HeaderViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_question_header);
            titleOmegaTextView = (OmegaTextView) itemView;
        }

        @Override
        protected void onBind(Question item) {
            Criteria criteria = item.getCriteria();
            titleOmegaTextView.setText(criteria.getTitle());
            titleOmegaTextView.setStartText(Text.from(R.string.format_criteria, criteria.getSuffix()));
        }

    }

    public interface QuestionsListener {

        void onAnswerStateChanged(Question question);

    }
}
