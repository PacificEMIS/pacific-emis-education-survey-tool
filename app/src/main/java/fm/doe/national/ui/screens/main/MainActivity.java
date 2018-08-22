package fm.doe.national.ui.screens.main;

import android.os.Bundle;

import java.io.InputStream;

import javax.inject.Inject;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.R;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.models.SchoolAccreditation;
import fm.doe.national.data.parsers.SchoolAccreditationParser;
import fm.doe.national.ui.screens.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Inject
    SchoolAccreditationParser schoolAccreditationParser;

    @Inject
    DataSource dataSource;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MicronesiaApplication.getAppComponent().inject(this);

        InputStream inputStream = null;
        try {
            inputStream = getAssets().open("surveys.xml");
//            String data = StreamUtils.asString(inputStream);

            SchoolAccreditation schoolAccreditation = schoolAccreditationParser.parse(inputStream);

            dataSource.createSchoolAccreditation(schoolAccreditation).subscribe();
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
