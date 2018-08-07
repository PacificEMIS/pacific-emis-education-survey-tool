package fm.doe.national.ui.screens.main;

import android.content.ContentProvider;
import android.content.Intent;
import android.os.Bundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.doe.national.MicronesiaApplication;
import fm.doe.national.R;
import fm.doe.national.data.converters.FileImporter;
import fm.doe.national.data.converters.SurveyImporter;
import fm.doe.national.data.data_source.db.DbAccreditationDataSource;
import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.utils.StreamUtils;

public class MainActivity extends BaseActivity {

    private static final int FILE_REQUEST_CODE = 101;
    @Inject
    DbAccreditationDataSource staticDataSource;

    @Inject
    SurveyImporter jsonImporter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        MicronesiaApplication.getAppComponent().inject(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                InputStream is = getContentResolver().openInputStream(data.getData());
                jsonImporter.importData(StreamUtils.asString(is));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.button_browse_test)
    public void onBrowseButtonClick() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        startActivityForResult(intent, FILE_REQUEST_CODE);
    }
}
