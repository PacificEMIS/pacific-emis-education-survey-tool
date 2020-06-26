package org.pacific_emis.surveys.wash.ui.questions;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.pacific_emis.surveys.wash_core.data.model.mutable.MutableQuestion;
import org.pacific_emis.surveys.wash_core.data.serialization.model.Relation;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class QuestionRelationsHelper {

    private final List<QuestionVisibilityWrapper> items;
    private final Listener listener;
    private final QuestionsAdapter.QuestionsListener questionsListener;
    private WeakReference<Context> contextWeakReference;

    public QuestionRelationsHelper(Context context,
                                   @NonNull List<MutableQuestion> questions,
                                   @NonNull Listener listener,
                                   QuestionsAdapter.QuestionsListener questionsListener) {
        items = questions.stream().map(QuestionVisibilityWrapper::new).collect(Collectors.toList());
        contextWeakReference = new WeakReference<>(context);
        this.listener = listener;
        this.questionsListener = questionsListener;
        initVisibleStates(0);
    }

    public void onQuestionAnswerChanged(MutableQuestion question) {
        int visibleQuestionsCount = collectVisibleQuestions().size();

        Optional<QuestionVisibilityWrapper> changedQuestionWrapperOptional = items.stream()
                .filter(it -> it.getQuestion().equals(question))
                .findFirst();

        if (changedQuestionWrapperOptional.isPresent()) {
            QuestionVisibilityWrapper questionVisibilityWrapper = changedQuestionWrapperOptional.get();
            questionVisibilityWrapper.setQuestion(question);
            initVisibleStates(visibleQuestionsCount);
        }
    }

    private void initVisibleStates(int visibleQuestionsCountBeforeUpdate) {
        items.forEach(this::updateChildrenVisibility);
        collectVisibleItemsAndNotify(visibleQuestionsCountBeforeUpdate);
    }

    private void updateChildrenVisibility(@NonNull QuestionVisibilityWrapper questionWrapper) {
        findChildrenIndexes(questionWrapper).forEach(i -> updateChildVisibility(items.get(i), questionWrapper));
    }

    private void collectVisibleItemsAndNotify(int visibleQuestionsCountBeforeUpdate) {
        final List<MutableQuestion> visibleQuestions = collectVisibleQuestions();
        if (visibleQuestions.size() != visibleQuestionsCountBeforeUpdate) {
            listener.onVisibleItemsChanged(visibleQuestions);
        }
    }

    private List<Integer> findChildrenIndexes(@NonNull QuestionVisibilityWrapper questionWrapper) {
        List<Integer> indexes = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            if (questionWrapper.getQuestion().getPrefix().equals(items.get(i).getParentPrefix())) {
                indexes.add(i);
            }
        }

        return indexes;
    }

    private void updateChildVisibility(@NonNull QuestionVisibilityWrapper childWrapper,
                                       @NonNull QuestionVisibilityWrapper parentWrapper) {
        Relation relation = childWrapper.getQuestion().getRelation();
        Context context = contextWeakReference.get();

        if (relation == null || context == null) {
            return;
        }

        MutableQuestion parentQuestion = parentWrapper.getQuestion();
        boolean isVisible = parentQuestion.isAnswerInRelation(context, relation);
        childWrapper.setVisible(isVisible);

        // clear answers in questions that become unavailable (but I may be wrong about this behavior)
        if (!isVisible) {
            MutableQuestion question = childWrapper.getQuestion();
            question.clearAnswer();
            questionsListener.onAnswerStateChanged(question);
        }
    }

    private List<MutableQuestion> collectVisibleQuestions() {
        return items.stream()
                .filter(QuestionVisibilityWrapper::isVisible)
                .map(QuestionVisibilityWrapper::getQuestion)
                .collect(Collectors.toList());
    }

    public interface Listener {
        void onVisibleItemsChanged(List<MutableQuestion> visibleItems);
    }

    private class QuestionVisibilityWrapper {
        private MutableQuestion question;
        private boolean isVisible;

        QuestionVisibilityWrapper(MutableQuestion question) {
            this.question = question;
            this.isVisible = question.getRelation() == null;
        }

        MutableQuestion getQuestion() {
            return question;
        }

        boolean isVisible() {
            return isVisible;
        }

        void setVisible(boolean visible) {
            isVisible = visible;
        }

        void setQuestion(MutableQuestion question) {
            this.question = question;
        }

        @Nullable
        String getParentPrefix() {
            if (question.getRelation() != null) {
                return question.getRelation().getQuestionId();
            } else {
                return null;
            }
        }
    }

}
