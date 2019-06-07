package fm.doe.national.wash.ui.questions;


import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.cloud.di.CloudComponentInjector;
import fm.doe.national.core.di.CoreComponentInjector;
import fm.doe.national.core.ui.screens.base.BaseFragment;
import fm.doe.national.core.ui.views.BottomNavigatorView;
import fm.doe.national.survey_core.di.SurveyCoreComponentInjector;
import fm.doe.national.wash.R;
import fm.doe.national.wash.ui.photos.WashPhotosActivity;
import fm.doe.national.wash_core.data.model.Location;
import fm.doe.national.wash_core.data.model.Question;
import fm.doe.national.wash_core.data.model.QuestionType;
import fm.doe.national.wash_core.data.model.mutable.MutableQuestion;
import fm.doe.national.wash_core.di.WashCoreComponentInjector;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionsFragment extends BaseFragment implements
        QuestionsView,
        BottomNavigatorView.Listener,
        QuestionsAdapter.QuestionsListener {

    private static final String ARG_GROUP_ID = "ARG_GROUP_ID";
    private static final String ARG_SUB_GROUP_ID = "ARG_SUB_GROUP_ID";
    private static final String TAG_DIALOG = "TAG_DIALOG";
    private static final int REQUEST_LOCATION_PERMISSIONS = 999;

    private final QuestionsAdapter questionsAdapter = new QuestionsAdapter(this);
    private final String locationPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    private RecyclerView recyclerView;
    private BottomNavigatorView bottomNavigatorView;
    private FusedLocationProviderClient locationProviderClient;

    private MutableQuestion stashedQuestion;
    private int stashedQuestionPosition;

    @InjectPresenter
    QuestionsPresenter presenter;

    @ProvidePresenter
    QuestionsPresenter providePresenter() {
        Application application = getActivity().getApplication();
        Bundle args = getArguments();
        return new QuestionsPresenter(
                CoreComponentInjector.getComponent(application),
                CloudComponentInjector.getComponent(application),
                SurveyCoreComponentInjector.getComponent(application),
                WashCoreComponentInjector.getComponent(application),
                args.getLong(ARG_GROUP_ID),
                args.getLong(ARG_SUB_GROUP_ID)
        );
    }

    public static QuestionsFragment create(long groupId, long subGroupId) {
        QuestionsFragment fragment = new QuestionsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_GROUP_ID, groupId);
        args.putLong(ARG_SUB_GROUP_ID, subGroupId);
        fragment.setArguments(args);
        return fragment;
    }

    public QuestionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_questions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        recyclerView.setAdapter(questionsAdapter);
        bottomNavigatorView.setListener(this);
    }

    private void bindViews(@NonNull View view) {
        recyclerView = view.findViewById(R.id.recyclerview);
        bottomNavigatorView = view.findViewById(R.id.bottomnavigatorview);
    }

    @Override
    public void setQuestions(List<MutableQuestion> questions) {
        // TODO: temp filter to prevent crashes in development
        List<QuestionType> developedTypes = Arrays.asList(
                QuestionType.BINARY,
                QuestionType.SINGLE_SELECTION,
                QuestionType.MULTI_SELECTION,
                QuestionType.TERNARY,
                QuestionType.TEXT_INPUT,
                QuestionType.NUMBER_INPUT,
                QuestionType.PHONE_INPUT,
                QuestionType.PHOTO,
                QuestionType.GEOLOCATION,
                QuestionType.COMPLEX_BINARY
        );
        questionsAdapter.setItems(
                questions.stream()
                        .filter(q -> developedTypes.contains(q.getType()))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public void showCommentEditor(Question question) {

    }

    @Override
    public void navigateToPhotos() {
        startActivity(new Intent(getActivity(), WashPhotosActivity.class));
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
        bottomNavigatorView.setNextText(text.getString(getContext()));
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
    public void onPhotoPressed(MutableQuestion question) {
        presenter.onPhotosPressed(question);
    }

    @Override
    public void onCommentPressed(MutableQuestion question) {
        presenter.onCommentPressed(question);
    }

    @Override
    public void onAnswerStateChanged(MutableQuestion question) {
        presenter.onAnswerChanged(question);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSIONS &&
                permissions.length > 0 &&
                permissions[0].equals(locationPermission) &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                stashedQuestion != null) {

            onTakeLocationPressed(stashedQuestion, stashedQuestionPosition);
        }
    }

    @Override
    public void onTakeLocationPressed(MutableQuestion question, int position) {
        Activity activity = getActivity();

        if (activity == null) {
            return;
        }

        if (activity.checkSelfPermission(locationPermission) != PackageManager.PERMISSION_GRANTED) {
            stashedQuestion = question;
            stashedQuestionPosition = position;
            requestPermissions(new String[]{locationPermission}, REQUEST_LOCATION_PERMISSIONS);
            return;
        }

        locationProviderClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    presenter.onLocationChanged(new Location(location.getLatitude(), location.getLongitude()), question);
                    questionsAdapter.notifyItemChanged(position);
                });
    }

    //    @Override
    public void onCommentSubmit(String comment) {
        presenter.onCommentEdit(comment);
    }
}
