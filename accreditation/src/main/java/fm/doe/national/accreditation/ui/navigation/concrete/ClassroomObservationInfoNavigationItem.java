package fm.doe.national.accreditation.ui.navigation.concrete;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.accreditation.R;
import fm.doe.national.accreditation.ui.observation_info.ObservationInfoFragment;
import fm.doe.national.core.ui.screens.base.BaseFragment;
import fm.doe.national.survey_core.navigation.BuildableNavigationItem;

public class ClassroomObservationInfoNavigationItem extends BuildableNavigationItem {

    private final long categoryId;

    public ClassroomObservationInfoNavigationItem(long categoryId) {
        super(Text.from(R.string.label_classroom_observation_info), categoryId);
        this.categoryId = categoryId;
    }

    @NonNull
    @Override
    public BaseFragment buildFragment() {
        return ObservationInfoFragment.create(categoryId);
    }
}
