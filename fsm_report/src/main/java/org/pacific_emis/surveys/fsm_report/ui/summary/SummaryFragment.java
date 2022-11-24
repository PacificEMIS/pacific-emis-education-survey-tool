package org.pacific_emis.surveys.fsm_report.ui.summary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;


import org.pacific_emis.surveys.fsm_report.R;
import org.pacific_emis.surveys.report_core.domain.ReportInteractor;
import org.pacific_emis.surveys.report_core.model.SummaryViewData;
import org.pacific_emis.surveys.report_core.ui.level_legend.LevelLegendView;
import org.pacific_emis.surveys.report_core.ui.summary.BaseSummaryFragment;

import java.util.List;

public class SummaryFragment extends BaseSummaryFragment implements SummaryView {

    private final SummaryStandardAdapter adapter = new SummaryStandardAdapter();
    private final ReportInteractor interactor;

    @InjectPresenter
    SummaryPresenter presenter;

    private RecyclerView summaryRecyclerView;
    private LevelLegendView levelLegendView;
    private View progressView;

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
        return inflater.inflate(R.layout.fragment_fsm_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        summaryRecyclerView.setAdapter(adapter);
    }

    private void bindViews(View view) {
        summaryRecyclerView = view.findViewById(R.id.recyclerview_summary);
        levelLegendView = view.findViewById(R.id.levellegendview);
        progressView = view.findViewById(R.id.progressbar);
    }

    @Override
    public void setSummaryData(@NonNull List<SummaryViewData> data) {
        adapter.setItems(data);
        summaryRecyclerView.setVisibility(View.VISIBLE);
        progressView.setVisibility(View.GONE);
    }

    @Override
    public void setHeaderItem(LevelLegendView.Item item) {
        levelLegendView.setItem(item);
    }
}
