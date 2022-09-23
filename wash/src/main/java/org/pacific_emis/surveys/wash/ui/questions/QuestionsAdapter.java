package org.pacific_emis.surveys.wash.ui.questions;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import org.pacific_emis.surveys.core.data.model.Teacher;
import org.pacific_emis.surveys.core.ui.views.InputFieldLayout;
import org.pacific_emis.surveys.core.ui.views.InputAutoCompleteFieldLayout;
import org.pacific_emis.surveys.core.utils.CollectionUtils;
import org.pacific_emis.surveys.core.utils.ViewUtils;
import org.pacific_emis.surveys.survey_core.ui.custom_views.BinaryAnswerSelectorView;
import org.pacific_emis.surveys.survey_core.ui.survey.BaseQuestionsAdapter;
import org.pacific_emis.surveys.wash.R;
import org.pacific_emis.surveys.wash.ui.custom_views.answer_selector_view.AnswerSelectorView;
import org.pacific_emis.surveys.wash_core.data.model.BinaryAnswerState;
import org.pacific_emis.surveys.wash_core.data.model.Location;
import org.pacific_emis.surveys.wash_core.data.model.QuestionType;
import org.pacific_emis.surveys.wash_core.data.model.TernaryAnswerState;
import org.pacific_emis.surveys.wash_core.data.model.mutable.MutableAnswer;
import org.pacific_emis.surveys.wash_core.data.model.mutable.MutableQuestion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionsAdapter extends BaseQuestionsAdapter<MutableQuestion> implements QuestionRelationsHelper.Listener {

    private QuestionsListener questionsListener;
    private QuestionRelationsHelper relationsHelper;
    private List<Teacher> teacherList = new ArrayList<>();

    public QuestionsAdapter(BaseQuestionsAdapter.Listener<MutableQuestion> baseListener, QuestionsListener questionsListener) {
        super(baseListener);
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
                return new TernaryViewHolder(parent);
            case TEXT_INPUT:
                return new TextInputViewHolder(parent);
            case NUMBER_INPUT:
                return new NumericTextInputViewHolder(parent);
            case PHONE_INPUT:
                return new PhoneTextInputViewHolder(parent);
            case GEOLOCATION:
                return new GeoLocationViewHolder(parent);
            case PHOTO:
                return new PhotoQuestionViewHolder(parent);
            case SINGLE_SELECTION:
                return new SingleSelectionViewHolder(parent);
            case MULTI_SELECTION:
                return new MultipleSelectionViewHolder(parent);
            case COMPLEX_BINARY:
                return new VariantsViewHolder(parent, VariantsAdapter.Type.BINARY);
            case COMPLEX_NUMBER_INPUT:
                return new VariantsViewHolder(parent, VariantsAdapter.Type.NUMERIC);
            case COMPLEX_TEXT_INPUT:
                return new TextInputAutoCompleteTeacherViewHolder(parent);
        }
        throw new IllegalStateException();
    }

    public void setTeacherList(List<Teacher> teacherList) {
        this.teacherList = teacherList;
    }

    public void setRelativeItems(Context context, List<MutableQuestion> items) {
        relationsHelper = new QuestionRelationsHelper(context, items , this, questionsListener);
    }

    private void notifyAnswerStateChanged(MutableQuestion question) {
        relationsHelper.onQuestionAnswerChanged(question);
        questionsListener.onAnswerStateChanged(question);
    }

    @Override
    public void onVisibleItemsChanged(List<MutableQuestion> visibleItems) {
        this.setItems(visibleItems);
    }

    class VariantsViewHolder extends QuestionViewHolder implements VariantsAdapter.OnAnswerChangeListener {

        private final VariantsAdapter.Type type;
        private RecyclerView recyclerView = findViewById(R.id.recyclerview);
        private VariantsAdapter adapter;

        VariantsViewHolder(ViewGroup parent, VariantsAdapter.Type type) {
            super(parent, R.layout.item_complex_question);
            this.type = type;
        }

        @Override
        protected void onBind(MutableQuestion item) {
            super.onBind(item);
            adapter = new VariantsAdapter(type, item, this);
            recyclerView.setAdapter(adapter);
        }

        @Override
        public void onAnswerChange() {
            notifyAnswerStateChanged(getItem());
        }
    }

    class GeoLocationViewHolder extends QuestionViewHolder {

        private Button positionButton = findViewById(R.id.button_geo);
        private TextView positionTextView = findViewById(R.id.textview_position);

        GeoLocationViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_geolocation_question);
            positionButton.setOnClickListener(this);
        }

        @Override
        protected void onBind(MutableQuestion item) {
            super.onBind(item);

            MutableAnswer answer = item.getAnswer();

            if (answer == null) {
                return;
            }

            Location existingLocation = answer.getLocation();

            if (existingLocation == null) {
                return;
            }

            positionTextView.setText(getString(R.string.format_position, existingLocation.latitude, existingLocation.longitude));
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.button_geo) {
                questionsListener.onTakeLocationPressed(getItem(), getAdapterPosition());
            } else {
                super.onClick(v);
            }
        }
    }

    class PhotoQuestionViewHolder extends QuestionViewHolder {

        PhotoQuestionViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_photo_question);
        }

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
            MutableAnswer answer = getItem().getAnswer();

            if (answer == null) {
                return;
            }

            binaryAnswerSelectorView.setStateNotNotifying(convertToUiState(answer.getBinaryAnswerState()));
        }

        @Override
        public void onStateChanged(BinaryAnswerSelectorView view, BinaryAnswerSelectorView.State state) {
            MutableAnswer answer = getItem().getAnswer();

            if (answer == null) {
                return;
            }

            answer.setBinaryAnswerState(convertFromUiState(state));
            notifyAnswerStateChanged(getItem());
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

    class SingleSelectionViewHolder extends SelectionViewHolder {

        SingleSelectionViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_single_selection_question);
        }

        @Override
        public void onCheckedChange(int atPosition, boolean checked) {
            MutableQuestion question = getItem();
            question.setCheckedState(atPosition, checked);
            notifyAnswerStateChanged(question);
        }
    }

    class MultipleSelectionViewHolder extends SelectionViewHolder {

        MultipleSelectionViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_multiple_selection_question);
        }

        @Override
        public void onCheckedChange(int atPosition, boolean checked) {
            MutableQuestion question = getItem();
            question.setCheckedState(atPosition, checked);
            notifyAnswerStateChanged(question);
        }
    }

    class TernaryViewHolder extends QuestionViewHolder implements AnswerSelectorView.Listener {

        private AnswerSelectorView answerSelectorView = findViewById(R.id.answerselectionview);

        TernaryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_single_selection_question);
            answerSelectorView.setListener(this);
        }

        @Override
        protected void onBind(MutableQuestion item) {
            super.onBind(item);

            List<String> values = Arrays.stream(TernaryAnswerState.values())
                    .map(v -> v.getText().getString(getContext()))
                    .collect(Collectors.toList());

            MutableAnswer answer = item.getAnswer();
            TernaryAnswerState answerState = answer != null ? answer.getTernaryAnswerState() : null;
            Integer selectedIndex = answerState != null ? answerState.ordinal() : null;

            answerSelectorView.setItems(
                    values,
                    selectedIndex != null ? CollectionUtils.singletonArrayList(selectedIndex) : CollectionUtils.emptyArrayList()
            );
        }

        @Override
        public void onCheckedChange(int atPosition, boolean checked) {
            MutableQuestion question = getItem();
            question.setCheckedState(atPosition, checked);
            notifyAnswerStateChanged(question);
        }

    }

    class NumericTextInputViewHolder extends TextInputViewHolder {

        NumericTextInputViewHolder(ViewGroup parent) {
            super(parent);
            inputFieldLayout.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

    }

    class PhoneTextInputViewHolder extends TextInputViewHolder {

        PhoneTextInputViewHolder(ViewGroup parent) {
            super(parent);
            inputFieldLayout.setInputType(InputType.TYPE_CLASS_PHONE);
        }

    }

    class TextInputViewHolder extends QuestionViewHolder implements InputFieldLayout.OnDonePressedListener {

        InputFieldLayout inputFieldLayout = findViewById(R.id.inputfieldlayout);

        TextInputViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_text_input_question);
            inputFieldLayout.setOnDonePressedListener(this);
        }

        @Override
        protected void onBind(MutableQuestion item) {
            super.onBind(item);

            if (item.getAnswer() != null) {
                inputFieldLayout.setStartingText(item.getAnswer().getInputText());
            }
            inputFieldLayout.setDoneButtonVisible(false);
        }

        @Override
        public void onDonePressed(View view, @Nullable String content) {
            if (view.getId() == R.id.inputfieldlayout) {
                MutableQuestion question = getItem();
                question.setAnswerInputText(content);
                notifyAnswerStateChanged(question);
                hideKeyboard();
            }
        }

        private void hideKeyboard() {
            ViewUtils.hideKeyboardAndClearFocus(inputFieldLayout, itemView);
        }
    }

    class TextInputAutoCompleteTeacherViewHolder extends QuestionViewHolder implements InputAutoCompleteFieldLayout.OnDonePressedListener  {

        InputAutoCompleteFieldLayout inputAutoComleteFieldLayout = findViewById(R.id.inputselectablefieldlayout);
        ViewGroup parent;

        TextInputAutoCompleteTeacherViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_auto_complete_input);
            this.parent = parent;
            inputAutoComleteFieldLayout.setOnDonePressedListener(this);
        }

        @Override
        protected void onBind(MutableQuestion item) {
            super.onBind(item);

            if (item.getAnswer() != null) {
                inputAutoComleteFieldLayout.setStartingText(item.getAnswer().getInputText());
            }
            inputAutoComleteFieldLayout.setContentList(teacherList);
            inputAutoComleteFieldLayout.setDoneButtonVisible(false);
        }

        @Override
        public void onDonePressed(View view, @Nullable Object content) {
            if (view.getId() == R.id.inputselectablefieldlayout) {
                MutableQuestion question = getItem();
                if (content instanceof String) {
                    question.setAnswerInputText((String) content);
                }
                if (content instanceof Teacher) {
                    Teacher teacher = (Teacher) content;
                    question.setAnswerInputText(teacher.getName());
                }
                notifyAnswerStateChanged(question);
                hideKeyboard();
            }
        }

        private void hideKeyboard() {
            ViewUtils.hideKeyboardAndClearFocus(inputAutoComleteFieldLayout, itemView);
        }
    }

    private abstract class SelectionViewHolder extends QuestionViewHolder implements AnswerSelectorView.Listener {

        private AnswerSelectorView answerSelectorView = findViewById(R.id.answerselectionview);

        SelectionViewHolder(ViewGroup parent, int res) {
            super(parent, res);
            answerSelectorView.setListener(this);
        }

        @Override
        protected void onBind(MutableQuestion item) {
            super.onBind(item);
            answerSelectorView.setItems(item.getItems(), item.getSelectedIndexes());
        }
    }

    private abstract class QuestionViewHolder extends BaseQuestionViewHolder {

        private TextView prefixTextView;
        private TextView titleTextView;

        QuestionViewHolder(ViewGroup parent, int res) {
            super(parent, res);
            bindViews();
            titleTextView.setOnLongClickListener(this);
        }

        protected void bindViews() {
            prefixTextView = findViewById(R.id.textview_prefix);
            titleTextView = findViewById(R.id.textview_title);
        }

        @Override
        protected void onBind(MutableQuestion item) {
            super.onBind(item);
            prefixTextView.setText(item.getPrefix());
            titleTextView.setText(item.getTitle());
        }
    }

    public interface QuestionsListener {

        void onAnswerStateChanged(MutableQuestion question);

        void onTakeLocationPressed(MutableQuestion question, int position);

    }
}
