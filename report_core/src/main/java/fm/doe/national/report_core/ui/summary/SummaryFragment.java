package fm.doe.national.report_core.ui.summary;

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
import fm.doe.national.report_core.model.SummaryViewData;
import fm.doe.national.report_core.ui.base.BaseReportFragment;
import fm.doe.national.report_core.ui.level_legend.LevelLegendView;

public class SummaryFragment extends BaseReportFragment implements SummaryView {

    private final SummaryStandardAdapter adapter = new SummaryStandardAdapter();
    private final ReportInteractor interactor;

    @InjectPresenter
    SummaryPresenter presenter;

    private RecyclerView summaryRecyclerView;
    private LevelLegendView levelLegendView;

    public SummaryFragment(ReportInteractor interactor) {
        this.interactor = interactor;
    }

    @ProvidePresenter
    public SummaryPresenter providePresenter() {
        return new SummaryPresenter(interactor);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        summaryRecyclerView.setAdapter(adapter);
    }

    private void bindViews(View view) {
        summaryRecyclerView = view.findViewById(R.id.recyclerview_summary);
        levelLegendView = view.findViewById(R.id.summaryheaderview);
    }

    @Override
    public void setLoadingVisibility(boolean visible) {
        // nothing
    }

    @Override
    public void setSummaryData(@NonNull List<SummaryViewData> data) {
        adapter.setItems(data);
    }

    @Override
    public Text getTabName() {
        return Text.from(R.string.summary);
    }

    @Override
    public void setHeaderItem(LevelLegendView.Item item) {
        levelLegendView.setItem(item);
    }
}
