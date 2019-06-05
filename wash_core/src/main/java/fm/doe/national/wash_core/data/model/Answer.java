package fm.doe.national.wash_core.data.model;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.util.List;

import fm.doe.national.core.data.model.IdentifiedObject;
import fm.doe.national.core.data.model.Photo;
import fm.doe.national.core.utils.CollectionUtils;
import fm.doe.national.wash_core.data.serialization.model.Variant;

public interface Answer extends IdentifiedObject {

    @Nullable
    List<String> getItems();

    @Nullable
    String getComment();

    @Nullable
    List<Variant> getVariants();

    @Nullable
    Location getLocation();

    @Nullable
    List<? extends Photo> getPhotos();

    @Nullable
    BinaryAnswerState getBinaryAnswerState();

    @Nullable
    TernaryAnswerState getTernaryAnswerState();

    default boolean isAnsweredForQuestionType(QuestionType questionType) {
        switch (questionType) {
            case BINARY:
                return getBinaryAnswerState() != null;
            case TERNARY:
                return getTernaryAnswerState() != null;
            case TEXT_INPUT:
                return !TextUtils.isEmpty(getComment());
            case NUMBER_INPUT:
                return !TextUtils.isEmpty(getComment());
            case PHONE_INPUT:
                return !TextUtils.isEmpty(getComment());
            case GEOLOCATION:
                return getLocation() != null;
            case PHOTO:
                return !CollectionUtils.isEmpty(getPhotos());
            case SINGLE_SELECTION:
                return !CollectionUtils.isEmpty(getItems());
            case MULTI_SELECTION:
                return !CollectionUtils.isEmpty(getItems());
            case COMPLEX_BINARY:
                return !CollectionUtils.isEmpty(getVariants());
        }
        throw new IllegalStateException();
    }

}
