package fm.doe.national.ui.screens.license;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import fm.doe.national.BuildConfig;
import fm.doe.national.R;
import fm.doe.national.core.ui.screens.base.BaseActivity;
import fm.doe.national.core.utils.StreamUtils;

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

            String licenceText = String.format(StreamUtils.asString(inputStream), BuildConfig.VERSION_ALIAS);

            licenseTextView.setText(licenceText);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
