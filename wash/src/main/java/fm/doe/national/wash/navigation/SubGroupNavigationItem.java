package fm.doe.national.wash.navigation;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.core.data.model.Progress;
import fm.doe.national.core.data.model.mutable.MutableProgress;
import fm.doe.national.core.ui.screens.base.BaseFragment;
import fm.doe.national.survey_core.navigation.ProgressablePrefixedBuildableNavigationItem;
import fm.doe.national.wash.ui.questions.QuestionsFragment;
import fm.doe.national.wash_core.data.model.Group;
import fm.doe.national.wash_core.data.model.SubGroup;
import fm.doe.national.wash_core.data.model.mutable.MutableSubGroup;

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
