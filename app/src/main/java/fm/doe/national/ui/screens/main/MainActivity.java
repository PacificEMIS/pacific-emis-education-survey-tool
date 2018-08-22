package fm.doe.national.ui.screens.main;

import android.content.Intent;
import android.os.Bundle;


import com.tickaroo.tikxml.TikXml;
import com.tickaroo.tikxml.XmlReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import butterknife.OnClick;
import fm.doe.national.BuildConfig;
import fm.doe.national.MicronesiaApplication;
import fm.doe.national.R;
import fm.doe.national.data.data_source.db.OrmLiteDataSource;
import fm.doe.national.data.data_source.models.SchoolAccreditation;
import fm.doe.national.data.data_source.models.db.OrmLiteSchool;
import fm.doe.national.data.data_source.models.serializable.SerializableSchoolAccreditation;
import fm.doe.national.data.parsers.SchoolAccreditationParser;
import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.utils.StreamUtils;
import okio.Buffer;
import okio.BufferedSource;
import okio.Okio;

public class MainActivity extends BaseActivity {

    @Inject
    SchoolAccreditationParser schoolAccreditationParser;

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
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
