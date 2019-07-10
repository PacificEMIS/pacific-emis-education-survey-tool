package fm.doe.national.accreditation.ui.questions;


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

import java.util.List;

import fm.doe.national.accreditation.R;
import fm.doe.national.accreditation.ui.photos.AccreditationPhotosActivity;
import fm.doe.national.accreditation_core.data.model.SubCriteria;
import fm.doe.national.accreditation_core.di.AccreditationCoreComponentInjector;
import fm.doe.national.core.ui.screens.base.BaseFragment;
import fm.doe.national.core.ui.views.BottomNavigatorView;
import fm.doe.national.remote_storage.di.RemoteStorageComponentInjector;
import fm.doe.national.survey_core.di.SurveyCoreComponentInjector;

public class QuestionsFragment extends BaseFragment implements
        QuestionsView,
        QuestionsAdapter.QuestionsListener,
        CommentDialogFragment.OnCommentSubmitListener,
        BottomNavigatorView.Listener {

    private static final String ARG_CATEGORY_ID = "ARG_CATEGORY_ID";
    private static final String ARG_STANDARD_ID = "ARG_STANDARD_ID";
    private static final String TAG_DIALOG = "TAG_DIALOG";

    private RecyclerView recyclerView;
    private BottomNavigatorView bottomNavigatorView;
    private final QuestionsAdapter questionsAdapter = new QuestionsAdapter(this);

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
    public void onPhotoPressed(Question question) {
        presenter.onPhotosPressed(question);
    }

    @Override
    public void onCommentPressed(Question question) {
        presenter.onCommentPressed(question);
    }

    @Override
    public void onAnswerStateChanged(Question question) {
        presenter.onAnswerChanged(question);
    }

    @Override
    public void showCommentEditor(SubCriteria subCriteria) {
        CommentDialogFragment dialog = CommentDialogFragment.create(subCriteria);
        dialog.setListener(this);
        dialog.show(getChildFragmentManager(), TAG_DIALOG);
    }

    @Override
    public void onCommentSubmit(String comment) {
        presenter.onCommentEdit(comment);
    }

    @Override
    public void navigateToPhotos() {
        startActivity(AccreditationPhotosActivity.createIntent(getContext()));
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
}
