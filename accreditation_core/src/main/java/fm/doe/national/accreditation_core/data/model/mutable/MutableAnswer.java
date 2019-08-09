package fm.doe.national.accreditation_core.data.model.mutable;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.accreditation_core.data.model.Answer;
import fm.doe.national.accreditation_core.data.model.AnswerState;
import fm.doe.national.core.data.exceptions.NotImplementedException;
import fm.doe.national.core.data.model.BaseSerializableIdentifiedObject;
import fm.doe.national.core.data.model.ConflictResolveStrategy;
import fm.doe.national.core.data.model.Photo;
import fm.doe.national.core.data.model.mutable.BaseMutableEntity;
import fm.doe.national.core.data.model.mutable.MutablePhoto;
import fm.doe.national.core.utils.CollectionUtils;
import fm.doe.national.core.utils.TextUtil;

public class MutableAnswer extends BaseMutableEntity implements Answer {

    private AnswerState answerState;
    private String comment;
    private List<MutablePhoto> photos;

    public MutableAnswer() {
    }

    public MutableAnswer(@NonNull Answer otherAnswer) {
        this.id = otherAnswer.getId();
        this.answerState = otherAnswer.getState();
        this.comment = otherAnswer.getComment();
        if (otherAnswer.getPhotos() != null) {
            this.photos = otherAnswer.getPhotos().stream().map(MutablePhoto::new).collect(Collectors.toList());
        }
    }

    @Override
    public AnswerState getState() {
        return answerState;
    }

    public void setState(AnswerState state) {
        this.answerState = state;
    }

    @Nullable
    @Override
    public String getComment() {
        return comment;
    }

    public void setComment(@Nullable String comment) {
        this.comment = comment;
    }

    @NonNull
    public List<MutablePhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<MutablePhoto> photos) {
        this.photos = photos;
    }

    @Nullable
    public MutableAnswer merge(Answer other, ConflictResolveStrategy strategy) {
        if (strategy != ConflictResolveStrategy.THEIRS) {
            // MINE is not supported
            throw new NotImplementedException();
        }

        String otherComment = other.getComment();
        List<? extends Photo> otherPhotos = other.getPhotos();
        boolean haveChanges = other.getState() != AnswerState.NOT_ANSWERED ||
                !CollectionUtils.isEmpty(other.getPhotos()) ||
                !TextUtils.isEmpty(otherComment);

        if (!haveChanges) {
            return null;
        }

        answerState = other.getState();
        comment = otherComment;

        if (otherPhotos != null) {
            photos = otherPhotos
                    .stream()
                    .map(MutablePhoto::new)
                    .peek(photo -> photo.setId(BaseSerializableIdentifiedObject.DEFAULT_ID))
                    .collect(Collectors.toList());
        } else {
            photos = null;
        }

        return this;
    }
}
