package fm.doe.national.accreditation_core.data.model.mutable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.accreditation_core.data.model.AccreditationSurvey;
import fm.doe.national.accreditation_core.data.model.Category;
import fm.doe.national.core.data.model.ConflictResolveStrategy;
import fm.doe.national.core.data.model.SurveyState;
import fm.doe.national.core.data.model.mutable.BaseMutableEntity;
import fm.doe.national.core.data.model.mutable.MutableProgress;
import fm.doe.national.core.preferences.entities.AppRegion;
import fm.doe.national.core.preferences.entities.SurveyType;
import fm.doe.national.core.utils.CollectionUtils;
import fm.doe.national.core.utils.ObjectUtils;

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

    public List<MutableAnswer> merge(AccreditationSurvey other, ConflictResolveStrategy strategy) {
        if (strategy == ConflictResolveStrategy.THEIRS) {
            this.completeDate = other.getCompleteDate();
            this.createDate = other.getCreateDate();
            this.surveyTag = other.getSurveyTag();
        }

        List<? extends Category> externalCategories = other.getCategories();
        List<MutableAnswer> changedAnswers = new ArrayList<>();

        if (!CollectionUtils.isEmpty(externalCategories)) {
            for (Category category : externalCategories) {
                for (MutableCategory mutableCategory : getCategories()) {
                    if (mutableCategory.getTitle().equals(category.getTitle())) {
                        changedAnswers.addAll(mutableCategory.merge(category, strategy));
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
}
