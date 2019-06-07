package fm.doe.national.wash.ui.questions;

import android.app.Activity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;
import com.omega_r.libs.omegatypes.Text;
import com.omega_r.libs.views.OmegaTextView;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.core.utils.CollectionUtils;
import fm.doe.national.survey_core.ui.custom_views.BinaryAnswerSelectorView;
import fm.doe.national.wash.R;
import fm.doe.national.wash.ui.custom_views.answer_selector_view.AnswerSelectorView;
import fm.doe.national.wash_core.data.model.BinaryAnswerState;
import fm.doe.national.wash_core.data.model.Location;
import fm.doe.national.wash_core.data.model.QuestionType;
import fm.doe.national.wash_core.data.model.TernaryAnswerState;
import fm.doe.national.wash_core.data.model.mutable.MutableAnswer;
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
        }
        throw new IllegalStateException();
    }

    @Override
    protected ViewHolder provideViewHolder(ViewGroup parent) {
        // unused
        return null;
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
            questionsListener.onAnswerStateChanged(getItem());
        }
    }

    class GeoLocationViewHolder extends QuestionViewHolder {

        private Button positionButton = findViewById(R.id.button_geo);
        private OmegaTextView latitudeTextView = findViewById(R.id.omegatextview_latitude);
        private OmegaTextView longitudeTextView = findViewById(R.id.omegatextview_longitude);

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

            latitudeTextView.setText(Text.from(String.valueOf(existingLocation.latitude)));
            longitudeTextView.setText(Text.from(String.valueOf(existingLocation.longitude)));
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

    class SingleSelectionViewHolder extends SelectionViewHolder {

        SingleSelectionViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_single_selection_question);
        }

        @Override
        public void onCheckedChange(int atPosition, boolean checked) {
            MutableQuestion question = getItem();
            question.setCheckedState(atPosition, checked);
            questionsListener.onAnswerStateChanged(question);
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
            questionsListener.onAnswerStateChanged(question);
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
            questionsListener.onAnswerStateChanged(question);
        }

    }

    class NumericTextInputViewHolder extends TextInputViewHolder {

        NumericTextInputViewHolder(ViewGroup parent) {
            super(parent);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

    }

    class PhoneTextInputViewHolder extends TextInputViewHolder {

        PhoneTextInputViewHolder(ViewGroup parent) {
            super(parent);
            editText.setInputType(InputType.TYPE_CLASS_PHONE);
        }

    }

    class TextInputViewHolder extends QuestionViewHolder implements TextWatcher {

        EditText editText = findViewById(R.id.textinputedittext);
        private ImageButton doneButton = findViewById(R.id.imagebutton_done);
        private String existingValue;

        TextInputViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_text_input_question);
            doneButton.setOnClickListener(this);
            editText.addTextChangedListener(this);
        }

        @Override
        protected void onBind(MutableQuestion item) {
            super.onBind(item);

            if (item.getAnswer() != null) {
                existingValue = item.getAnswer().getInputText();
                editText.setText(existingValue);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // nothing
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // nothing
        }

        @Override
        public void afterTextChanged(Editable s) {
            doneButton.setVisibility(editText.getText().toString().equals(existingValue) ? View.GONE : View.VISIBLE);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.imagebutton_done) {
                String inputtedText = editText.getText().toString();
                MutableQuestion question = getItem();
                existingValue = inputtedText.isEmpty() ? null : inputtedText;
                question.setAnswerInputText(existingValue);
                questionsListener.onAnswerStateChanged(question);
                doneButton.setVisibility(View.GONE);
                hideKeyboard();
            } else {
                super.onClick(v);
            }
        }

        private void hideKeyboard() {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            itemView.requestFocus();
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

    private abstract class QuestionViewHolder extends ViewHolder {

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

        void onPhotoPressed(MutableQuestion question);

        void onCommentPressed(MutableQuestion question);

        void onAnswerStateChanged(MutableQuestion question);

        void onTakeLocationPressed(MutableQuestion question, int position);

    }
}
