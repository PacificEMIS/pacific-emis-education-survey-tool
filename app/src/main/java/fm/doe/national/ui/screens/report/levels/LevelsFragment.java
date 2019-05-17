package fm.doe.national.ui.screens.report.levels;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.presenter.InjectPresenter;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.app_support.utils.ViewUtils;
import fm.doe.national.ui.screens.report.BaseReportFragment;
import fm.doe.national.ui.screens.report.ReportLevel;

public class LevelsFragment extends BaseReportFragment implements LevelsView {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.textview_determination)
    TextView determinationTextView;

    @BindView(R.id.textview_level)
    TextView levelTextView;

    private final EvalutaionFormsAdapter adapter = new EvalutaionFormsAdapter();

    private TextView totalObtainedScoreTextView;
    private TextView totalFinalScoreTextView;


    @InjectPresenter
    LevelsPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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

        String determinationPrefix = getString(R.string.title_determination_of_overall_level);
        SpannableString spannableString = new SpannableString(determinationPrefix + " " + reportLevel.getDetermination().getString(getContext()));
        spannableString.setSpan(
                new TypefaceSpan(getString(R.string.font_medium)),
                0, determinationPrefix.length(),
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        determinationTextView.setText(spannableString);
    }
}
