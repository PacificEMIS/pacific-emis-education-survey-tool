package fm.doe.national.fcm_report.ui.levels;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.omega_r.libs.omegatypes.Text;
import com.omega_r.libs.views.OmegaTextView;
import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

import fm.doe.national.core.utils.ViewUtils;
import fm.doe.national.fcm_report.di.ComponentInjector;
import fm.doe.national.fcm_report.R;
import fm.doe.national.fcm_report.data.model.SchoolAccreditationLevel;
import fm.doe.national.report_core.domain.ReportInteractor;
import fm.doe.national.report_core.domain.ReportLevel;
import fm.doe.national.report_core.ui.base.BaseReportFragment;
import fm.doe.national.report_core.ui.summary_header.SummaryHeaderView;

public class LevelsFragment extends BaseReportFragment implements LevelsView {

    @InjectPresenter
    LevelsPresenter presenter;

    private RecyclerView recyclerView;
    private OmegaTextView determinationTextView;
    private TextView levelTextView;
    private SummaryHeaderView summaryHeaderView;

    private final EvalutaionFormsAdapter adapter = new EvalutaionFormsAdapter();

    private TextView totalObtainedScoreTextView;
    private TextView totalFinalScoreTextView;

    @ProvidePresenter
    LevelsPresenter providePresenter() {
        return new LevelsPresenter(ComponentInjector.getComponent(getActivity().getApplication()));
    }

    public LevelsFragment(ReportInteractor interactor) {
        // used in reflection
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fcm_levels, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);

        recyclerView.setAdapter(adapter);

        ViewUtils.forEachChild(
                view.findViewById(R.id.layout_titles),
                TextView.class,
                textView -> {
                    textView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.grey_600));
                    textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                }
        );

        ViewGroup totalsViewGroup = view.findViewById(R.id.layout_total_scores);
        ViewUtils.forEachChild(
                totalsViewGroup,
                TextView.class,
                textView -> textView.setTypeface(textView.getTypeface(), Typeface.BOLD)
        );

        totalObtainedScoreTextView = totalsViewGroup.findViewById(R.id.textview_obtained_score);
        totalFinalScoreTextView = totalsViewGroup.findViewById(R.id.textview_final_score);
    }

    private void bindViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerview);
        determinationTextView = view.findViewById(R.id.textview_determination);
        levelTextView = view.findViewById(R.id.textview_level);
        summaryHeaderView = view.findViewById(R.id.summaryheaderview);
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

        ReportLevel reportLevel = data.getReportLevel();
        reportLevel.getName().applyTo(levelTextView, null);
        levelTextView.setBackgroundColor(ContextCompat.getColor(getContext(), reportLevel.getColorRes()));

        reportLevel.getMeaning().applyTo(determinationTextView, null);
    }

    @Override
    public void setHeaderItem(SummaryHeaderView.Item item) {
        summaryHeaderView.setItem(item);
    }
}
