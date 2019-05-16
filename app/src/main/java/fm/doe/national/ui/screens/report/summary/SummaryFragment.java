package fm.doe.national.ui.screens.report.summary;

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

public class SummaryFragment extends BaseReportFragment implements SummaryView {

    private final SummaryStandardAdapter adapter = new SummaryStandardAdapter();

    @InjectPresenter
    SummaryPresenter presenter;

    @BindView(R.id.recyclerview_summary)
    RecyclerView summaryRecyclerView;

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
        summaryRecyclerView.setAdapter(adapter);
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
}
