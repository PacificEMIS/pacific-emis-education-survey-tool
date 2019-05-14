package fm.doe.national.data.persistence.entity.relative;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import fm.doe.national.data.persistence.entity.RoomAnswer;
import fm.doe.national.data.persistence.entity.RoomPhoto;

public class RelativeRoomAnswer {

    @Embedded
    public RoomAnswer answer;

    @Relation(parentColumn = "uid", entityColumn = "answer_id")
    public List<RoomPhoto> photos;

}
