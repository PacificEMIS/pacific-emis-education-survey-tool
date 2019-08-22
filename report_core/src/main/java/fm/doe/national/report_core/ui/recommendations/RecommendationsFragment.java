package fm.doe.national.report_core.ui.recommendations;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;
import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

import java.util.List;

import fm.doe.national.report_core.R;
import fm.doe.national.report_core.domain.ReportInteractor;
import fm.doe.national.report_core.model.recommendations.Recommendation;
import fm.doe.national.report_core.ui.base.BaseReportFragment;
import fm.doe.national.survey_core.di.SurveyCoreComponentInjector;

public class RecommendationsFragment extends BaseReportFragment implements
        RecommendationsView,
        BaseListAdapter.OnItemClickListener<Recommendation> {

    private final ReportInteractor interactor;

    @InjectPresenter
    RecommendationsPresenter presenter;

    private RecyclerView recommendationsRecyclerView;
    private View progressView;
    private RecommendationsAdapter recommendationsAdapter = new RecommendationsAdapter();

    public RecommendationsFragment(ReportInteractor interactor) {
        this.interactor = interactor;
    }

    @ProvidePresenter
    public RecommendationsPresenter providePresenter() {
        return new RecommendationsPresenter(interactor, SurveyCoreComponentInjector.getComponent(getActivity().getApplication()));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recommendations, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        recommendationsAdapter.setClickListener(this);
        recommendationsRecyclerView.setAdapter(recommendationsAdapter);
    }

    private void bindViews(View view) {
        recommendationsRecyclerView = view.findViewById(R.id.recyclerview_recommendations);
        progressView = view.findViewById(R.id.progressbar);
    }

    @Override
    public void setRecommendationsLoadingVisibility(boolean visible) {
        // nothing
    }

    @Override
    public void setRecommendations(List<Recommendation> recommendations) {
        recommendationsAdapter.setItems(recommendations);

        recommendationsRecyclerView.setVisibility(View.VISIBLE);
        progressView.setVisibility(View.GONE);
    }

    @Override
    public Text getTabName() {
        return Text.from(R.string.label_recommendations);
    }

    @Override
    public void onItemClick(Recommendation item) {
        presenter.onRecommendationPressed(item);
    }
}
