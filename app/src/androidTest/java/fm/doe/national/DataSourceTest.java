package fm.doe.national;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;
import java.util.List;

import fm.doe.national.data.data_source.RoomDataSource;
import fm.doe.national.data.model.School;
import fm.doe.national.data.persistence.entity.PersistenceSchool;
import fm.doe.national.data.serialization.parsers.CsvSchoolParser;
import fm.doe.national.data.serialization.parsers.XmlSurveyParser;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.observers.TestObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DataSourceTest {

    private final XmlSurveyParser surveyParser = new XmlSurveyParser();
    private final CsvSchoolParser schoolParser = new CsvSchoolParser();
    private RoomDataSource dataSource;

    @BeforeClass
    public static void before() {
        RxAndroidPlugins.reset();
        RxJavaPlugins.reset();
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerCallable -> Schedulers.trampoline());
    }

    @AfterClass
    public static void after(){
        RxAndroidPlugins.reset();
        RxJavaPlugins.reset();
    }

    @Before
    public void init() {
        Context context = ApplicationProvider.getApplicationContext();
        dataSource = new RoomDataSource(context);
    }

    @Nullable
    private InputStream openSurveyFile() {
        return openFile("survey.xml");
    }

    @Nullable
    private InputStream openSchoolsFile() {
        return openFile("schools.csv");
    }

    @Nullable
    private InputStream openFile(String fileName) {
        ClassLoader classLoader = this.getClass().getClassLoader();
        if (classLoader == null) {
            return null;
        }
        return classLoader.getResourceAsStream(fileName);
    }

    @Test
    public void testWriteAndLoadSchools() {
        TestObserver<List<PersistenceSchool>> testObserver = new TestObserver<>();
        Single.fromCallable(() -> schoolParser.parse(openSchoolsFile()))
                .flatMapCompletable(schools -> dataSource.rewriteSchools(schools))
                .andThen(dataSource.loadSchools())
                .subscribe(testObserver);
        testObserver.awaitTerminalEvent();
        testObserver.assertValue(TestUtil.check(schools -> {
            assertEquals(186, schools.size());
            School firstSchool = schools.get(0);
            assertEquals("CHK001", firstSchool.getSuffix());
            assertEquals("Akoyikoyi School", firstSchool.getName());


            School lastSchool = schools.get(185);
            assertEquals("KSA208", lastSchool.getSuffix());
            assertEquals("Walung Elementary School", lastSchool.getName());
        }));
    }

}
