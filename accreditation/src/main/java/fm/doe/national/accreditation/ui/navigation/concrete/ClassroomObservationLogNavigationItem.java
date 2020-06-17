package fm.doe.national.accreditation.ui.navigation.concrete;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.accreditation.R;
import fm.doe.national.accreditation.ui.observation_log.ObservationLogFragment;
import fm.doe.national.core.ui.screens.base.BaseFragment;
import fm.doe.national.survey_core.navigation.PrefixedBuildableNavigationItem;

public class ClassroomObservationLogNavigationItem extends PrefixedBuildableNavigationItem {

    private final long categoryId;

    public ClassroomObservationLogNavigationItem(long id, long categoryId) {
        super(
                id,
                Text.from(R.string.label_classroom_observation_log_suffix),
                Text.from(R.string.label_classroom_observation_log_prefix)
        );
        this.categoryId = categoryId;
    }

    @NonNull
    @Override
    public BaseFragment buildFragment() {
        return ObservationLogFragment.create(categoryId);
    }
}
