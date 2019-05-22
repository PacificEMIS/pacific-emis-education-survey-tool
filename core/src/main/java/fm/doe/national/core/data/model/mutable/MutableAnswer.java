package fm.doe.national.core.data.model.mutable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.core.data.model.Answer;
import fm.doe.national.core.data.model.AnswerState;

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
}
