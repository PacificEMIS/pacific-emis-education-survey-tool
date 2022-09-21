package org.pacific_emis.surveys.ui.screens.surveys;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import org.pacific_emis.surveys.R;
import org.pacific_emis.surveys.core.data.model.Survey;
import org.pacific_emis.surveys.core.ui.screens.base.BaseAdapter;
import org.pacific_emis.surveys.core.ui.screens.base.BasePresenter;
import org.pacific_emis.surveys.core.ui.views.InputDialog;
import org.pacific_emis.surveys.offline_sync.ui.base.BaseBluetoothActivity;
import org.pacific_emis.surveys.survey.ui.SurveyActivity;
import org.pacific_emis.surveys.ui.screens.menu.MainMenuActivity;
import org.pacific_emis.surveys.ui.screens.survey_creation.CreateSurveyActivity;

public class SurveysActivity extends BaseBluetoothActivity implements
        SurveysView,
        View.OnClickListener,
        BaseAdapter.OnItemClickListener<Survey>,
        SurveysAdapter.ItemClickListener {

    private static final String TAG = SurveysActivity.class.getName();
    private static final String SCHEME_WEB = "http";

    @InjectPresenter
    SurveysPresenter presenter;

    @BindView(R.id.recyclerview_categories)
    RecyclerView recyclerView;

    @BindView(R.id.fab_new_accreditation)
    FloatingActionButton newAccreditationFab;

    @BindView(R.id.textview_creation_hint)
    View creationHintView;

    private final SurveysAdapter surveysAdapter = new SurveysAdapter(this, this);

    private MenuItem exportAllMenuItem;
    private boolean isExportEnabled;

    @Nullable
    private Dialog inputDialog;

    public static Intent createIntent(Context context) {
        return new Intent(context, SurveysActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        ActionBar supportActionBar = getSupportActionBar();

        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white);
        }
    }

    private void initViews() {
        setTitle(R.string.label_school_accreditation);
        recyclerView.setAdapter(surveysAdapter);
        newAccreditationFab.setOnClickListener(this);
    }

    @Override
    public void onHomePressed() {
        startActivity(MainMenuActivity.createIntent(this));
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_surveys;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_surveys, menu);
        exportAllMenuItem = menu.findItem(R.id.action_export_all);
        updateMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_export_all:
                presenter.onExportAllPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void navigateToSurvey() {
        startActivity(new Intent(this, SurveyActivity.class));
    }

    @Override
    public void setSurveys(List<Survey> accreditations) {
        creationHintView.setVisibility(accreditations.isEmpty() ? View.VISIBLE : View.GONE);
        surveysAdapter.setItems(accreditations);
    }

    @Override
    public void removeSurvey(Survey passing) {
        surveysAdapter.removeItem(passing);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_new_accreditation:
                startActivity(CreateSurveyActivity.createIntent(this));
                break;
        }
    }

    @Override
    public void onItemClick(Survey item) {
        presenter.onSurveyPressed(item);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem, Survey survey) {
        switch (menuItem.getItemId()) {
            case R.id.action_merge:
                presenter.onSurveyMergePressed(survey);
                return true;
            case R.id.action_merged:
                presenter.onSurveyMergedPressed(survey);
                return true;
            case R.id.action_export_to_excel:
                presenter.onSurveyExportToExcelPressed(survey);
                return true;
            case R.id.action_remove:
                presenter.onSurveyRemovePressed(survey);
                return true;
            case R.id.action_change_date:
                presenter.onSurveyChangeDatePressed(survey);
                return true;
            case R.id.action_force_sync:
                presenter.onForceSyncPressed(survey);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onUploadItemClick(Survey survey) {
        switch (survey.getUploadState()) {
            case IN_PROGRESS:
                Toast.makeText(this, R.string.label_synced_in_progress, Toast.LENGTH_SHORT).show();
                break;
            case SUCCESSFULLY:
                Toast.makeText(this, R.string.label_synced_successfully, Toast.LENGTH_SHORT).show();
                break;
            case NOT_UPLOAD:
                Toast.makeText(this, R.string.label_has_not_synced, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Nullable
    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }

    @OnClick(R.id.textview_load_survey)
    void onLoadPartiallySavedSurveyPressed() {
        presenter.onLoadPartiallySavedSurveyPressed();
    }

    @Override
    public void setTitle(Text title) {
        this.setTitle(title.getString(this));
    }

    @Override
    public void setExportEnabled(boolean isEnabled) {
        this.isExportEnabled = isEnabled;
        updateMenu();
        surveysAdapter.setExportEnabled(isExportEnabled);
    }

    private void updateMenu() {
        if (exportAllMenuItem != null) {
            exportAllMenuItem.setVisible(isExportEnabled);
        }
    }

    @Override
    public void openInExternalApp(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
        String scheme = uri.getScheme();

        if (scheme == null) {
            Log.e(TAG, "uri should contain scheme");
            return;
        }

        if (scheme.contains(SCHEME_WEB)) {
            intent.setData(uri);
        } else {
            intent.setDataAndType(
                    uri,
                    MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(uri.getPath()))
            );
        }

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            showMessage(Text.from(R.string.title_error), Text.from(R.string.error_no_app_to_open));
        }
    }

    @Override
    public void showInputDialog(@Nullable Text title, @Nullable Text existingText, InputListener listener) {
        inputDialog = InputDialog.create(this, title, existingText, true).setListener(listener::onInput);
        inputDialog.show();
    }

}
