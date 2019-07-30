package fm.doe.national.offline_sync.ui.progress;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.data.model.mutable.MutableProgress;
import fm.doe.national.core.ui.screens.base.BaseActivity;
import fm.doe.national.core.utils.ViewUtils;
import fm.doe.national.offline_sync.R;
import fm.doe.national.offline_sync.data.model.Device;
import fm.doe.national.offline_sync.di.OfflineSyncComponentInjector;
import fm.doe.national.remote_storage.di.RemoteStorageComponentInjector;

public class ProgressActivity extends BaseActivity implements ProgressView, View.OnClickListener {

    @InjectPresenter
    ProgressPresenter presenter;

    private TextView descriptionTextView;
    private ProgressBar progressBar;
    private TextView deviceNameTextView;
    private TextView schoolNameTextView;
    private TextView macNameTextView;
    private TextView surveyDateTextView;
    private Button tryAgainButton;
    private Button okButton;

    public static Intent createIntent(Context context) {
        return new Intent(context, ProgressActivity.class);
    }

    @ProvidePresenter
    ProgressPresenter providePresenter() {
        Application application = getApplication();
        return new ProgressPresenter(
                OfflineSyncComponentInjector.getComponent(application),
                RemoteStorageComponentInjector.getComponent(application)
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_merge_progress);
        descriptionTextView = findViewById(R.id.textview_description);
        progressBar = findViewById(R.id.progressbar);
        deviceNameTextView = findViewById(R.id.textview_device_name);
        schoolNameTextView = findViewById(R.id.textview_school_name);
        macNameTextView = findViewById(R.id.textview_mac);
        surveyDateTextView = findViewById(R.id.textview_survey_date);
        tryAgainButton = findViewById(R.id.button_try_again);
        okButton = findViewById(R.id.button_ok);
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
        text.applyTo(descriptionTextView);
    }

    @Override
    public void setDevice(Device device) {
        macNameTextView.setText(device.getAddress());
        deviceNameTextView.setText(device.getName());
    }

    @Override
    public void setSurvey(Survey survey) {
        schoolNameTextView.setText(survey.getSchoolName());
        surveyDateTextView.setText(survey.getSurveyTag());
    }

    @Override
    public void setMergeProgress(int progress) {
        ViewUtils.rebindProgress(new MutableProgress(100, progress), null, progressBar);
    }

    @Override
    public void showTryAgain() {
        tryAgainButton.setVisibility(View.VISIBLE);
        okButton.setVisibility(View.GONE);
    }

    @Override
    public void showContinue() {
        tryAgainButton.setVisibility(View.GONE);
        okButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v == tryAgainButton || v == okButton) {
            presenter.onEndSessionPressed();
        }
    }
}
