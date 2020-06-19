package fm.doe.national.accreditation.ui.observation_log;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

import java.util.Arrays;
import java.util.Date;

import fm.doe.national.accreditation.R;
import fm.doe.national.accreditation_core.di.AccreditationCoreComponentInjector;
import fm.doe.national.core.ui.screens.base.BaseFragment;
import fm.doe.national.core.ui.views.BottomNavigatorView;
import fm.doe.national.remote_storage.di.RemoteStorageComponentInjector;
import fm.doe.national.survey_core.di.SurveyCoreComponentInjector;

public class ObservationLogFragment extends BaseFragment implements
        ObservationLogView,
        BottomNavigatorView.Listener {

    private static final String ARG_CATEGORY_ID = "ARG_CATEGORY_ID";

    @InjectPresenter
    ObservationLogPresenter presenter;

    private final ObservationLogAdapter adapter = new ObservationLogAdapter();

    private BottomNavigatorView bottomNavigatorView;
    private RecyclerView recyclerView;


    public static ObservationLogFragment create(long categoryId) {
        ObservationLogFragment fragment = new ObservationLogFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CATEGORY_ID, categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    public ObservationLogFragment() {
        // Required empty public constructor
    }

    @ProvidePresenter
    ObservationLogPresenter providePresenter() {
        Application application = requireActivity().getApplication();
        Bundle args = requireArguments();
        return new ObservationLogPresenter(
                RemoteStorageComponentInjector.getComponent(application),
                SurveyCoreComponentInjector.getComponent(application),
                AccreditationCoreComponentInjector.getComponent(application),
                args.getLong(ARG_CATEGORY_ID)
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_observation_log, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        setupUserInteractions();
    }

    private void bindViews(@NonNull View view) {
        bottomNavigatorView = view.findViewById(R.id.bottomnavigatorview);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setAdapter(adapter);

        adapter.setItems(Arrays.asList(
                new RecordViewData(new Date(), "Action", "React"),
                new RecordViewData(new Date(), "Action2", "React2"),
                new RecordViewData(new Date(), "Action3", "React3"),
                new RecordViewData(new Date(), "Action4", "React4"),
                new RecordViewData(new Date(), "Action", "React"),
                new RecordViewData(new Date(), "Action2", "React2"),
                new RecordViewData(new Date(), "Action3", "React3"),
                new RecordViewData(new Date(), "Action4", "React4"),
                new RecordViewData(new Date(), "Action", "React"),
                new RecordViewData(new Date(), "Action2", "React2"),
                new RecordViewData(new Date(), "Action3", "React3"),
                new RecordViewData(new Date(), "Action4", "React4")
        ));
    }

    private void setupUserInteractions() {
        bottomNavigatorView.setListener(this);
    }

    @Override
    public void setPrevButtonVisible(boolean isVisible) {
        bottomNavigatorView.setPrevButtonVisible(isVisible);
    }

    @Override
    public void setNextButtonEnabled(boolean isEnabled) {
        bottomNavigatorView.setNextButtonEnabled(isEnabled);
    }

    @Override
    public void setNextButtonText(Text text) {
        bottomNavigatorView.setNextText(text.getString(requireContext()));
    }

    @Override
    public void onPrevPressed() {
        presenter.onPrevPressed();
    }

    @Override
    public void onNextPressed() {
        presenter.onNextPressed();
    }

}