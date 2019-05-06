package fm.doe.national.data.model.mutable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.data.model.Answer;
import fm.doe.national.data.model.AnswerState;
import fm.doe.national.data.model.Photo;

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
        if (otherAnswer.getPhotos() == null) {
            return;
        }

        ArrayList<MutablePhoto> photos = new ArrayList<>();
        for (Photo photo : otherAnswer.getPhotos()) {
            photos.add(new MutablePhoto(photo));
        }
        this.photos = photos;
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

    public List<MutablePhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<MutablePhoto> photos) {
        this.photos = photos;
    }
}
