package fm.doe.national.offline_sync.ui.progress;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

import fm.doe.national.cloud.di.CloudComponentInjector;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.ui.screens.base.BaseActivity;
import fm.doe.national.core.ui.views.IconButton;
import fm.doe.national.core.utils.DateUtils;
import fm.doe.national.offline_sync.R;
import fm.doe.national.offline_sync.data.model.Device;
import fm.doe.national.offline_sync.di.OfflineSyncComponentInjector;

public class ProgressActivity extends BaseActivity implements ProgressView, View.OnClickListener {

    @InjectPresenter
    ProgressPresenter presenter;

    private TextView desciptionTextView;
    private ProgressBar progressBar;
    private TextView deviceNameTextView;
    private TextView schoolNameTextView;
    private TextView macNameTextView;
    private TextView surveyDateTextView;
    private IconButton tryAgainButton;
    private IconButton okButton;

    public static Intent createIntent(Context context) {
        return new Intent(context, ProgressActivity.class);
    }

    @ProvidePresenter
    ProgressPresenter providePresenter() {
        Application application = getApplication();
        return new ProgressPresenter(
                OfflineSyncComponentInjector.getComponent(application),
                CloudComponentInjector.getComponent(application)
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_merge_progress);
        desciptionTextView = findViewById(R.id.textview_description);
        progressBar = findViewById(R.id.progressbar);
        deviceNameTextView = findViewById(R.id.textview_device_name);
        schoolNameTextView = findViewById(R.id.textview_school_name);
        macNameTextView = findViewById(R.id.textview_mac);
        surveyDateTextView = findViewById(R.id.textview_survey_date);
        tryAgainButton = findViewById(R.id.iconbutton_try_again);
        okButton = findViewById(R.id.iconbutton_ok);
        tryAgainButton.setOnClickListener(this);
        okButton.setOnClickListener(this);
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
        macNameTextView.setText(device.getAddress());
        deviceNameTextView.setText(device.getName());
    }

    @Override
    public void setSurvey(Survey survey) {
        schoolNameTextView.setText(survey.getSchoolName());
        surveyDateTextView.setText(DateUtils.formatUi(survey.getDate()));
    }

    @Override
    public void setMergeProgress(int progress) {
        progressBar.setProgress(progress, true);
    }

    @Override
    public void showTryAgainButton() {
        tryAgainButton.setVisibility(View.VISIBLE);
        okButton.setVisibility(View.GONE);
    }

    @Override
    public void showContinueButton() {
        tryAgainButton.setVisibility(View.GONE);
        okButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == tryAgainButton.getId() || v.getId() == okButton.getId()) {
            presenter.onEndSessionPressed();
        }
    }
}
