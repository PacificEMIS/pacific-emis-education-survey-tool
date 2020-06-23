package fm.doe.national.accreditation.ui.observation_log;

import android.app.Application;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;
import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fm.doe.national.accreditation.R;
import fm.doe.national.accreditation_core.data.model.mutable.MutableObservationLogRecord;
import fm.doe.national.accreditation_core.di.AccreditationCoreComponentInjector;
import fm.doe.national.core.ui.screens.base.BaseFragment;
import fm.doe.national.core.ui.views.BottomNavigatorView;
import fm.doe.national.remote_storage.di.RemoteStorageComponentInjector;
import fm.doe.national.survey_core.di.SurveyCoreComponentInjector;

public class ObservationLogFragment extends BaseFragment implements
        ObservationLogView,
        BottomNavigatorView.Listener, ObservationLogAdapter.Listener, View.OnClickListener {

    private static final String ARG_CATEGORY_ID = "ARG_CATEGORY_ID";

    @InjectPresenter
    ObservationLogPresenter presenter;

    private final ObservationLogAdapter adapter = new ObservationLogAdapter(new RecordsDiffCallback(), this);

    private BottomNavigatorView bottomNavigatorView;
    private Button addButton;
    private OmegaRecyclerView recyclerView;

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
        addButton = view.findViewById(R.id.button_add);
        addButton.setOnClickListener(this);
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

    @Override
    public void updateLog(List<MutableObservationLogRecord> items) {
        updateLog(items, null);
    }

    @Override
    public void updateScrollingToPosition(List<MutableObservationLogRecord> items, int position) {
        updateLog(items, () -> recyclerView.smoothScrollToPosition(position + getRecyclerItemsOffset()));
    }

    private int getRecyclerItemsOffset() {
        final int realItemsCount = adapter.getItemCount();
        final int internalItemsCount = recyclerView.getAdapter().getItemCount();
        return internalItemsCount - realItemsCount;
    }

    private void updateLog(List<MutableObservationLogRecord> items, @Nullable Runnable commitCallback) {
        addButton.setText(items.isEmpty()
                ? R.string.button_classroom_observation_log_start
                : R.string.button_classroom_observation_log_add);
        adapter.submitList(items, commitCallback);
    }

    @Override
    public void onTeacherActionChanged(int position, @Nullable String action) {
        presenter.onTeacherActionChanged(position, action);
    }

    @Override
    public void onStudentsActionChanged(int position, @Nullable String action) {
        presenter.onStudentsActionChanged(position, action);
    }

    @Override
    public void onTimePressed(int position) {
        presenter.onTimePressed(position);
    }

    @Override
    public void onDeletePressed(int position) {
        presenter.onDeletePressed(position);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_add) {
            presenter.onAddPressed();
        }
    }

    @Override
    public void showTimePicker(@NonNull Date sourceDate, @NonNull OnTimePickedListener listener) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(sourceDate);
        new TimePickerDialog(
                requireContext(),
                (timePicker, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    listener.onTimePicked(calendar.getTime());
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
        ).show();
    }
}