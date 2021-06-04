package org.pacific_emis.surveys.wash_core.data.model.mutable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.pacific_emis.surveys.core.data.model.ConflictResolveStrategy;
import org.pacific_emis.surveys.core.data.model.mutable.BaseMutableEntity;
import org.pacific_emis.surveys.core.data.model.mutable.MutableProgress;
import org.pacific_emis.surveys.core.utils.CollectionUtils;
import org.pacific_emis.surveys.wash_core.data.model.Group;
import org.pacific_emis.surveys.wash_core.data.model.SubGroup;

public class MutableGroup extends BaseMutableEntity implements Group {

    @NonNull
    private String title;

    @NonNull
    private String prefix;

    @Nullable
    private List<MutableSubGroup> subGroups;

    @NonNull
    private MutableProgress progress;

    public MutableGroup(Group other) {
        this(other.getTitle(), other.getPrefix());

        this.id = other.getId();

        if (other.getSubGroups() != null) {
            this.subGroups = other.getSubGroups().stream().map(MutableSubGroup::new).collect(Collectors.toList());
        }
    }

    public MutableGroup(@NonNull String title, @NonNull String prefix) {
        this.title = title;
        this.prefix = prefix;
        this.progress = MutableProgress.createEmptyProgress();
    }

    @NonNull
    @Override
    public String getTitle() {
        return title;
    }

    @NonNull
    @Override
    public String getPrefix() {
        return prefix;
    }

    @Nullable
    @Override
    public List<MutableSubGroup> getSubGroups() {
        return subGroups;
    }

    @NonNull
    @Override
    public MutableProgress getProgress() {
        return progress;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public void setPrefix(@NonNull String prefix) {
        this.prefix = prefix;
    }

    public void setSubGroups(@Nullable List<MutableSubGroup> subGroups) {
        this.subGroups = subGroups;
    }

    public void setProgress(@NonNull MutableProgress progress) {
        this.progress = progress;
    }

    public List<MutableAnswer> merge(Group other, ConflictResolveStrategy strategy) {
        List<? extends SubGroup> externalSubGroups = other.getSubGroups();
        List<MutableAnswer> changedAnswers = new ArrayList<>();

        if (!CollectionUtils.isEmpty(externalSubGroups)) {
            for (SubGroup subGroup : externalSubGroups) {
                for (MutableSubGroup mutableSubGroup : getSubGroups()) {
                    if (mutableSubGroup.getPrefix().equals(subGroup.getPrefix())) {
                        changedAnswers.addAll(mutableSubGroup.merge(subGroup, strategy));
                        break;
                    }
                }
            }
        }

        return changedAnswers;
    }
}
