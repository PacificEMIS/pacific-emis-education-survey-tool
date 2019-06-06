package fm.doe.national.wash_core.data.model.mutable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fm.doe.national.core.data.model.mutable.BaseMutableEntity;
import fm.doe.national.core.utils.CollectionUtils;
import fm.doe.national.wash_core.data.model.Question;
import fm.doe.national.wash_core.data.model.QuestionType;
import fm.doe.national.wash_core.data.model.TernaryAnswerState;
import fm.doe.national.wash_core.data.serialization.model.Relation;
import fm.doe.national.wash_core.data.serialization.model.Variant;

public class MutableQuestion extends BaseMutableEntity implements Question {

    @NonNull
    private String title;

    @NonNull
    private String prefix;

    @NonNull
    private QuestionType questionType;

    @Nullable
    private List<String> items;

    @Nullable
    private List<Variant> variants;

    @Nullable
    private Relation relation;

    @Nullable
    private MutableAnswer answer;

    public MutableQuestion(Question other) {
        this(other.getTitle(), other.getPrefix(), other.getType());

        this.id = other.getId();
        this.items = other.getItems();
        this.variants = other.getVariants();
        this.relation = other.getRelation();

        if (other.getAnswer() != null) {
            this.answer = new MutableAnswer(other.getAnswer());
        }
    }

    public MutableQuestion(@NonNull String title, @NonNull String prefix, @NonNull QuestionType questionType) {
        this.title = title;
        this.prefix = prefix;
        this.questionType = questionType;
    }

    @NonNull
    @Override
    public String getTitle() {
        return title;
    }

    @NonNull
    @Override
    public String getPrefix() {
        return prefix;
    }

    @NonNull
    @Override
    public QuestionType getType() {
        return questionType;
    }

    @Nullable
    @Override
    public List<String> getItems() {
        return items;
    }

    @Nullable
    @Override
    public List<Variant> getVariants() {
        return variants;
    }

    @Nullable
    @Override
    public Relation getRelation() {
        return relation;
    }

    @Nullable
    @Override
    public MutableAnswer getAnswer() {
        return answer;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public void setPrefix(@NonNull String prefix) {
        this.prefix = prefix;
    }

    public void setQuestionType(@NonNull QuestionType questionType) {
        this.questionType = questionType;
    }

    public void setItems(@Nullable List<String> items) {
        this.items = items;
    }

    public void setVariants(@Nullable List<Variant> variants) {
        this.variants = variants;
    }

    public void setRelation(@Nullable Relation relation) {
        this.relation = relation;
    }

    public void setAnswer(@Nullable MutableAnswer answer) {
        this.answer = answer;
    }

    public void setAnswerInputText(@Nullable String text) {
        if (answer == null) {
            return;
        }

        answer.setInputText(text);
    }

    public void setCheckedState(int atPosition, boolean checked) {
        switch (questionType) {
            case SINGLE_SELECTION:
                setCheckedStateOnSingle(atPosition, checked);
                break;
            case MULTI_SELECTION:
                setCheckedStateOnMultiple(atPosition, checked);
                break;
            case TERNARY:
                setCheckedStateOnTernary(atPosition, checked);
                break;
        }
    }

    private void setCheckedStateOnSingle(int atPosition, boolean checked) {
        List<Integer> selectedIndexes = getSelectedIndexes();
        Integer currentSelectedIndex = selectedIndexes.isEmpty() ? null : selectedIndexes.get(0);

        if (answer == null || CollectionUtils.isEmpty(items) || items.size() <= atPosition) {
            return;
        }

        if (checked) {
            answer.setItems(Collections.singletonList(items.get(atPosition)));
        } else if (currentSelectedIndex != null && currentSelectedIndex == atPosition) {
            answer.setItems(null);
        }

    }

    private void setCheckedStateOnMultiple(int atPosition, boolean checked) {
        if (!CollectionUtils.isEmpty(items) && items.size() > atPosition && answer != null) {
            List<String> existingAnswerItems = answer.getItems();
            ArrayList<String> wrappedExistingAnswerItems =
                    existingAnswerItems == null ? CollectionUtils.emptyArrayList() : new ArrayList<>(existingAnswerItems);
            String item = items.get(atPosition);

            if (checked) {
                wrappedExistingAnswerItems.add(item);
            } else {
                wrappedExistingAnswerItems.remove(item);
            }

            answer.setItems(wrappedExistingAnswerItems);
        }
    }

    private void setCheckedStateOnTernary(int atPosition, boolean checked) {
        if (answer == null) {
            return;
        }

        TernaryAnswerState answerState = answer.getTernaryAnswerState();
        Integer currentAnswerStatePosition = answerState != null ? answerState.ordinal() : null;

        if (checked) {
            answer.setTernaryAnswerState(TernaryAnswerState.values()[atPosition]);
        } else if (currentAnswerStatePosition != null && atPosition == currentAnswerStatePosition) {
            answer.setTernaryAnswerState(null);
        }
    }

    @NonNull
    public ArrayList<Integer> getSelectedIndexes() {
        if (CollectionUtils.isEmpty(items) || answer == null || CollectionUtils.isEmpty(answer.getItems())) {
            return CollectionUtils.emptyArrayList();
        }

        ArrayList<Integer> selectedIndexes = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            if (answer.getItems().contains(items.get(i))) {
                selectedIndexes.add(i);
            }
        }

        return selectedIndexes;
    }

}
