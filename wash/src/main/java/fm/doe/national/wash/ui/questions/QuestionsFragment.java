package fm.doe.national.wash.ui.questions;


import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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

    private final QuestionsAdapter questionsAdapter = new QuestionsAdapter(this);
    private RecyclerView recyclerView;
    private BottomNavigatorView bottomNavigatorView;

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
                QuestionType.TERNARY
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
    public void navigateToPhotos(long groupId, long sbuGroupId, long questionId) {

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

    //    @Override
    public void onCommentSubmit(String comment) {
        presenter.onCommentEdit(comment);
    }
}
