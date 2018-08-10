package fm.doe.national.ui.screens.main;

import android.content.Intent;
import android.os.Bundle;

import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

import butterknife.OnClick;
import fm.doe.national.MicronesiaApplication;
import fm.doe.national.R;
import fm.doe.national.data.converters.DataImporter;
import fm.doe.national.data.data_source.db.DatabaseDataSource;
import fm.doe.national.data.data_source.db.dao.DatabaseHelper;
import fm.doe.national.data.data_source.db.dao.GroupStandardDao;
import fm.doe.national.data.data_source.db.models.survey.OrmLiteGroupStandard;
import fm.doe.national.ui.screens.base.BaseActivity;

public class MainActivity extends BaseActivity {

    private static final int FILE_REQUEST_CODE = 101;
    @Inject
    DatabaseDataSource staticDataSource;

    @Inject
    DatabaseHelper databaseHelper;

    @Inject
    DataImporter dataImporter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MicronesiaApplication.getAppComponent().inject(this);

        try {
            GroupStandardDao answerDao = databaseHelper.getGroupStandardDao();
            List<OrmLiteGroupStandard> ormLiteGroupStandards = answerDao.queryForAll();
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
          /*  try {
                InputStream is = getContentResolver().openInputStream(data.getData());
                jsonImporter.importData(StreamUtils.asString(is));
            } catch (IOException e) {
                e.printStackTrace();
            }*/
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
