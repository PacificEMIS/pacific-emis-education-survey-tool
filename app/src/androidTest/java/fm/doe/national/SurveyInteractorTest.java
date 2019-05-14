package fm.doe.national;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fm.doe.national.app_support.utils.DisposeBag;
import fm.doe.national.data.data_source.RoomDataSource;
import fm.doe.national.data.model.AnswerState;
import fm.doe.national.data.model.mutable.MutableAnswer;
import fm.doe.national.data.model.mutable.MutableCategory;
import fm.doe.national.data.model.mutable.MutableCriteria;
import fm.doe.national.data.model.mutable.MutableStandard;
import fm.doe.national.data.model.mutable.MutableSubCriteria;
import fm.doe.national.data.model.mutable.MutableSurvey;
import fm.doe.national.data.serialization.parsers.XmlSurveyParser;
import fm.doe.national.domain.SurveyInteractorImpl;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class SurveyInteractorTest {

    private final XmlSurveyParser surveyParser = new XmlSurveyParser();
    private SurveyInteractorImpl interactor;
    private RoomDataSource dataSource;
    private DisposeBag disposeBag;

    @BeforeClass
    public static void beforeClass() {
        RxAndroidPlugins.reset();
        RxJavaPlugins.reset();
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerCallable -> Schedulers.trampoline());
    }

    @AfterClass
    public static void afterClass(){
        RxAndroidPlugins.reset();
        RxJavaPlugins.reset();
    }

    @Before
    public void before() {
        Context context = ApplicationProvider.getApplicationContext();
        dataSource = new RoomDataSource(context);
        interactor = new SurveyInteractorImpl(dataSource);
        disposeBag = new DisposeBag();
        Single.fromCallable(() -> surveyParser.parse(openSurveyFile()))
                .flatMapCompletable(survey -> dataSource.rewriteStaticSurvey(survey))
                .andThen(dataSource.clearDynamicData())
                .blockingAwait();
    }

    @After
    public void after() {
        disposeBag.dispose();
    }

    @Nullable
    private InputStream openSurveyFile() {
        return openFile("survey.xml");
    }

    @Nullable
    private InputStream openFile(String fileName) {
        ClassLoader classLoader = this.getClass().getClassLoader();
        if (classLoader == null) {
            return null;
        }
        return classLoader.getResourceAsStream(fileName);
    }

    @After
    public void deInit() {
        dataSource.closeConnections();
    }

    @Test
    public void canSetCurrentSurveyAndGetValues() {
        MutableSurvey survey = dataSource.createSurvey("CHK001", "canSetCurrentSurveyAndGetValues", new Date())
                .flatMap(sh -> interactor.getAllSurveys())
                .blockingGet().get(0);

        interactor.setCurrentSurvey(survey);

        List<MutableCategory> categories = interactor.requestCategories().blockingGet();
        assertEquals(4, categories.size());
        MutableCategory category = categories.get(3);
        assertEquals("Classroom Observation", category.getTitle());
        assertEquals(5, category.getStandards().size());
        assertEquals(0, category.getProgress().completed);

        List<MutableStandard> standards = interactor.requestStandards(category.getId()).blockingGet();
        assertEquals(5, standards.size());
        MutableStandard standard = standards.get(2);
        assertEquals("CO3", standard.getSuffix());
        assertEquals("Teaching and learning", standard.getTitle());
        assertEquals(0, standard.getProgress().completed);
        assertEquals(20, standard.getProgress().total);

        List<MutableCriteria> criterias = interactor.requestCriterias(category.getId(), standard.getId()).blockingGet();
        assertEquals(4, criterias.size());
        MutableCriteria criteria = criterias.get(0);
        assertEquals("CO3.1", criteria.getSuffix());
        assertFalse(criteria.getSubCriterias().isEmpty());
        assertEquals(0, criteria.getProgress().completed);
        assertEquals(5, criteria.getProgress().total);
    }

    @Test
    public void canHandleProgressChanges() {
        //region prepare
        MutableSurvey survey = dataSource.createSurvey("CHK001", "canSetCurrentSurveyAndGetValues", new Date())
                .flatMap(sh -> interactor.getAllSurveys())
                .blockingGet()
                .get(0);

        interactor.setCurrentSurvey(survey);

        List<MutableCategory> categories = interactor.requestCategories().blockingGet();
        MutableCategory category = categories.get(3);
        assertEquals(0, category.getProgress().completed);

        List<MutableStandard> standards = interactor.requestStandards(category.getId()).blockingGet();
        MutableStandard standard = standards.get(2);
        assertEquals(0, standard.getProgress().completed);
        assertEquals(20, standard.getProgress().total);

        List<MutableCriteria> criterias = interactor.requestCriterias(category.getId(), standard.getId()).blockingGet();
        MutableCriteria criteria = criterias.get(0);
        assertEquals(0, criteria.getProgress().completed);
        assertEquals(5, criteria.getProgress().total);

        List<MutableCriteria> updatedCriterias = new ArrayList<>();
        List<MutableStandard> updatedStandards = new ArrayList<>();
        List<MutableCategory> updatedCategories = new ArrayList<>();
        List<MutableSurvey> updatedSurveys = new ArrayList<>();

        disposeBag.add(interactor.getCriteriaProgressSubject().subscribe(updatedCriterias::add));
        disposeBag.add(interactor.getStandardProgressSubject().subscribe(updatedStandards::add));
        disposeBag.add(interactor.getCategoryProgressSubject().subscribe(updatedCategories::add));
        disposeBag.add(interactor.getSurveyProgressSubject().subscribe(updatedSurveys::add));
        //endregion

        // region case 1
        MutableSubCriteria subCriteria = criteria.getSubCriterias().get(0);
        MutableAnswer answer = subCriteria.getAnswer();
        answer.setState(AnswerState.POSITIVE);
        interactor.updateAnswer(
                answer,
                category.getId(),
                standard.getId(),
                criteria.getId(),
                subCriteria.getId()
        ).blockingAwait();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            fail();
        }

        assertEquals(1, updatedCategories.size());
        assertEquals(1, updatedStandards.size());
        assertEquals(1, updatedCategories.size());
        assertEquals(1, updatedSurveys.size());

        assertEquals(criteria.getId(), updatedCriterias.get(0).getId());
        assertEquals(standard.getId(), updatedStandards.get(0).getId());
        assertEquals(category.getId(), updatedCategories.get(0).getId());
        assertEquals(survey.getId(), updatedSurveys.get(0).getId());

        assertEquals(1, updatedCriterias.get(0).getProgress().completed);
        assertEquals(1, updatedStandards.get(0).getProgress().completed);
        assertEquals(1, updatedCategories.get(0).getProgress().completed);
        assertEquals(1, updatedSurveys.get(0).getProgress().completed);


        assertEquals(criteria.getProgress().total, updatedCriterias.get(0).getProgress().total);
        assertEquals(standard.getProgress().total, updatedStandards.get(0).getProgress().total);
        assertEquals(category.getProgress().total, updatedCategories.get(0).getProgress().total);
        assertEquals(survey.getProgress().total, updatedSurveys.get(0).getProgress().total);

        updatedCategories.clear();
        updatedCriterias.clear();
        updatedStandards.clear();
        updatedSurveys.clear();
        //endregion

        //region case 2
        MutableSubCriteria subCriteria1 = criteria.getSubCriterias().get(1);
        MutableAnswer answer1 = subCriteria1.getAnswer();
        answer1.setState(AnswerState.NEGATIVE);
        interactor.updateAnswer(
                answer1,
                category.getId(),
                standard.getId(),
                criteria.getId(),
                subCriteria1.getId()
        ).blockingAwait();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            fail();
        }

        assertEquals(1, updatedCategories.size());
        assertEquals(1, updatedStandards.size());
        assertEquals(1, updatedCategories.size());
        assertEquals(1, updatedSurveys.size());

        assertEquals(criteria.getId(), updatedCriterias.get(0).getId());
        assertEquals(standard.getId(), updatedStandards.get(0).getId());
        assertEquals(category.getId(), updatedCategories.get(0).getId());
        assertEquals(survey.getId(), updatedSurveys.get(0).getId());

        assertEquals(2, updatedCriterias.get(0).getProgress().completed);
        assertEquals(2, updatedStandards.get(0).getProgress().completed);
        assertEquals(2, updatedCategories.get(0).getProgress().completed);
        assertEquals(2, updatedSurveys.get(0).getProgress().completed);


        assertEquals(criteria.getProgress().total, updatedCriterias.get(0).getProgress().total);
        assertEquals(standard.getProgress().total, updatedStandards.get(0).getProgress().total);
        assertEquals(category.getProgress().total, updatedCategories.get(0).getProgress().total);
        assertEquals(survey.getProgress().total, updatedSurveys.get(0).getProgress().total);

        updatedCategories.clear();
        updatedCriterias.clear();
        updatedStandards.clear();
        updatedSurveys.clear();
        //endregion

        //region case 3
        MutableSubCriteria subCriteria2 = criteria.getSubCriterias().get(2);
        MutableAnswer answer2 = subCriteria2.getAnswer();
        answer2.setState(AnswerState.NOT_ANSWERED);
        answer2.setComment("adgg");
        interactor.updateAnswer(
                answer2,
                category.getId(),
                standard.getId(),
                criteria.getId(),
                subCriteria2.getId()
        ).blockingAwait();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            fail();
        }

        assertEquals(1, updatedCategories.size());
        assertEquals(1, updatedStandards.size());
        assertEquals(1, updatedCategories.size());
        assertEquals(1, updatedSurveys.size());

        assertEquals(criteria.getId(), updatedCriterias.get(0).getId());
        assertEquals(standard.getId(), updatedStandards.get(0).getId());
        assertEquals(category.getId(), updatedCategories.get(0).getId());
        assertEquals(survey.getId(), updatedSurveys.get(0).getId());

        assertEquals(2, updatedCriterias.get(0).getProgress().completed);
        assertEquals(2, updatedStandards.get(0).getProgress().completed);
        assertEquals(2, updatedCategories.get(0).getProgress().completed);
        assertEquals(2, updatedSurveys.get(0).getProgress().completed);


        assertEquals(criteria.getProgress().total, updatedCriterias.get(0).getProgress().total);
        assertEquals(standard.getProgress().total, updatedStandards.get(0).getProgress().total);
        assertEquals(category.getProgress().total, updatedCategories.get(0).getProgress().total);
        assertEquals(survey.getProgress().total, updatedSurveys.get(0).getProgress().total);

        updatedCategories.clear();
        updatedCriterias.clear();
        updatedStandards.clear();
        updatedSurveys.clear();
        //endregion

        //region case 4
        MutableAnswer answer3 = subCriteria.getAnswer();
        answer3.setState(AnswerState.NOT_ANSWERED);
        interactor.updateAnswer(
                answer3,
                category.getId(),
                standard.getId(),
                criteria.getId(),
                subCriteria.getId()
        ).blockingAwait();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            fail();
        }

        assertEquals(1, updatedCategories.size());
        assertEquals(1, updatedStandards.size());
        assertEquals(1, updatedCategories.size());
        assertEquals(1, updatedSurveys.size());

        assertEquals(criteria.getId(), updatedCriterias.get(0).getId());
        assertEquals(standard.getId(), updatedStandards.get(0).getId());
        assertEquals(category.getId(), updatedCategories.get(0).getId());
        assertEquals(survey.getId(), updatedSurveys.get(0).getId());

        assertEquals(1, updatedCriterias.get(0).getProgress().completed);
        assertEquals(1, updatedStandards.get(0).getProgress().completed);
        assertEquals(1, updatedCategories.get(0).getProgress().completed);
        assertEquals(1, updatedSurveys.get(0).getProgress().completed);


        assertEquals(criteria.getProgress().total, updatedCriterias.get(0).getProgress().total);
        assertEquals(standard.getProgress().total, updatedStandards.get(0).getProgress().total);
        assertEquals(category.getProgress().total, updatedCategories.get(0).getProgress().total);
        assertEquals(survey.getProgress().total, updatedSurveys.get(0).getProgress().total);

        updatedCategories.clear();
        updatedCriterias.clear();
        updatedStandards.clear();
        updatedSurveys.clear();
        //endregion
    }

}
