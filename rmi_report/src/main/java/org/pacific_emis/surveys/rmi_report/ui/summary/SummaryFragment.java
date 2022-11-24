package org.pacific_emis.surveys.rmi_report.ui.summary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;


import org.pacific_emis.surveys.report_core.domain.ReportInteractor;
import org.pacific_emis.surveys.report_core.model.SummaryViewData;
import org.pacific_emis.surveys.report_core.ui.summary.BaseSummaryFragment;
import org.pacific_emis.surveys.rmi_report.R;

import java.util.List;

public class SummaryFragment extends BaseSummaryFragment implements SummaryView {

    private final SummaryStandardAdapter adapter = new SummaryStandardAdapter();
    private final ReportInteractor interactor;

    @InjectPresenter
    SummaryPresenter presenter;

    private RecyclerView summaryRecyclerView;
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
        return inflater.inflate(R.layout.fragment_rmi_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        summaryRecyclerView.setAdapter(adapter);
    }

    private void bindViews(View view) {
        summaryRecyclerView = view.findViewById(R.id.recyclerview_summary);
        progressView = view.findViewById(R.id.progressbar);
    }

    @Override
    public void setSummaryData(@NonNull List<SummaryViewData> data) {
        adapter.setItems(data);
        summaryRecyclerView.setVisibility(View.VISIBLE);
        progressView.setVisibility(View.GONE);
    }

}
