package fm.doe.national.wash.ui.questions;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;

import fm.doe.national.survey_core.ui.custom_views.BinaryAnswerSelectorView;
import fm.doe.national.wash.R;
import fm.doe.national.wash_core.data.model.BinaryAnswerState;
import fm.doe.national.wash_core.data.model.Question;
import fm.doe.national.wash_core.data.model.QuestionType;
import fm.doe.national.wash_core.data.model.mutable.MutableQuestion;

public class QuestionsAdapter extends BaseListAdapter<MutableQuestion> {

    private QuestionsListener questionsListener;

    public QuestionsAdapter(QuestionsListener questionsListener) {
        this.questionsListener = questionsListener;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType().ordinal();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        for (QuestionType questionType : QuestionType.values()) {
            if (questionType.ordinal() == viewType) {
                return provideViewHolderForQuestionType(questionType, parent);
            }
        }
        throw new IllegalStateException();
    }

    private ViewHolder provideViewHolderForQuestionType(QuestionType questionType, ViewGroup parent) {
        switch (questionType) {
            case BINARY:
                return new BinaryQuestionViewHolder(parent);
            case TERNARY:
                break;
            case TEXT_INPUT:
                break;
            case NUMBER_INPUT:
                break;
            case PHONE_INPUT:
                break;
            case GEOLOCATION:
                break;
            case PHOTO:
                break;
            case SINGLE_SELECTION:
                break;
            case MULTI_SELECTION:
                break;
            case COMPLEX_BINARY:
                break;
        }
        throw new IllegalStateException();
    }

    @Override
    protected ViewHolder provideViewHolder(ViewGroup parent) {
        // unused
        return null;
    }

    class BinaryQuestionViewHolder extends QuestionViewHolder implements BinaryAnswerSelectorView.StateChangedListener {

        private BinaryAnswerSelectorView binaryAnswerSelectorView;

        BinaryQuestionViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_binary_question);
            binaryAnswerSelectorView.setListener(this);
        }

        @Override
        protected void bindViews() {
            super.bindViews();
            binaryAnswerSelectorView = findViewById(R.id.binaryanswerselectorview);
        }

        @Override
        protected void onBind(MutableQuestion item) {
            super.onBind(item);
            binaryAnswerSelectorView.setStateNotNotifying(convertToUiState(item.getAnswer().getBinaryAnswerState()));
        }

        @Override
        public void onStateChanged(BinaryAnswerSelectorView view, BinaryAnswerSelectorView.State state) {
            getItem().getAnswer().setBinaryAnswerState(convertFromUiState(state));
            questionsListener.onAnswerStateChanged(getItem());
        }

        private BinaryAnswerSelectorView.State convertToUiState(@Nullable BinaryAnswerState state) {
            if (state == null) {
                return BinaryAnswerSelectorView.State.NEUTRAL;
            }

            switch (state) {
                case YES:
                    return BinaryAnswerSelectorView.State.POSITIVE;
                case NO:
                    return BinaryAnswerSelectorView.State.NEGATIVE;
            }

            throw new IllegalStateException();
        }

        @Nullable
        private BinaryAnswerState convertFromUiState(BinaryAnswerSelectorView.State state) {
            switch (state) {
                case NEUTRAL:
                    return null;
                case NEGATIVE:
                    return BinaryAnswerState.NO;
                case POSITIVE:
                    return BinaryAnswerState.YES;
            }
            throw new IllegalStateException();
        }
    }

    private class QuestionViewHolder extends ViewHolder {

        private TextView prefixTextView;
        private TextView titleTextView;
        private ImageButton photosButton;
        private ImageButton commentButton;

        QuestionViewHolder(ViewGroup parent, int res) {
            super(parent, res);
            bindViews();
            photosButton.setOnClickListener(this);
            commentButton.setOnClickListener(this);
            titleTextView.setOnLongClickListener(this);
        }

        protected void bindViews() {
            prefixTextView = findViewById(R.id.textview_prefix);
            titleTextView = findViewById(R.id.textview_title);
            photosButton = findViewById(R.id.imagebutton_photo);
            commentButton = findViewById(R.id.imagebutton_comment);
        }

        @Override
        protected void onBind(MutableQuestion item) {
            prefixTextView.setText(item.getPrefix());
            titleTextView.setText(item.getTitle());
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.imagebutton_photo) {
                questionsListener.onPhotoPressed(getItem());
            } else if (id == R.id.imagebutton_comment) {
                questionsListener.onCommentPressed(getItem());
            } else {
                super.onClick(v);
            }
        }
    }

    public interface QuestionsListener {

        void onPhotoPressed(Question question);

        void onCommentPressed(Question question);

        void onAnswerStateChanged(Question question);

    }
}
