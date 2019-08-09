package fm.doe.national.accreditation_core.data.model.mutable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.accreditation_core.data.model.Answer;
import fm.doe.national.accreditation_core.data.model.AnswerState;
import fm.doe.national.core.data.model.BaseSerializableIdentifiedObject;
import fm.doe.national.core.data.model.ConflictResolveStrategy;
import fm.doe.national.core.data.model.Photo;
import fm.doe.national.core.data.model.mutable.BaseMutableEntity;
import fm.doe.national.core.data.model.mutable.MutablePhoto;
import fm.doe.national.core.utils.CollectionUtils;

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
    public MutableAnswer merge(Answer other, ConflictResolveStrategy strategy, boolean isOtherAnswered) {
        boolean haveChanges = false;

        if (strategy == ConflictResolveStrategy.THEIRS && other.getState() != AnswerState.NOT_ANSWERED) {
            haveChanges = answerState != other.getState();
            answerState = other.getState();
        }

        String externalComment = other.getComment();
        if (externalComment != null) {
            comment = externalComment;
            haveChanges = true;
        } else if (isOtherAnswered && strategy == ConflictResolveStrategy.THEIRS) {
            comment = null;
            haveChanges = true;
        }

        List<? extends Photo> otherPhotos = other.getPhotos();
        if (!CollectionUtils.isEmpty(otherPhotos)) {
            this.photos = otherPhotos.stream()
                    .map(MutablePhoto::new)
                    .peek(photo -> photo.setId(BaseSerializableIdentifiedObject.DEFAULT_ID))
                    .collect(Collectors.toList());
            haveChanges = true;
        } else if (isOtherAnswered && strategy == ConflictResolveStrategy.THEIRS) {
            this.photos = null;
            haveChanges = true;
        }

        return haveChanges ? this : null;
    }
}
