package fm.doe.national.accreditation.ui.navigation.concrete;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.accreditation.R;
import fm.doe.national.accreditation.ui.observation_info.ObservationInfoFragment;
import fm.doe.national.core.ui.screens.base.BaseFragment;
import fm.doe.national.survey_core.navigation.PrefixedBuildableNavigationItem;

public class ClassroomObservationInfoNavigationItem extends PrefixedBuildableNavigationItem {

    private final long categoryId;

    public ClassroomObservationInfoNavigationItem(long id, long categoryId) {
        super(
                id,
                Text.from(R.string.label_classroom_observation_info_suffix),
                Text.from(R.string.label_classroom_observation_info_prefix)
        );
        this.categoryId = categoryId;
    }

    @NonNull
    @Override
    public BaseFragment buildFragment() {
        return ObservationInfoFragment.create(categoryId);
    }
}
