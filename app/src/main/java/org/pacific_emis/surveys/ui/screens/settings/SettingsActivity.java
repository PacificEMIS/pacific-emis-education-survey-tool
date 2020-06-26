package org.pacific_emis.surveys.ui.screens.settings;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.omega_r.libs.omegarecyclerview.BaseListAdapter;
import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindView;
import org.pacific_emis.surveys.R;
import org.pacific_emis.surveys.core.preferences.entities.AppRegion;
import org.pacific_emis.surveys.core.preferences.entities.OperatingMode;
import org.pacific_emis.surveys.core.ui.screens.base.BaseActivity;
import org.pacific_emis.surveys.core.ui.views.InputDialog;
import org.pacific_emis.surveys.ui.screens.logo.LogoActivity;
import org.pacific_emis.surveys.ui.screens.password.PasswordsActivity;
import org.pacific_emis.surveys.ui.screens.settings.items.Item;
import org.pacific_emis.surveys.ui.screens.templates.SurveyTemplatesActivity;

public class SettingsActivity extends BaseActivity implements SettingsView, BaseListAdapter.OnItemClickListener<Item>, SettingsAdapter.OnBooleanValueChangedListener {

    private static final String TAG_INPUT_DIALOG = "TAG_INPUT_DIALOG";

    private final SettingsAdapter adapter = new SettingsAdapter(this, this);

    @InjectPresenter
    SettingsPresenter presenter;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @Nullable
    private Dialog inputDialog;

    @Nullable
    private Dialog selectorDialog;

    public static Intent createIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        setTitle(R.string.label_settings);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_settings;
    }

    @Override
    public void setItems(List<Item> options) {
        adapter.setItems(options);
    }

    @Override
    public void onItemClick(Item item) {
        presenter.onItemPressed(item);
    }

    @Override
    public void showInputDialog(@Nullable Text title, @Nullable Text existingText, InputListener listener) {
        inputDialog = InputDialog.create(this, title, existingText)
                .setListener(listener::onInput);
        inputDialog.show();
    }

    @Override
    public void showRegionSelector(RegionListener listener) {
        selectorDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.sheet_app_context, null);
        View fsmItemView = sheetView.findViewById(R.id.textview_fsm);
        View rmiItemView = sheetView.findViewById(R.id.textview_rmi);
        TextView titleTextView = sheetView.findViewById(R.id.textview_title);
        titleTextView.setText(R.string.title_choose_context);
        fsmItemView.setOnClickListener(v -> {
            listener.onRegionSelected(AppRegion.FSM);
            safeDismiss(selectorDialog);
        });
        rmiItemView.setOnClickListener(v -> {
            listener.onRegionSelected(AppRegion.RMI);
            safeDismiss(selectorDialog);
        });
        selectorDialog.setContentView(sheetView);
        selectorDialog.show();
    }

    @Override
    public void showOperatingModeSelector(OperatingModeListener listener) {
        selectorDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.sheet_operating_mode, null);
        View prodItemView = sheetView.findViewById(R.id.textview_prod);
        View devItemView = sheetView.findViewById(R.id.textview_dev);
        TextView titleTextView = sheetView.findViewById(R.id.textview_title);
        titleTextView.setText(R.string.title_choose_op_mode);
        prodItemView.setOnClickListener(v -> {
            listener.onOperatingModeSelected(OperatingMode.PROD);
            safeDismiss(selectorDialog);
        });
        devItemView.setOnClickListener(v -> {
            listener.onOperatingModeSelected(OperatingMode.DEV);
            safeDismiss(selectorDialog);
        });
        selectorDialog.setContentView(sheetView);
        selectorDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        safeDismiss(inputDialog);
        safeDismiss(selectorDialog);
    }

    private void safeDismiss(@Nullable Dialog dialog) {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void navigateToChangePassword() {
        startActivity(PasswordsActivity.createIntent(this, true));
    }

    @Override
    public void navigateToChangeLogo() {
        startActivity(LogoActivity.createIntent(this));
    }

    @Override
    public void navigateToTemplates() {
        startActivity(SurveyTemplatesActivity.createIntent(this));
    }

    @Nullable
    @Override
    public SettingsPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onBooleanValueChanged(Item item, boolean value) {
        presenter.onBooleanValueChanged(item, value);
    }
}
