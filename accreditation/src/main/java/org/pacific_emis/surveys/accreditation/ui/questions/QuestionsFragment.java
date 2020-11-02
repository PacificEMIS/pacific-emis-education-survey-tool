package org.pacific_emis.surveys.accreditation.ui.questions;


import android.app.Application;
import android.content.Intent;
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

import java.util.List;

import org.pacific_emis.surveys.accreditation.R;
import org.pacific_emis.surveys.accreditation.ui.photos.AccreditationPhotosActivity;
import org.pacific_emis.surveys.accreditation_core.data.model.SubCriteria;
import org.pacific_emis.surveys.accreditation_core.di.AccreditationCoreComponentInjector;
import org.pacific_emis.surveys.core.data.model.Answerable;
import org.pacific_emis.surveys.core.ui.screens.base.BaseFragment;
import org.pacific_emis.surveys.core.ui.views.BottomNavigatorView;
import org.pacific_emis.surveys.remote_storage.di.RemoteStorageComponentInjector;
import org.pacific_emis.surveys.survey_core.di.SurveyCoreComponentInjector;
import org.pacific_emis.surveys.survey_core.ui.custom_views.CommentDialogFragment;
import org.pacific_emis.surveys.survey_core.ui.survey.BaseQuestionsAdapter;

public class QuestionsFragment extends BaseFragment implements
        QuestionsView,
        QuestionsAdapter.QuestionsListener,
        CommentDialogFragment.OnCommentSubmitListener,
        BottomNavigatorView.Listener, BaseQuestionsAdapter.Listener {

    private static final String ARG_CATEGORY_ID = "ARG_CATEGORY_ID";
    private static final String ARG_STANDARD_ID = "ARG_STANDARD_ID";
    private static final String TAG_DIALOG = "TAG_DIALOG";
    private static final int REQUEST_CODE_PHOTOS = 999;

    private RecyclerView recyclerView;
    private BottomNavigatorView bottomNavigatorView;
    private final QuestionsAdapter questionsAdapter = new QuestionsAdapter(this, this);

    @InjectPresenter
    QuestionsPresenter presenter;

    @ProvidePresenter
    QuestionsPresenter providePresenter() {
        Application application = getActivity().getApplication();
        Bundle args = getArguments();
        return new QuestionsPresenter(
                RemoteStorageComponentInjector.getComponent(application),
                SurveyCoreComponentInjector.getComponent(application),
                AccreditationCoreComponentInjector.getComponent(application),
                args.getLong(ARG_CATEGORY_ID),
                args.getLong(ARG_STANDARD_ID)
        );
    }

    public static QuestionsFragment create(long categoryId, long standardId) {
        QuestionsFragment fragment = new QuestionsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CATEGORY_ID, categoryId);
        args.putLong(ARG_STANDARD_ID, standardId);
        fragment.setArguments(args);
        return fragment;
    }

    public QuestionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
    public void setQuestions(List<Question> questions) {
        questionsAdapter.setItems(questions);
    }

    @Override
    public void onAnswerStateChanged(Question question) {
        presenter.onAnswerChanged(question);
    }

    @Override
    public void showCommentEditor(SubCriteria subCriteria) {
        CommentDialogFragment dialog = CommentDialogFragment.create(new CommentDialogFragment.ViewData(
                getString(R.string.format_subcriteria, subCriteria.getSuffix()),
                subCriteria.getTitle(),
                subCriteria.getInterviewQuestions(),
                subCriteria.getAnswer().getComment()
        ));
        dialog.setListener(this);
        dialog.show(getChildFragmentManager(), TAG_DIALOG);
    }

    @Override
    public void onCommentSubmit(String comment) {
        presenter.onCommentEdit(comment);
    }

    @Override
    public void navigateToPhotos() {
        startActivityForResult(AccreditationPhotosActivity.createIntent(getContext()), REQUEST_CODE_PHOTOS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_PHOTOS:
                presenter.onReturnFromPhotos();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
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
    public void setHintTextVisible(boolean isVisible) {
        bottomNavigatorView.setHintTextVisible(isVisible);
    }

    @Override
    public void onCommentPressed(Answerable item, int position) {
        presenter.onCommentPressed((Question) item, position);
    }

    @Override
    public void onPhotosPressed(Answerable item, int position) {
        presenter.onPhotosPressed((Question) item, position);
    }

    @Override
    public void onDeleteCommentPressed(Answerable item, int position) {
        presenter.onCommentDeletePressed((Question) item, position);
    }

    @Override
    public void refreshQuestionAtPosition(int selectedQuestionPosition) {
        questionsAdapter.notifyItemChanged(selectedQuestionPosition);
    }
}
