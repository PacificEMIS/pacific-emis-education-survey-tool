package org.pacific_emis.surveys.wash.navigation;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Text;

import org.pacific_emis.surveys.core.data.model.Progress;
import org.pacific_emis.surveys.core.data.model.mutable.MutableProgress;
import org.pacific_emis.surveys.core.ui.screens.base.BaseFragment;
import org.pacific_emis.surveys.survey_core.navigation.ProgressablePrefixedBuildableNavigationItem;
import org.pacific_emis.surveys.wash.ui.questions.QuestionsFragment;
import org.pacific_emis.surveys.wash_core.data.model.Group;
import org.pacific_emis.surveys.wash_core.data.model.SubGroup;
import org.pacific_emis.surveys.wash_core.data.model.mutable.MutableSubGroup;

public class SubGroupNavigationItem extends ProgressablePrefixedBuildableNavigationItem {

    private final Group group;
    private final SubGroup subGroup;

    public SubGroupNavigationItem(Group group, SubGroup subGroup) {
        super(subGroup.getId(), Text.from(subGroup.getTitle()), Text.from(subGroup.getPrefix()));
        this.group = group;
        this.subGroup = subGroup;
    }

    @NonNull
    @Override
    public Progress getProgress() {
        return subGroup.getProgress();
    }

    @Override
    public void setProgress(Progress progress) {
        if (subGroup instanceof MutableSubGroup) {
            ((MutableSubGroup) subGroup).setProgress(new MutableProgress(progress.getTotal(), progress.getCompleted()));
        }
    }

    @NonNull
    @Override
    public BaseFragment buildFragment() {
        return QuestionsFragment.create(group.getId(), subGroup.getId());
    }

}
