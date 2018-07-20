package fm.doe.national.data_source.static_source;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fm.doe.national.BuildConfig;
import fm.doe.national.data_source.StaticDataSource;
import fm.doe.national.data_source.static_source.models.Accreditation;
import fm.doe.national.data_source.static_source.models.Schools;
import fm.doe.national.models.survey.School;

public class StaticDataSourceImpl implements StaticDataSource {

    private List<School> schoolList;

    public StaticDataSourceImpl(Context context, Gson gson) {
        AssetManager assetManager = context.getAssets();
        initSchools(assetManager, gson);
        initAccreditation(assetManager, gson);
    }

    private void initSchools(AssetManager assetManager, Gson gson) {
        try {
            InputStream inputStream = assetManager.open("schools.json");
            Reader inputReader = new InputStreamReader(inputStream);
            Schools schools = gson.fromJson(inputReader, Schools.class);
            schoolList = new ArrayList<>(schools.getList());
            inputStream.close();
            inputReader.close();
        } catch (IOException exc) {
            if (BuildConfig.DEBUG) {
                throw new RuntimeException(exc);
            } else {
                schoolList = Collections.emptyList();
            }
        }
    }

    private void initAccreditation(AssetManager assetManager, Gson gson) {
        try {
            InputStream inputStream = assetManager.open("accreditation.json");
            Reader inputReader = new InputStreamReader(inputStream);
            Accreditation accreditation = gson.fromJson(inputReader, Accreditation.class);
            inputStream.close();
            inputReader.close();
        } catch (IOException exc) {
            if (BuildConfig.DEBUG) {
                throw new RuntimeException(exc);
            }
        }
    }

    @Override
    public List<School> getSchoolList() {
        return schoolList;
    }

}
