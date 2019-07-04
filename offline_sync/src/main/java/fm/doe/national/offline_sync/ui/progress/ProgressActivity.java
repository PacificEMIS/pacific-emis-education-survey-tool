package fm.doe.national.offline_sync.ui.progress;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.ui.screens.base.BaseActivity;
import fm.doe.national.offline_sync.R;
import fm.doe.national.offline_sync.data.model.Device;
import fm.doe.national.offline_sync.di.OfflineSyncComponentInjector;

public class ProgressActivity extends BaseActivity implements ProgressView {

    @InjectPresenter
    ProgressPresenter presenter;

    @ProvidePresenter
    ProgressPresenter providePresenter() {
        return new ProgressPresenter(OfflineSyncComponentInjector.getComponent(getApplication()));
    }

    private TextView desciptionTextView;
    private ProgressBar progressBar;

    public static Intent createIntent(Context context) {
        return new Intent(context, ProgressActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        desciptionTextView = findViewById(R.id.textview_description);
        progressBar = findViewById(R.id.progressbar);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_progress;
    }

    @Override
    public void close() {
        this.finish();
    }

    @Override
    public void setDescription(Text text) {
        text.applyTo(desciptionTextView);
    }

    @Override
    public void setDevice(Device device) {

    }

    @Override
    public void setSurvey(Survey survey) {

    }

    @Override
    public void setMergeProgress(int progress) {

    }

    @Override
    public void showTryAgainButton() {

    }

    @Override
    public void showContinueButton() {

    }
}
