package org.pacific_emis.surveys.wash_core.data.model.mutable;

import android.content.Context;

import androidx.annotation.Nullable;

import org.pacific_emis.surveys.core.data.model.ConflictResolveStrategy;
import org.pacific_emis.surveys.core.data.model.mutable.MutableProgress;
import org.pacific_emis.surveys.core.data.model.mutable.MutableSurvey;
import org.pacific_emis.surveys.core.utils.CollectionUtils;
import org.pacific_emis.surveys.wash_core.data.model.Group;
import org.pacific_emis.surveys.wash_core.data.model.WashSurvey;
import org.pacific_emis.surveys.wash_core.data.serialization.model.Relation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MutableWashSurvey extends MutableSurvey implements WashSurvey {

    @Nullable
    private List<MutableGroup> groups;

    public MutableWashSurvey(WashSurvey other) {
        super(other);
        if (other.getGroups() != null) {
            this.groups = other.getGroups().stream().map(MutableGroup::new).collect(Collectors.toList());
        }
    }

    @Nullable
    @Override
    public List<MutableGroup> getGroups() {
        return groups;
    }

    public void setGroups(@Nullable List<MutableGroup> groups) {
        this.groups = groups;
    }

    public List<MutableAnswer> merge(WashSurvey other, ConflictResolveStrategy strategy) {
        if (strategy == ConflictResolveStrategy.THEIRS) {
            setCompleteDate(other.getCompleteDate());
            setCreateDate(other.getCreateDate());
            setSurveyTag(other.getSurveyTag());
        }

        List<? extends Group> externalGroups = other.getGroups();
        List<MutableAnswer> changedAnswers = new ArrayList<>();

        if (!CollectionUtils.isEmpty(externalGroups)) {
            for (Group group : externalGroups) {
                for (MutableGroup mutableGroup : getGroups()) {
                    if (mutableGroup.getPrefix().equals(group.getPrefix())) {
                        changedAnswers.addAll(mutableGroup.merge(group, strategy));
                        break;
                    }
                }
            }
        }

        return changedAnswers;
    }

    public MutableProgress calculateProgress(Context context) {
        int answerCount = 0;
        int questionsCount = 0;

        for (MutableGroup group : getGroups()) {
            for (MutableSubGroup subGroup : group.getSubGroups()) {
                if (subGroup.getQuestions() != null) {
                    for (MutableQuestion question : subGroup.getQuestions()) {
                        if (isQuestionAvailable(context, subGroup, question)) {
                            questionsCount++;
                        }
                        if (question.isAnswered()) {
                            answerCount++;
                        }
                    }
                }
            }
        }

        return new MutableProgress(questionsCount, answerCount);
    }

    @Override
    public MutableWashSurvey toMutable() {
        return this;
    }

    private boolean isQuestionAvailable(Context context, MutableSubGroup subGroup, MutableQuestion child) {
        Optional<MutableQuestion> parentQuestionOp = findParentQuestion(subGroup, child);

        if (!parentQuestionOp.isPresent()) {
            return true;
        }

        if (context == null) {
            return false;
        }

        MutableQuestion parentQuestion = parentQuestionOp.get();
        Relation relation = child.getRelation();
        assert relation != null;

        return parentQuestion.isAnswerInRelation(context, relation);
    }

    private Optional<MutableQuestion> findParentQuestion(MutableSubGroup subGroup, MutableQuestion child) {
        if (child.getRelation() == null) {
            return Optional.empty();
        }

        String parentId = child.getRelation().getQuestionId();
        return subGroup.getQuestions().parallelStream()
                .filter(question -> question.getPrefix().equals(parentId))
                .findFirst();
    }
}
