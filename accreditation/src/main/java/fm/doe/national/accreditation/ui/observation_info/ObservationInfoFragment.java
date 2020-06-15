package fm.doe.national.accreditation.ui.observation_info;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

import java.util.Date;

import fm.doe.national.accreditation.R;
import fm.doe.national.accreditation_core.di.AccreditationCoreComponentInjector;
import fm.doe.national.core.ui.screens.base.BaseFragment;
import fm.doe.national.remote_storage.di.RemoteStorageComponentInjector;
import fm.doe.national.survey_core.di.SurveyCoreComponentInjector;

public class ObservationInfoFragment extends BaseFragment implements ObservationInfoView {

    private static final String ARG_CATEGORY_ID = "ARG_CATEGORY_ID";

    @InjectPresenter
    ObservationInfoPresenter presenter;

    @ProvidePresenter
    ObservationInfoPresenter providePresenter() {
        Application application = requireActivity().getApplication();
        Bundle args = requireArguments();
        return new ObservationInfoPresenter(
                RemoteStorageComponentInjector.getComponent(application),
                SurveyCoreComponentInjector.getComponent(application),
                AccreditationCoreComponentInjector.getComponent(application),
                args.getLong(ARG_CATEGORY_ID)
        );
    }

    public static ObservationInfoFragment create(long categoryId) {
        ObservationInfoFragment fragment = new ObservationInfoFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CATEGORY_ID, categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    public ObservationInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_observation_info, container, false);
    }

    @Override
    public void setPrevButtonVisible(boolean isVisible) {

    }

    @Override
    public void setNextButtonEnabled(boolean isEnabled) {

    }

    @Override
    public void setNextButtonText(Text text) {

    }

    @Override
    public void setTeacherName(@Nullable String teacherName) {

    }

    @Override
    public void setGrade(@Nullable String grade) {

    }

    @Override
    public void setTotalStudentsPresent(@Nullable Integer totalStudentsPresent) {

    }

    @Override
    public void setSubject(@Nullable String subject) {

    }

    @Override
    public void setDate(@Nullable Date date) {

    }
}