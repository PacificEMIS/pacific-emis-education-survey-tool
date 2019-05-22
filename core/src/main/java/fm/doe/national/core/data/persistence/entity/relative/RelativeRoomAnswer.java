package fm.doe.national.core.data.persistence.entity.relative;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.core.data.model.mutable.MutableAnswer;
import fm.doe.national.core.data.model.mutable.MutablePhoto;
import fm.doe.national.core.data.persistence.entity.RoomAnswer;
import fm.doe.national.core.data.persistence.entity.RoomPhoto;

public class RelativeRoomAnswer {

    @Embedded
    public RoomAnswer answer;

    @Relation(parentColumn = "uid", entityColumn = "answer_id")
    public List<RoomPhoto> photos;

    public MutableAnswer toMutableAnswer() {
        MutableAnswer mutableAnswer = new MutableAnswer(answer);
        mutableAnswer.setPhotos(photos.stream().map(MutablePhoto::new).collect(Collectors.toList()));
        return mutableAnswer;
    }

}
