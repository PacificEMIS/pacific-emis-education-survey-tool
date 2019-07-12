package fm.doe.national.accreditation_core.data.serialization.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.accreditation_core.data.model.AccreditationSurvey;
import fm.doe.national.accreditation_core.data.model.Category;
import fm.doe.national.core.data.model.Progress;
import fm.doe.national.core.data.serialization.converters.AppRegionConverter;
import fm.doe.national.core.data.serialization.converters.SurveyTypeConverter;
import fm.doe.national.core.preferences.entities.AppRegion;
import fm.doe.national.core.preferences.entities.SurveyType;

@Root(name = "survey")
public class SerializableAccreditationSurvey implements AccreditationSurvey {

    @Element
    @Convert(SurveyTypeConverter.class)
    SurveyType type;

    @Element
    @Convert(AppRegionConverter.class)
    AppRegion region;

    @Element
    int version;

    @Nullable
    @Element(required = false)
    Date createDate;

    @Nullable
    @Element(required = false)
    Date surveyDate;

    @Nullable
    @Element(required = false)
    Date completeDate;

    @Nullable
    @Element(required = false)
    String schoolId;

    @Nullable
    @Element(required = false)
    String schoolName;

    @ElementList(inline = true)
    List<SerializableCategory> categories;

    public SerializableAccreditationSurvey() {
    }

    public SerializableAccreditationSurvey(@NonNull AccreditationSurvey other) {
        this.version = other.getVersion();
        this.type = other.getSurveyType();
        this.region = other.getAppRegion();
        this.createDate = other.getCreateDate();
        this.surveyDate = other.getSurveyDate();
        this.completeDate = other.getCompleteDate();
        this.schoolName = other.getSchoolName();
        this.schoolId = other.getSchoolId();
        if (other.getCategories() != null) {
            this.categories = other.getCategories().stream().map(SerializableCategory::new).collect(Collectors.toList());
        }
    }

    @Override
    public int getVersion() {
        return version;
    }

    @NonNull
    @Override
    public SurveyType getSurveyType() {
        return type;
    }

    @Nullable
    @Override
    public Date getCreateDate() {
        return createDate;
    }

    @Nullable
    @Override
    public Date getSurveyDate() {
        return surveyDate;
    }

    @Nullable
    @Override
    public Date getCompleteDate() {
        return completeDate;
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

    @Nullable
    @Override
    public List<? extends Category> getCategories() {
        return categories;
    }

    @Override
    public long getId() {
        return 0;
    }

    @NonNull
    @Override
    public Progress getProgress() {
        throw new IllegalStateException();
    }

    @NonNull
    @Override
    public AppRegion getAppRegion() {
        return region;
    }
}
