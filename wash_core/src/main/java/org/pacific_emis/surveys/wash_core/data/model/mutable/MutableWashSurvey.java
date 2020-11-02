package org.pacific_emis.surveys.wash_core.data.model.mutable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.pacific_emis.surveys.core.data.model.ConflictResolveStrategy;
import org.pacific_emis.surveys.core.data.model.SurveyState;
import org.pacific_emis.surveys.core.data.model.mutable.BaseMutableEntity;
import org.pacific_emis.surveys.core.data.model.mutable.MutableProgress;
import org.pacific_emis.surveys.core.preferences.entities.AppRegion;
import org.pacific_emis.surveys.core.preferences.entities.SurveyType;
import org.pacific_emis.surveys.core.utils.CollectionUtils;
import org.pacific_emis.surveys.core.utils.ObjectUtils;
import org.pacific_emis.surveys.wash_core.data.model.Group;
import org.pacific_emis.surveys.wash_core.data.model.WashSurvey;

public class MutableWashSurvey extends BaseMutableEntity implements WashSurvey {

    @Nullable
    private List<MutableGroup> groups;

    private int version;

    @NonNull
    private SurveyType surveyType;

    private Date createDate;
    private String surveyTag;

    @Nullable
    private Date completeDate;

    @Nullable
    private String schoolName;

    @Nullable
    private String schoolId;

    @NonNull
    private AppRegion appRegion;

    @NonNull
    private MutableProgress progress;

    @NonNull
    private SurveyState state;

    @Nullable
    private String createUser;

    @Nullable
    private String lastEditedUser;

    public MutableWashSurvey(WashSurvey other) {
        this(other.getVersion(), other.getSurveyType(), other.getAppRegion());

        this.id = other.getId();
        this.createDate = other.getCreateDate();
        this.surveyTag = other.getSurveyTag();
        this.completeDate = other.getCompleteDate();
        this.schoolId = other.getSchoolId();
        this.schoolName = other.getSchoolName();
        this.state = ObjectUtils.orElse(other.getState(), SurveyState.NOT_COMPLETED);
        this.createUser = other.getCreateUser();
        this.lastEditedUser = other.getLastEditedUser();

        if (other.getGroups() != null) {
            this.groups = other.getGroups().stream().map(MutableGroup::new).collect(Collectors.toList());
        }
    }

    public MutableWashSurvey(int version, @NonNull SurveyType surveyType, @NonNull AppRegion appRegion) {
        this.version = version;
        this.surveyType = surveyType;
        this.appRegion = appRegion;
        this.progress = MutableProgress.createEmptyProgress();
    }

    @Nullable
    @Override
    public List<MutableGroup> getGroups() {
        return groups;
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
    public AppRegion getAppRegion() {
        return appRegion;
    }

    @Nullable
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

    @NonNull
    @Override
    public MutableProgress getProgress() {
        return progress;
    }

    public void setGroups(@Nullable List<MutableGroup> groups) {
        this.groups = groups;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setSurveyType(@NonNull SurveyType surveyType) {
        this.surveyType = surveyType;
    }

    public void setCreateDate(@Nullable Date createDate) {
        this.createDate = createDate;
    }

    public void setSchoolName(@Nullable String schoolName) {
        this.schoolName = schoolName;
    }

    public void setSchoolId(@Nullable String schoolId) {
        this.schoolId = schoolId;
    }

    public void setAppRegion(@NonNull AppRegion appRegion) {
        this.appRegion = appRegion;
    }

    public void setProgress(@NonNull MutableProgress progress) {
        this.progress = progress;
    }

    public List<MutableAnswer> merge(WashSurvey other, ConflictResolveStrategy strategy) {
        if (strategy == ConflictResolveStrategy.THEIRS) {
            this.completeDate = other.getCompleteDate();
            this.createDate = other.getCreateDate();
            this.surveyTag = other.getSurveyTag();
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

    @Override
    public SurveyState getState() {
        return state;
    }

    public void setState(SurveyState state) {
        this.state = state;
    }

    public MutableProgress calculateProgress() {
        int answerCount = 0;
        int questionsCount = 0;

        for (MutableGroup group : getGroups()) {
            for (MutableSubGroup subGroup : group.getSubGroups()) {
                for (MutableQuestion question : subGroup.getQuestions()) {
                    questionsCount++;
                    if (question.isAnswered()) {
                        answerCount++;
                    }
                }
            }
        }

        return new MutableProgress(questionsCount, answerCount);
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
