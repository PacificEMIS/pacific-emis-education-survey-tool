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
import org.pacific_emis.surveys.core.preferences.entities.AppRegion;
import org.pacific_emis.surveys.core.preferences.entities.SurveyType;
import org.pacific_emis.surveys.core.utils.CollectionUtils;
import org.pacific_emis.surveys.core.utils.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MutableAccreditationSurvey extends BaseMutableEntity implements AccreditationSurvey {

    private int version;
    private SurveyType surveyType;
    private AppRegion appRegion;
    private Date createDate;
    private String surveyTag;
    private Date completeDate;
    private String schoolName;
    private String schoolId;
    private List<MutableCategory> categories;
    private MutableProgress progress = MutableProgress.createEmptyProgress();
    private SurveyState state;

    @Nullable
    private String createUser;

    @Nullable
    private String lastEditedUser;

    public static MutableAccreditationSurvey toMutable(@NonNull AccreditationSurvey accreditationSurvey) {
        if (accreditationSurvey instanceof MutableAccreditationSurvey) {
            return (MutableAccreditationSurvey) accreditationSurvey;
        }
        return new MutableAccreditationSurvey(accreditationSurvey);
    }

    public MutableAccreditationSurvey() {
    }

    public MutableAccreditationSurvey(@NonNull AccreditationSurvey other) {
        this.id = other.getId();
        this.version = other.getVersion();
        this.surveyType = other.getSurveyType();
        this.createDate = other.getCreateDate();
        this.surveyTag = other.getSurveyTag();
        this.completeDate = other.getCompleteDate();
        this.schoolName = other.getSchoolName();
        this.schoolId = other.getSchoolId();
        this.appRegion = other.getAppRegion();
        this.state = ObjectUtils.orElse(other.getState(), SurveyState.NOT_COMPLETED);
        this.createUser = other.getCreateUser();
        this.lastEditedUser = other.getLastEditedUser();

        if (other.getCategories() != null) {
            this.categories = other.getCategories().stream().map(MutableCategory::new).collect(Collectors.toList());
        }
    }

    @Override
    public int getVersion() {
        return version;
    }

    @NonNull
    @Override
    public SurveyType getSurveyType() {
        return surveyType;
    }

    @Nullable
    @Override
    public Date getCreateDate() {
        return createDate;
    }

    @Nullable
    @Override
    public String getSchoolName() {
        return schoolName;
    }

    @Nullable
    @Override
    public String getSchoolId() {
        return schoolId;
    }

    @NonNull
    @Override
    public List<MutableCategory> getCategories() {
        return categories;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setSurveyType(SurveyType surveyType) {
        this.surveyType = surveyType;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public void setCategories(List<MutableCategory> categories) {
        this.categories = categories;
    }

    @NonNull
    public MutableProgress getProgress() {
        return progress;
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

    public void setProgress(MutableProgress progress) {
        this.progress = progress;
    }

    @NonNull
    @Override
    public AppRegion getAppRegion() {
        return appRegion;
    }

    public void setAppRegion(AppRegion appRegion) {
        this.appRegion = appRegion;
    }

    @Override
    public String getSurveyTag() {
        return surveyTag;
    }

    @Nullable
    @Override
    public Date getCompleteDate() {
        return completeDate;
    }

    public void setSurveyTag(String surveyTag) {
        this.surveyTag = surveyTag;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    public MergeFieldsResult merge(AccreditationSurvey other, ConflictResolveStrategy strategy) {
        final MergeFieldsResult mergeResult = new MergeFieldsResult();

        if (strategy == ConflictResolveStrategy.THEIRS) {
            this.completeDate = other.getCompleteDate();
            this.createDate = other.getCreateDate();
            this.surveyTag = other.getSurveyTag();
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
    public SurveyState getState() {
        return state;
    }

    public void setState(SurveyState state) {
        this.state = state;
    }

    @Nullable
    @Override
    public String getCreateUser() {
        return createUser;
    }

    @Nullable
    @Override
    public String getLastEditedUser() {
        return lastEditedUser;
    }

    public void setCreateUser(@Nullable String createUser) {
        this.createUser = createUser;
    }

    public void setLastEditedUser(@Nullable String lastEditedUser) {
        this.lastEditedUser = lastEditedUser;
    }
}
