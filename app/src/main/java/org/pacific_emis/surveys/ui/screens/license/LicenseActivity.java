package org.pacific_emis.surveys.ui.screens.license;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;

import org.pacific_emis.surveys.BuildConfig;
import org.pacific_emis.surveys.R;
import org.pacific_emis.surveys.core.ui.screens.base.BaseActivity;
import org.pacific_emis.surveys.core.utils.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import butterknife.BindView;

public class LicenseActivity extends BaseActivity {

    @BindView(R.id.textview_license)
    TextView licenseTextView;

    public static Intent createIntent(Context context) {
        return new Intent(context, LicenseActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.label_license);
        readLicenseFile();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_license;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        ActionBar supportActionBar = getSupportActionBar();

        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white);
        }
    }

    private void readLicenseFile() {
        try (InputStream inputStream = getAssets().open(BuildConfig.LICENSE_FILE_NAME)) {

            String licenceText = String.format(
                    StreamUtils.asString(inputStream),
                    BuildConfig.VERSION_ALIAS,
                    Calendar.getInstance().get(Calendar.YEAR)
            );

            licenseTextView.setText(licenceText);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
