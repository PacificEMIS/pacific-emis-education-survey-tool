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
import fm.doe.national.data.data_source.db.OrmLiteDataSource;
import fm.doe.national.data.data_source.db.dao.DatabaseHelper;
import fm.doe.national.data.data_source.db.dao.GroupStandardDao;
import fm.doe.national.data.data_source.db.models.survey.OrmLiteGroupStandard;
import fm.doe.national.ui.screens.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }
}
