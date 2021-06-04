package org.pacific_emis.surveys.wash_core.data.persistence.entity.relative;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;
import java.util.stream.Collectors;

import org.pacific_emis.surveys.core.data.model.mutable.MutablePhoto;
import org.pacific_emis.surveys.wash_core.data.model.mutable.MutableAnswer;
import org.pacific_emis.surveys.wash_core.data.persistence.entity.RoomAnswer;
import org.pacific_emis.surveys.wash_core.data.persistence.entity.RoomPhoto;

public class RelativeRoomAnswer {

    @Embedded
    public RoomAnswer answer;

    @Relation(parentColumn = "uid", entityColumn = "answer_id")
    public List<RoomPhoto> photos;

    public MutableAnswer toMutable() {
        MutableAnswer mutableAnswer = new MutableAnswer(answer);
        mutableAnswer.setPhotos(photos.stream().map(MutablePhoto::new).collect(Collectors.toList()));
        return mutableAnswer;
    }

}
