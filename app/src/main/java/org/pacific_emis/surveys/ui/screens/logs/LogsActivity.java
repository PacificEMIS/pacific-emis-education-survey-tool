package org.pacific_emis.surveys.ui.screens.logs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.presenter.InjectPresenter;

import org.pacific_emis.surveys.R;
import org.pacific_emis.surveys.core.data.model.SurveyLog;
import org.pacific_emis.surveys.core.ui.screens.base.BaseActivity;

import java.util.List;

import butterknife.BindView;

public class LogsActivity extends BaseActivity implements LogsView {

    @InjectPresenter
    LogsPresenter presenter;

    @BindView(R.id.recyclerview_logs)
    RecyclerView logsRecycler;

    private final LogsAdapter adapter = new LogsAdapter();

    public static Intent createIntent(Context context) {
        return new Intent(context, LogsActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logsRecycler.setAdapter(adapter);
        setTitle(R.string.label_logs);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_logs;
    }


    @Override
    public void setLogs(List<SurveyLog> surveys) {
        adapter.setItems(surveys);
    }
}