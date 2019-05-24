package fm.doe.national.rmi_report.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

import fm.doe.national.report_core.domain.ReportInteractor;
import fm.doe.national.report_core.ui.base.BaseReportFragment;
import fm.doe.national.report_core.ui.summary_header.SummaryHeaderView;
import fm.doe.national.rmi_report.R;
import fm.doe.national.rmi_report.di.ComponentInjector;
import fm.doe.national.rmi_report.model.SchoolAccreditationTallyLevel;

public class LevelsFragment extends BaseReportFragment implements LevelsView {

    private SummaryHeaderView summaryHeaderView;
    private TextView level1TextView;
    private TextView level2TextView;
    private TextView level3TextView;
    private TextView level4TextView;
    private TextView scoreTextView;
    private TextView levelTextView;

    @InjectPresenter
    LevelsPresenter presenter;

    public LevelsFragment(ReportInteractor interactor) {
        // used in reflection
    }

    @ProvidePresenter
    LevelsPresenter providePresenter() {
        return new LevelsPresenter(ComponentInjector.getComponent(getActivity().getApplication()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rmi_levels, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
    }

    private void bindViews(View view) {
        summaryHeaderView = view.findViewById(R.id.summaryheaderview);
        level1TextView = view.findViewById(R.id.textview_tally_level_1);
        level2TextView = view.findViewById(R.id.textview_tally_level_2);
        level3TextView = view.findViewById(R.id.textview_tally_level_3);
        level4TextView = view.findViewById(R.id.textview_tally_level_4);
        scoreTextView = view.findViewById(R.id.textview_tally_score);
        levelTextView = view.findViewById(R.id.textview_tally_level);
    }

    @Override
    public Text getTabName() {
        return Text.from(R.string.title_school_accreditation_level);
    }

    @Override
    public void setData(SchoolAccreditationTallyLevel data) {
        level1TextView.setText(String.valueOf(data.getCountOfOnes()));
        level2TextView.setText(String.valueOf(data.getCountOfTwos()));
        level3TextView.setText(String.valueOf(data.getCountOfThrees()));
        level4TextView.setText(String.valueOf(data.getCountOfFours()));
        scoreTextView.setText(String.valueOf(data.getTallyScore()));
        levelTextView.setText(String.valueOf(data.getLevel().getName().getString(getContext())));
        levelTextView.setBackgroundColor(getContext().getColor(data.getLevel().getColorRes()));
    }

    @Override
    public void setHeaderItem(SummaryHeaderView.Item item) {
        summaryHeaderView.setItem(item);
    }
}
