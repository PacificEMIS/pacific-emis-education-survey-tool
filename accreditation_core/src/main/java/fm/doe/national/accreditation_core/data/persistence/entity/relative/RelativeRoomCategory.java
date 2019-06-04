package fm.doe.national.accreditation_core.data.persistence.entity.relative;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.accreditation_core.data.model.mutable.MutableCategory;
import fm.doe.national.accreditation_core.data.persistence.entity.RoomCategory;
import fm.doe.national.accreditation_core.data.persistence.entity.RoomStandard;

public class RelativeRoomCategory {

    @Embedded
    public RoomCategory category;

    @Relation(parentColumn = "uid", entityColumn = "category_id", entity = RoomStandard.class)
    public List<RelativeRoomStandard> standards;

    public MutableCategory toMutableCategory() {
        MutableCategory mutableCategory = new MutableCategory(category);
        mutableCategory.setStandards(standards.stream().map(RelativeRoomStandard::toMutableStandard).collect(Collectors.toList()));
        return mutableCategory;
    }

}
