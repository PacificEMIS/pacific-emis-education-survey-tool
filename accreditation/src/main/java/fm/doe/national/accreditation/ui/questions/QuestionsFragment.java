package fm.doe.national.accreditation.ui.questions;


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
import fm.doe.national.core.di.ComponentInjector;
import fm.doe.national.core.ui.screens.base.BaseFragment;

public class QuestionsFragment extends BaseFragment implements QuestionsView, QuestionsAdapter.QuestionsListener {

    private static final String ARG_CATEGORY_ID = "ARG_CATEGORY_ID";
    private static final String ARG_STANDARD_ID = "ARG_STANDARD_ID";

    private RecyclerView recyclerView;
    private final QuestionsAdapter questionsAdapter = new QuestionsAdapter(this);

    @InjectPresenter
    QuestionsPresenter presenter;

    @ProvidePresenter
    QuestionsPresenter providePresenter() {
        return new QuestionsPresenter(
                ComponentInjector.getComponent(getActivity().getApplication()),
                getArguments().getLong(ARG_CATEGORY_ID),
                getArguments().getLong(ARG_STANDARD_ID)
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_questions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        recyclerView.setAdapter(questionsAdapter);
    }

    private void bindViews(@NonNull View view) {
        recyclerView = view.findViewById(R.id.recyclerview);
    }

    @Override
    public void setQuestions(List<Question> questions) {
        questionsAdapter.setItems(questions);
    }

    @Override
    public void showCommentEditor(String comment) {
        showToast(Text.from(R.string.coming_soon));
    }

    @Override
    public void showPhotos() {
        showToast(Text.from(R.string.coming_soon));
    }

    @Override
    public void onPhotoPressed(Question question) {
        presenter.onPhotosPressed();
    }

    @Override
    public void onCommentPressed(Question question) {
        presenter.onCommentPressed(question);
    }

    @Override
    public void onAnswerStateChanged(Question question) {
        presenter.onAnswerChanged(question);
    }
}
