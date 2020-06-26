package org.pacific_emis.surveys.accreditation.ui.navigation.concrete;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Text;

import org.pacific_emis.surveys.accreditation.R;
import org.pacific_emis.surveys.accreditation.ui.observation_info.ObservationInfoFragment;
import org.pacific_emis.surveys.core.ui.screens.base.BaseFragment;
import org.pacific_emis.surveys.survey_core.navigation.PrefixedBuildableNavigationItem;

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
