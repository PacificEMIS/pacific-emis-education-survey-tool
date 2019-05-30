package fm.doe.national.accreditation.ui.questions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;
import com.omega_r.libs.omegatypes.Text;
import com.omega_r.libs.views.OmegaTextView;

import fm.doe.national.accreditation.R;
import fm.doe.national.accreditation.ui.custom_views.BinaryAnswerSelectorView;
import fm.doe.national.core.data.model.AnswerState;
import fm.doe.national.core.data.model.Criteria;
import fm.doe.national.core.data.model.SubCriteria;
import fm.doe.national.core.data.model.mutable.MutableAnswer;

public class QuestionsAdapter extends BaseListAdapter<Question> {

    private static final int VIEW_TYPE_CRITERIA = 0;
    private static final int VIEW_TYPE_SUB_CRITERIA = 1;

    private QuestionsListener questionsListener;

    public QuestionsAdapter(QuestionsListener questionsListener) {
        this.questionsListener = questionsListener;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getCriteria() == null ? VIEW_TYPE_SUB_CRITERIA : VIEW_TYPE_CRITERIA;
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

    @Override
    protected ViewHolder provideViewHolder(ViewGroup parent) {
        // unused
        return null;
    }

    class QuestionViewHolder extends ViewHolder implements BinaryAnswerSelectorView.StateChangedListener {

        private TextView prefixTextView;
        private TextView titleTextView;
        private TextView questionTextView;
        private BinaryAnswerSelectorView binaryAnswerSelectorView;
        private ImageButton photosButton;
        private ImageButton commentButton;

        QuestionViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_question);
            bindViews();
            photosButton.setOnClickListener(this);
            commentButton.setOnClickListener(this);
            binaryAnswerSelectorView.setListener(this);
        }

        private void bindViews() {
            prefixTextView = findViewById(R.id.textview_prefix);
            titleTextView = findViewById(R.id.textview_title);
            questionTextView = findViewById(R.id.textview_question);
            binaryAnswerSelectorView = findViewById(R.id.binary_answer_selector_view);
            photosButton = findViewById(R.id.imagebutton_photo);
            commentButton = findViewById(R.id.imagebutton_comment);
        }

        @Override
        protected void onBind(Question item) {
            SubCriteria subCriteria = item.getSubCriteria();
            prefixTextView.setText(getString(R.string.format_subcriteria, subCriteria.getSuffix()));
            titleTextView.setText(subCriteria.getTitle());
            questionTextView.setText(subCriteria.getInterviewQuestions());
            binaryAnswerSelectorView.setStateNotNotifying(convertToUiState(subCriteria.getAnswer().getState()));
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
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_header, parent, false));
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

        void onPhotoPressed(Question question);

        void onCommentPressed(Question question);

        void onAnswerStateChanged(Question question);

    }
}
