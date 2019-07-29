package fm.doe.national.fcm_report.ui.levels;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

import fm.doe.national.core.utils.ViewUtils;
import fm.doe.national.fcm_report.R;
import fm.doe.national.fcm_report.data.model.SchoolAccreditationLevel;
import fm.doe.national.fcm_report.di.FsmReportComponentInjector;
import fm.doe.national.report_core.domain.ReportInteractor;
import fm.doe.national.report_core.domain.ReportLevel;
import fm.doe.national.report_core.ui.base.BaseReportFragment;
import fm.doe.national.report_core.ui.level_legend.LevelLegendView;

public class LevelsFragment extends BaseReportFragment implements LevelsView {

    @InjectPresenter
    LevelsPresenter presenter;

    private RecyclerView recyclerView;
    private TextView levelTextView;
    private LevelLegendView levelLegendView;
    private View progressView;

    private final EvalutaionFormsAdapter adapter = new EvalutaionFormsAdapter();

    private TextView totalObtainedScoreTextView;
    private TextView totalFinalScoreTextView;
    private TextView totalScoreTitleTextView;

    @ProvidePresenter
    LevelsPresenter providePresenter() {
        return new LevelsPresenter(FsmReportComponentInjector.getComponent(getActivity().getApplication()));
    }

    public LevelsFragment(ReportInteractor interactor) {
        // used in reflection
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fsm_levels, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        recyclerView.setAdapter(adapter);
    }

    private void bindViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerview);
        levelTextView = view.findViewById(R.id.textview_level);
        levelLegendView = view.findViewById(R.id.levellegendview);
        ViewGroup totalsViewGroup = view.findViewById(R.id.layout_total_scores);
        totalObtainedScoreTextView = totalsViewGroup.findViewById(R.id.textview_obtained_score);
        totalFinalScoreTextView = totalsViewGroup.findViewById(R.id.textview_final_score);
        totalScoreTitleTextView = totalsViewGroup.findViewById(R.id.textview_name);
        progressView = view.findViewById(R.id.progressbar);
    }

    @Override
    public Text getTabName() {
        return Text.from(R.string.title_school_accreditation_level);
    }

    @Override
    public void setLoadingVisible(boolean visible) {
        // nothing
    }

    @Override
    public void setData(SchoolAccreditationLevel data) {
        adapter.setItems(data.getForms());

        totalFinalScoreTextView.setText(String.valueOf(data.getTotalScore()));
        totalObtainedScoreTextView.setText(String.valueOf(data.getTotalObtainedScore()));
        totalScoreTitleTextView.setText(R.string.label_total_score);

        ReportLevel reportLevel = data.getReportLevel();
        reportLevel.getName().applyTo(levelTextView, null);

        ViewUtils.setTintedBackgroundDrawable(
                levelTextView,
                fm.doe.national.report_core.R.drawable.bg_level,
                reportLevel.getColorRes()
        );

        recyclerView.setVisibility(View.VISIBLE);
        progressView.setVisibility(View.GONE);
    }

    @Override
    public void setHeaderItem(LevelLegendView.Item item) {
        levelLegendView.setItem(item);
    }
}
