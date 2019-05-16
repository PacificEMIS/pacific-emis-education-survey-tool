package fm.doe.national.ui.screens.report.recommendations;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.ui.screens.report.BaseReportFragment;
import fm.doe.national.data.model.recommendations.Recommendation;

public class RecommendationsFragment extends BaseReportFragment implements RecommendationsView {

    @InjectPresenter
    RecommendationsPresenter presenter;

    @BindView(R.id.recyclerview_recommendations)
    RecyclerView recommendationsRecyclerView;

    private RecommendationsAdapter recommendationsAdapter = new RecommendationsAdapter();

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
        recommendationsRecyclerView.setAdapter(recommendationsAdapter);
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
