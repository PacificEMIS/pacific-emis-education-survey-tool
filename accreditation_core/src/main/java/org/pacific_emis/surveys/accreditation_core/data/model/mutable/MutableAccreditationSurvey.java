package org.pacific_emis.surveys.accreditation_core.data.model.mutable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.pacific_emis.surveys.accreditation_core.data.model.AccreditationSurvey;
import org.pacific_emis.surveys.accreditation_core.data.model.AnswerState;
import org.pacific_emis.surveys.accreditation_core.data.model.Category;
import org.pacific_emis.surveys.accreditation_core.data.model.MergeFieldsResult;
import org.pacific_emis.surveys.core.data.model.ConflictResolveStrategy;
import org.pacific_emis.surveys.core.data.model.SurveyState;
import org.pacific_emis.surveys.core.data.model.mutable.BaseMutableEntity;
import org.pacific_emis.surveys.core.data.model.mutable.MutableProgress;
import org.pacific_emis.surveys.core.data.model.mutable.MutableSurvey;
import org.pacific_emis.surveys.core.preferences.entities.AppRegion;
import org.pacific_emis.surveys.core.preferences.entities.SurveyType;
import org.pacific_emis.surveys.core.preferences.entities.UploadState;
import org.pacific_emis.surveys.core.utils.CollectionUtils;
import org.pacific_emis.surveys.core.utils.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MutableAccreditationSurvey extends MutableSurvey implements AccreditationSurvey {

    private List<MutableCategory> categories;

    public MutableAccreditationSurvey() {
    }

    public MutableAccreditationSurvey(@NonNull AccreditationSurvey other) {
        super(other);

        if (other.getCategories() != null) {
            this.categories = other.getCategories().stream().map(MutableCategory::new).collect(Collectors.toList());
        }
    }

    @NonNull
    @Override
    public List<MutableCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<MutableCategory> categories) {
        this.categories = categories;
    }

    public MutableProgress calculateProgress() {
        int answerCount = 0;
        int questionsCount = 0;
        for (MutableCategory category : getCategories()) {
            for (MutableStandard standard : category.getStandards()) {
                for (MutableCriteria criteria : standard.getCriterias()) {
                    for (MutableSubCriteria subCriteria : criteria.getSubCriterias()) {
                        questionsCount++;

                        if (subCriteria.getAnswer().getState() != AnswerState.NOT_ANSWERED) {
                            answerCount++;
                        }
                    }
                }
            }
        }

        return new MutableProgress(questionsCount, answerCount);
    }

    public MergeFieldsResult merge(AccreditationSurvey other, ConflictResolveStrategy strategy) {
        final MergeFieldsResult mergeResult = new MergeFieldsResult();

        if (strategy == ConflictResolveStrategy.THEIRS) {
            setCompleteDate(other.getCompleteDate());
            setCreateDate(other.getCreateDate());
            setSurveyTag(other.getSurveyTag());
        }

        List<? extends Category> externalCategories = other.getCategories();

        if (!CollectionUtils.isEmpty(externalCategories)) {
            for (Category category : externalCategories) {
                for (MutableCategory mutableCategory : getCategories()) {
                    if (mutableCategory.getTitle().equals(category.getTitle())) {
                        mergeResult.plus(mutableCategory.merge(category, strategy));
                        break;
                    }
                }
            }
        }

        return mergeResult;
    }

    @Override
    public MutableAccreditationSurvey toMutable() {
        return this;
    }
}
