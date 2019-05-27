package fm.doe.national.report_core.ui.recommendations;

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

import fm.doe.national.report_core.R;
import fm.doe.national.report_core.domain.ReportInteractor;
import fm.doe.national.report_core.model.recommendations.Recommendation;
import fm.doe.national.report_core.ui.base.BaseReportFragment;

public class RecommendationsFragment extends BaseReportFragment implements RecommendationsView {

    private final ReportInteractor interactor;

    @InjectPresenter
    RecommendationsPresenter presenter;

    private RecyclerView recommendationsRecyclerView;

    private RecommendationsAdapter recommendationsAdapter = new RecommendationsAdapter();

    public RecommendationsFragment(ReportInteractor interactor) {
        this.interactor = interactor;
    }

    @ProvidePresenter
    public RecommendationsPresenter providePresenter() {
        return new RecommendationsPresenter(interactor);
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
        recommendationsRecyclerView.setAdapter(recommendationsAdapter);
    }

    private void bindViews(View view) {
        recommendationsRecyclerView = view.findViewById(R.id.recyclerview_recommendations);
    }

    @Override
    public void setRecommendationsLoadingVisibility(boolean visible) {
        // nothing
    }

    @Override
    public void setRecommendations(List<Recommendation> recommendations) {
        recommendationsAdapter.setItems(recommendations);
    }

    @Override
    public Text getTabName() {
        return Text.from(R.string.label_recommendations);
    }
}
