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

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.core.utils.ViewUtils;
import fm.doe.national.ui.custom_views.summary_header.SummaryHeaderView;
import fm.doe.national.core.data.model.ReportLevel;
import fm.doe.national.ui.screens.report.base.BaseReportFragment;

public class LevelsFragment extends BaseReportFragment implements LevelsView {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.textview_determination)
    OmegaTextView determinationTextView;

    @BindView(R.id.textview_level)
    TextView levelTextView;

    @BindView(R.id.summaryheaderview)
    SummaryHeaderView summaryHeaderView;

    private final EvalutaionFormsAdapter adapter = new EvalutaionFormsAdapter();

    private TextView totalObtainedScoreTextView;
    private TextView totalFinalScoreTextView;


    @InjectPresenter
    LevelsPresenter presenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_levels, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
