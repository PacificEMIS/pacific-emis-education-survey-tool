package fm.doe.national.data.data_source.static_source;

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
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.static_source.models.Accreditation;
import fm.doe.national.data.data_source.static_source.models.Schools;
import fm.doe.national.data.data_source.static_source.models.StaticGroupStandard;
import fm.doe.national.data.data_source.static_source.models.StaticSchool;
import fm.doe.national.models.survey.Answer;
import fm.doe.national.models.survey.Criteria;
import fm.doe.national.models.survey.GroupStandard;
import fm.doe.national.models.survey.School;
import fm.doe.national.models.survey.Standard;
import fm.doe.national.models.survey.SubCriteria;
import fm.doe.national.models.survey.Survey;
import io.reactivex.Single;

public class StaticDataSource implements DataSource {

    private List<StaticSchool> schoolList;
    private List<StaticGroupStandard> groupStandardList;

    public StaticDataSource(Context context, Gson gson) {
        AssetManager assetManager = context.getAssets();
        initSchools(assetManager, gson);
        initAccreditation(assetManager, gson);
    }

    private void initSchools(AssetManager assetManager, Gson gson) {
        try {
            InputStream inputStream = assetManager.open("schools.json");
            Reader inputReader = new InputStreamReader(inputStream);
            Schools schools = gson.fromJson(inputReader, Schools.class);
            schoolList = schools.getList();
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
            groupStandardList = accreditation.getList();
            inputStream.close();
            inputReader.close();
        } catch (IOException exc) {
            if (BuildConfig.DEBUG) {
                throw new RuntimeException(exc);
            }
        }
    }

    @Override
    public Single<School> createSchool(String name) {
        throw new RuntimeException("You can't call this method inside StaticDataSource");
    }

    @Override
    public Single<List<School>> requestSchools() {
        return Single.just(new ArrayList<>(schoolList));
    }

    @Override
    public Single<Survey> createSurvey(School school, int year) {
        throw new RuntimeException("You can't call this method inside StaticDataSource");
    }

    @Override
    public Single<GroupStandard> createGroupStandard() {
        throw new RuntimeException("You can't call this method inside StaticDataSource");
    }

    @Override
    public Single<List<GroupStandard>> requestGroupStandard() {
        return Single.just(new ArrayList<>(groupStandardList));
    }

    @Override
    public Single<Standard> createStandard(String name, GroupStandard group) {
        throw new RuntimeException("You can't call this method inside StaticDataSource");
    }

    @Override
    public Single<Criteria> createCriteria(String name, Standard standard) {
        throw new RuntimeException("You can't call this method inside StaticDataSource");
    }

    @Override
    public Single<SubCriteria> createSubCriteria(String name, Criteria criteria) {
        throw new RuntimeException("You can't call this method inside StaticDataSource");
    }

    @Override
    public Single<Answer> createAnswer(boolean answer, SubCriteria criteria, Survey survey) {
        throw new RuntimeException("You can't call this method inside StaticDataSource");
    }

}
