package fm.doe.national.ui.screens.main;

import android.os.Bundle;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.R;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.models.School;
import fm.doe.national.data.parsers.SchoolParser;
import fm.doe.national.ui.screens.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

}
