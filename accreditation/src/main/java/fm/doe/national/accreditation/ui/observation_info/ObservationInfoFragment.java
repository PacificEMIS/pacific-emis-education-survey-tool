package fm.doe.national.accreditation.ui.observation_info;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import fm.doe.national.accreditation.R;
import fm.doe.national.accreditation_core.di.AccreditationCoreComponentInjector;
import fm.doe.national.core.ui.screens.base.BaseFragment;
import fm.doe.national.core.ui.views.BottomNavigatorView;
import fm.doe.national.core.utils.TextWatcherAdapter;
import fm.doe.national.remote_storage.di.RemoteStorageComponentInjector;
import fm.doe.national.survey_core.di.SurveyCoreComponentInjector;

public class ObservationInfoFragment extends BaseFragment implements
        ObservationInfoView,
        BottomNavigatorView.Listener {

    private static final String ARG_CATEGORY_ID = "ARG_CATEGORY_ID";

    @SuppressLint("ConstantLocale")
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.US);

    @InjectPresenter
    ObservationInfoPresenter presenter;

    private EditText teacherNameEditText;
    private TextView gradeTextView;
    private EditText totalStudentsEditText;
    private EditText subjectEditText;
    private TextView dateTimeTextView;
    private BottomNavigatorView bottomNavigatorView;

    @Nullable
    private Dialog selectorDialog;

    private TextWatcher teacherNameTextWatcher = new TextWatcherAdapter() {
        @Override
        public void afterTextChanged(Editable s) {
            presenter.onTeacherNameChanged(s.toString());
        }
    };

    private TextWatcher totalStudentsTextWatcher = new TextWatcherAdapter() {
        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                presenter.onTotalStudentsChanged(null);
                return;
            }
            try {
                presenter.onTotalStudentsChanged(Integer.parseInt(s.toString()));
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
    };

    private TextWatcher subjectTextWatcher = new TextWatcherAdapter() {
        @Override
        public void afterTextChanged(Editable s) {
            presenter.onSubjectChanged(s.toString());
        }
    };

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_observation_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        setupUserInteractions();
    }

    private void bindViews(@NonNull View view) {
        teacherNameEditText = view.findViewById(R.id.textinputedittext_teacher_name);
        gradeTextView = view.findViewById(R.id.textview_grade);
        totalStudentsEditText = view.findViewById(R.id.textinputedittext_students);
        subjectEditText = view.findViewById(R.id.textinputedittext_subject);
        dateTimeTextView = view.findViewById(R.id.textview_date_time);
        bottomNavigatorView = view.findViewById(R.id.bottomnavigatorview);
    }

    private void setupUserInteractions() {
        bottomNavigatorView.setListener(this);
        teacherNameEditText.addTextChangedListener(teacherNameTextWatcher);
        totalStudentsEditText.addTextChangedListener(totalStudentsTextWatcher);
        subjectEditText.addTextChangedListener(subjectTextWatcher);
        gradeTextView.setOnClickListener((view) -> showGradeSelector());
        dateTimeTextView.setOnClickListener((view) -> presenter.onDateTimePressed());
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
    public void setTeacherName(@Nullable String teacherName) {
        teacherNameEditText.setText(teacherName);
    }

    @Override
    public void setGrade(@Nullable String grade) {
        if (grade == null) {
            gradeTextView.setText(R.string.label_observation_info_please_select_grade);
        } else {
            gradeTextView.setText(grade);
        }
    }

    @Override
    public void setTotalStudentsPresent(@Nullable Integer totalStudentsPresent) {
        totalStudentsEditText.setText(totalStudentsPresent == null ? null : totalStudentsPresent.toString());
    }

    @Override
    public void setSubject(@Nullable String subject) {
        subjectEditText.setText(subject);
    }

    @Override
    public void setDate(@NonNull Date date) {
        dateTimeTextView.setText(DATE_FORMAT.format(date));
    }

    @Override
    public void onPrevPressed() {
        presenter.onPrevPressed();
    }

    @Override
    public void onNextPressed() {
        presenter.onNextPressed();
    }

    private void showGradeSelector() {
//        selectorDialog = new BottomSheetDialog(requireContext());
//        View sheetView = getLayoutInflater().inflate(R.layout.sheet_operating_mode, null);
//        View prodItemView = sheetView.findViewById(R.id.textview_prod);
//        View devItemView = sheetView.findViewById(R.id.textview_dev);
//        TextView titleTextView = sheetView.findViewById(R.id.textview_title);
//        titleTextView.setText(R.string.title_choose_op_mode);
//        selectorDialog.setContentView(sheetView);
//        selectorDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        safeDismiss(selectorDialog);
    }

    private void safeDismiss(@Nullable Dialog dialog) {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void showDateTimePicker(@NonNull Date sourceDate, @NonNull OnDateTimePickedListener listener) {
        final Calendar resultCalendar = Calendar.getInstance();
        resultCalendar.clear();
        final Calendar sourceCalendar = Calendar.getInstance();
        sourceCalendar.setTime(sourceDate);
        new DatePickerDialog(
                requireContext(),
                (datePicker, year, monthOfYear, dayOfMonth) -> {
                    resultCalendar.set(year, monthOfYear, dayOfMonth);
                    new TimePickerDialog(
                            requireContext(),
                            (timePicker, hourOfDay, minute) -> {
                                resultCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                resultCalendar.set(Calendar.MINUTE, minute);
                                listener.onDateTimePicked(resultCalendar.getTime());
                            },
                            sourceCalendar.get(Calendar.HOUR_OF_DAY),
                            sourceCalendar.get(Calendar.MINUTE),
                            false
                    ).show();
                },
                sourceCalendar.get(Calendar.YEAR),
                sourceCalendar.get(Calendar.MONTH),
                sourceCalendar.get(Calendar.DATE)
        ).show();
    }
}