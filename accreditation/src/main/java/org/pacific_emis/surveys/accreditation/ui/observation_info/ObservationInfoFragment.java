package org.pacific_emis.surveys.accreditation.ui.observation_info;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

import org.jetbrains.annotations.NotNull;
import org.pacific_emis.surveys.accreditation.R;
import org.pacific_emis.surveys.accreditation_core.di.AccreditationCoreComponentInjector;
import org.pacific_emis.surveys.core.data.model.Subject;
import org.pacific_emis.surveys.core.data.model.Teacher;
import org.pacific_emis.surveys.core.ui.screens.base.BaseFragment;
import org.pacific_emis.surveys.core.ui.views.BottomNavigatorView;
import org.pacific_emis.surveys.core.ui.views.InputFieldLayout;
import org.pacific_emis.surveys.core.utils.ViewUtils;
import org.pacific_emis.surveys.remote_storage.di.RemoteStorageComponentInjector;
import org.pacific_emis.surveys.survey_core.di.SurveyCoreComponentInjector;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ObservationInfoFragment extends BaseFragment implements
        ObservationInfoView,
        BottomNavigatorView.Listener,
        InputFieldLayout.OnDonePressedListener {

    private static final String ARG_CATEGORY_ID = "ARG_CATEGORY_ID";

    @SuppressLint("ConstantLocale")
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.US);

    @InjectPresenter
    ObservationInfoPresenter presenter;

    private AutoCompleteTextView teacherNameAutoComplete;
    private TextView gradeTextView;
    private InputFieldLayout totalStudentsInputFieldLayout;
    private AutoCompleteTextView subjectAutoComplete;
    private TextView dateTimeTextView;
    private BottomNavigatorView bottomNavigatorView;
    private View rootView;
    private ArrayAdapter<String> teachersAutoAdapter;
    private final List<String> teacherNames = new ArrayList<String>();
    private ArrayAdapter<String> subjectsAutoAdapter;
    private final List<String> subjectTitles = new ArrayList<String>();

    @Nullable
    private Dialog selectorDialog;

    public static ObservationInfoFragment create(long categoryId) {
        ObservationInfoFragment fragment = new ObservationInfoFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CATEGORY_ID, categoryId);
        fragment.setArguments(args);
        return fragment;
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
        teachersAutoAdapter = initializeAutocompleteAdapter(teacherNames);
        subjectsAutoAdapter = initializeAutocompleteAdapter(subjectTitles);
        return inflater.inflate(R.layout.fragment_observation_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        setupUserInteractions();
    }

    private void bindViews(@NonNull View view) {
        teacherNameAutoComplete = view.findViewById(R.id.input_auto_teacher_name);
        subjectAutoComplete = view.findViewById(R.id.input_auto_subject);
        gradeTextView = view.findViewById(R.id.textview_grade);
        totalStudentsInputFieldLayout = view.findViewById(R.id.inputfieldlayout_students);
        dateTimeTextView = view.findViewById(R.id.textview_date_time);
        bottomNavigatorView = view.findViewById(R.id.bottomnavigatorview);
        rootView = view.findViewById(R.id.layout_root);
    }

    private void setupUserInteractions() {
        bottomNavigatorView.setListener(this);
        teacherNameAutoComplete.setAdapter(teachersAutoAdapter);
        teacherNameAutoComplete.setOnItemClickListener(new TeacherDropdownItemClickListener());
        subjectAutoComplete.setAdapter(teachersAutoAdapter);
        subjectAutoComplete.setOnItemClickListener(new SubjectDropdownItemClickListener());
        totalStudentsInputFieldLayout.setOnDonePressedListener(this);
        gradeTextView.setOnClickListener((view) -> presenter.onGradePressed());
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
        teacherNames.add(teacherName);
        teachersAutoAdapter.notifyDataSetChanged();
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
        totalStudentsInputFieldLayout.setStartingText(totalStudentsPresent == null ? null : totalStudentsPresent.toString());
    }

    @Override
    public void setSubject(@Nullable String subject) {
        subjectTitles.add(subject);
        subjectsAutoAdapter.notifyDataSetChanged();
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

    @Override
    public void showGradeSelector(@NonNull List<String> possibleGrades, @NonNull OnGradePickedListener listener) {
        selectorDialog = new BottomSheetDialog(requireContext());
        final View sheetView = getLayoutInflater().inflate(R.layout.sheet_picker, null);
        final SheetPickerAdapter adapter = new SheetPickerAdapter(possibleGrades, item -> {
            listener.onGradePicked(item);
            safeDismiss(selectorDialog);
        });
        adapter.setItems(possibleGrades);
        final RecyclerView recyclerView = sheetView.findViewById(R.id.recyclerview);
        recyclerView.setAdapter(adapter);
        final TextView titleTextView = sheetView.findViewById(R.id.textview_title);
        titleTextView.setText(R.string.label_select_grade);
        selectorDialog.setContentView(sheetView);
        selectorDialog.show();
    }

    @Override
    public void addTeachersToAutocompleteField(@NonNull @NotNull List<Teacher> teachers) {
        teacherNames.clear();
        for (Teacher t : teachers) {
            teacherNames.add(t.getName());
        }
        teachersAutoAdapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_dropdown_item_1line,
                teacherNames
        );
        teacherNameAutoComplete.setAdapter(teachersAutoAdapter);
    }

    @Override
    public void addSubjectsToAutocompleteField(@NonNull @NotNull List<Subject> subjects) {
        subjectTitles.clear();
        for (Subject s : subjects) {
            subjectTitles.add(s.getName());
        }
        subjectsAutoAdapter.notifyDataSetChanged();
        subjectsAutoAdapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_dropdown_item_1line,
                subjectTitles
        );
        subjectAutoComplete.setAdapter(subjectsAutoAdapter);
    }

    @Override
    public void onDonePressed(View view, @Nullable String content) {
        if (view.getId() == R.id.inputfieldlayout_students) {
            ViewUtils.hideKeyboardAndClearFocus(totalStudentsInputFieldLayout, rootView);
            if (TextUtils.isEmpty(content)) {
                presenter.onTotalStudentsChanged(null);
                return;
            }
            try {
                presenter.onTotalStudentsChanged(Integer.parseInt(content));
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
    }

    class TeacherDropdownItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ViewUtils.hideKeyboardAndClearFocus(teacherNameAutoComplete, rootView);
            presenter.onTeacherNameChanged((String) teachersAutoAdapter.getItem(position));
        }
    }

    class SubjectDropdownItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ViewUtils.hideKeyboardAndClearFocus(subjectAutoComplete, rootView);
            presenter.onSubjectChanged((String) subjectsAutoAdapter.getItem(position));
        }
    }

    private ArrayAdapter<String> initializeAutocompleteAdapter(List<String> list) {
        return new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, list);
    }
}