package org.pacific_emis.surveys;

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
import java.util.Date;
import java.util.List;

import org.pacific_emis.surveys.core.data.data_source.RoomDataSource;
import org.pacific_emis.surveys.core.data.model.Answer;
import org.pacific_emis.surveys.core.data.model.AnswerState;
import org.pacific_emis.surveys.core.data.model.Category;
import org.pacific_emis.surveys.core.data.model.Criteria;
import org.pacific_emis.surveys.core.data.model.Photo;
import org.pacific_emis.surveys.core.data.model.School;
import org.pacific_emis.surveys.core.data.model.Standard;
import org.pacific_emis.surveys.core.data.model.SubCriteria;
import org.pacific_emis.surveys.core.data.model.Survey;
import org.pacific_emis.surveys.core.data.model.mutable.MutableAnswer;
import org.pacific_emis.surveys.core.data.model.mutable.MutablePhoto;
import org.pacific_emis.surveys.core.data.serialization.parsers.CsvSchoolParser;
import org.pacific_emis.surveys.core.data.serialization.parsers.XmlSurveyParser;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.observers.TestObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DataSourceTest {

    private final XmlSurveyParser surveyParser = new XmlSurveyParser();
    private final CsvSchoolParser schoolParser = new CsvSchoolParser();
    private RoomDataSource dataSource;

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
    }

    @After
    public void after() {
        dataSource.closeConnections();
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
    public void testCanWriteAndLoadSchools() {
        TestObserver<List<School>> testObserver = new TestObserver<>();
        Single.fromCallable(() -> schoolParser.parse(openSchoolsFile()))
                .flatMapCompletable(schools -> dataSource.rewriteAllSchools(schools))
                .andThen(dataSource.loadSchools())
                .subscribe(testObserver);
        testObserver.awaitTerminalEvent();
        testObserver.assertValue(TestUtil.check(schools -> {
            assertEquals(186, schools.size());
            School firstSchool = schools.get(0);
            assertEquals("CHK001", firstSchool.getId());
            assertEquals("Akoyikoyi School", firstSchool.getName());


            School lastSchool = schools.get(185);
            assertEquals("KSA208", lastSchool.getId());
            assertEquals("Walung Elementary School", lastSchool.getName());
        }));
    }

    @Test
    public void testCanWriteAndLoadBaseSurvey() {
        TestObserver<Survey> testObserver = new TestObserver<>();
        Single.fromCallable(() -> surveyParser.parse(openSurveyFile()))
                .flatMapCompletable(survey -> dataSource.rewriteTemplateSurvey(survey))
                .andThen(dataSource.getTemplateSurvey())
                .subscribe(testObserver);
        testObserver.awaitTerminalEvent();
        testObserver.assertValue(TestUtil.check(survey -> {
            assertNull(survey.getCreateDate());
            assertNull(survey.getSchoolId());
            assertNull(survey.getSchoolName());
            assertEquals(SurveyType.SCHOOL_ACCREDITATION, survey.getSurveyType());
            assertEquals(1, survey.getVersion());
            assertEquals(4, survey.getCategories().size());
            Category category = survey.getCategories().get(3);
            assertEquals("Classroom Observation", category.getTitle());
            assertEquals(5, category.getStandards().size());
            Standard standard = category.getStandards().get(2);
            assertEquals("CO3", standard.getSuffix());
            assertEquals("Teaching and learning", standard.getTitle());
            assertEquals(4, standard.getCriterias().size());
            Criteria criteria = standard.getCriterias().get(0);
            assertEquals("CO3.1", criteria.getSuffix());
            assertFalse(criteria.getSubCriterias().isEmpty());
        }));
    }

    @Test
    public void testCreateNewSurvey() {
        TestObserver<Survey> testObserver = new TestObserver<>();
        Date creationDate = new Date();
        dataSource.deleteCreatedSurveys()
                .andThen(Single.fromCallable(() -> surveyParser.parse(openSurveyFile())))
                .flatMapCompletable(survey -> dataSource.rewriteTemplateSurvey(survey))
                .andThen(dataSource.createSurvey("CHK001", "testCreateNewSurvey", creationDate))
                .subscribe(testObserver);
        testObserver.awaitTerminalEvent();
        testObserver.assertValue(TestUtil.check(survey -> {
            assertTrue(survey.getId() > 0);
            assertEquals("testCreateNewSurvey", survey.getSchoolName());
            assertEquals("CHK001", survey.getSchoolId());
            assertEquals(creationDate, survey.getCreateDate());
            assertEquals(SurveyType.SCHOOL_ACCREDITATION, survey.getSurveyType());
            assertEquals(1, survey.getVersion());
            assertEquals(4, survey.getCategories().size());
            Category category = survey.getCategories().get(3);
            assertEquals("Classroom Observation", category.getTitle());
            assertEquals(5, category.getStandards().size());
            Standard standard = category.getStandards().get(2);
            assertEquals("CO3", standard.getSuffix());
            assertEquals("Teaching and learning", standard.getTitle());
            assertEquals(4, standard.getCriterias().size());
            Criteria criteria = standard.getCriterias().get(0);
            assertEquals("CO3.1", criteria.getSuffix());
            assertFalse(criteria.getSubCriterias().isEmpty());
            assertNotNull(criteria.getSubCriterias().get(0).getAnswer());
        }));
    }

    @Test
    public void testCanGetCreatedSurvey() {
        TestObserver<Survey> testObserver = new TestObserver<>();

        Date creationDate = new Date();
        dataSource.deleteCreatedSurveys()
                .andThen(Single.fromCallable(() -> surveyParser.parse(openSurveyFile())))
                .flatMapCompletable(survey -> dataSource.rewriteTemplateSurvey(survey))
                .andThen(dataSource.createSurvey("CHK001", "testCanGetCreatedSurvey", creationDate))
                .flatMap(survey -> dataSource.loadSurvey(survey.getId()))
                .subscribe(testObserver);
        testObserver.awaitTerminalEvent();
        testObserver.assertValue(TestUtil.check(survey -> {
            assertTrue(survey.getId() > 0);
            assertEquals("testCanGetCreatedSurvey", survey.getSchoolName());
            assertEquals("CHK001", survey.getSchoolId());
            assertEquals(creationDate, survey.getCreateDate());
            assertEquals(SurveyType.SCHOOL_ACCREDITATION, survey.getSurveyType());
            assertEquals(1, survey.getVersion());
            assertEquals(4, survey.getCategories().size());
            Category category = survey.getCategories().get(3);
            assertEquals("Classroom Observation", category.getTitle());
            assertEquals(5, category.getStandards().size());
            Standard standard = category.getStandards().get(2);
            assertEquals("CO3", standard.getSuffix());
            assertEquals("Teaching and learning", standard.getTitle());
            assertEquals(4, standard.getCriterias().size());
            Criteria criteria = standard.getCriterias().get(0);
            assertEquals("CO3.1", criteria.getSuffix());
            assertFalse(criteria.getSubCriterias().isEmpty());
        }));
    }

    @Test
    public void testCanLoadMultipleSurveys() {
        TestObserver<List<Survey>> testObserver = new TestObserver<>();
        dataSource.deleteCreatedSurveys()
                .andThen(Single.fromCallable(() -> surveyParser.parse(openSurveyFile())))
                .flatMapCompletable(survey -> dataSource.rewriteTemplateSurvey(survey))
                .andThen(dataSource.createSurvey("CHK001", "testCanGetCreatedSurvey", new Date()))
                .flatMap(s -> dataSource.createSurvey("CHK002", "testCanGetCreatedSurveytestCanGetCreatedSurvey", new Date()))
                .flatMap(s -> dataSource.createSurvey("CHK003", "testCanGetCreatedSurveytestCanGetCreatedSurveytestCanGetCreatedSurvey", new Date()))
                .flatMap(s -> dataSource.loadAllSurveys())
                .subscribe(testObserver);
        testObserver.awaitTerminalEvent();
        testObserver.assertValue(TestUtil.check(surveys -> {
            assertEquals(3, surveys.size());
            assertEquals("CHK001", surveys.get(0).getSchoolId());
            assertEquals("CHK002", surveys.get(1).getSchoolId());
            assertEquals("CHK003", surveys.get(2).getSchoolId());
        }));
    }

    @Test
    public void testCanClearDynamicData() {
        TestObserver<List<Survey>> testObserver = new TestObserver<>();

        Date creationDate = new Date();
        Single.fromCallable(() -> surveyParser.parse(openSurveyFile()))
                .flatMapCompletable(survey -> dataSource.rewriteTemplateSurvey(survey))
                .andThen(dataSource.createSurvey("CHK001", "testCanClearDynamicData", creationDate))
                .flatMapCompletable(s -> dataSource.deleteCreatedSurveys())
                .andThen(dataSource.loadAllSurveys())
                .subscribe(testObserver);
        testObserver.awaitTerminalEvent();
        testObserver.assertValue(TestUtil.check(mutableSurveys -> assertTrue(mutableSurveys.isEmpty())));
    }

    @Test
    public void testCanDeleteSurvey() {
        TestObserver<List<Survey>> testObserver = new TestObserver<>();
        dataSource.deleteCreatedSurveys()
                .andThen(Single.fromCallable(() -> surveyParser.parse(openSurveyFile())))
                .flatMapCompletable(survey -> dataSource.rewriteTemplateSurvey(survey))
                .andThen(dataSource.createSurvey("CHK001", "testCanGetCreatedSurvey", new Date()))
                .flatMap(s -> dataSource.createSurvey("CHK002", "testCanGetCreatedSurveytestCanGetCreatedSurvey", new Date()))
                .flatMap(s -> dataSource.createSurvey("CHK003", "testCanGetCreatedSurveytestCanGetCreatedSurveytestCanGetCreatedSurvey", new Date()))
                .flatMapCompletable(s -> dataSource.deleteSurvey(s.getId()))
                .andThen(dataSource.loadAllSurveys())
                .subscribe(testObserver);
        testObserver.awaitTerminalEvent();
        testObserver.assertValue(TestUtil.check(surveys -> {
            assertEquals(2, surveys.size());
            assertEquals("CHK001", surveys.get(0).getSchoolId());
            assertEquals("CHK002", surveys.get(1).getSchoolId());
        }));
    }

    @Test
    public void testCanAnswer() {
        TestObserver<Answer> testObserver = new TestObserver<>();
        dataSource.deleteCreatedSurveys()
                .andThen(Single.fromCallable(() -> surveyParser.parse(openSurveyFile())))
                .flatMapCompletable(survey -> dataSource.rewriteTemplateSurvey(survey))
                .andThen(dataSource.createSurvey("CHK001", "testCanAnswer", new Date()))
                .flatMap(survey -> {
                    SubCriteria subCriteria = getTestSubCriteria(survey);
                    assertNotNull(subCriteria.getAnswer());
                    assertEquals(AnswerState.NOT_ANSWERED, subCriteria.getAnswer().getState());
                    MutableAnswer answer = new MutableAnswer();
                    answer.setState(AnswerState.POSITIVE);
                    answer.setComment("Comment");
                    return dataSource.updateAnswer(answer, subCriteria.getId());
                })
                .subscribe(testObserver);
        testObserver.awaitTerminalEvent();
        testObserver.assertValue(TestUtil.check(answer -> {
            assertEquals(AnswerState.POSITIVE, answer.getState());
            assertEquals("Comment", answer.getComment());
            assertTrue(answer.getPhotos() == null || answer.getPhotos().isEmpty());
        }));
    }

    private SubCriteria getTestSubCriteria(Survey survey) {
        return survey
                .getCategories().get(0)
                .getStandards().get(0)
                .getCriterias().get(0)
                .getSubCriterias().get(0);
    }

    @Test
    public void testCanCreateAndDeletePhoto() {
        TestObserver<Survey> testObserver = new TestObserver<>();
        dataSource.deleteCreatedSurveys()
                .andThen(Single.fromCallable(() -> surveyParser.parse(openSurveyFile())))
                .flatMapCompletable(survey -> dataSource.rewriteTemplateSurvey(survey))
                .andThen(dataSource.createSurvey("CHK001", "testCanCreatePhoto", new Date()))
                .flatMap(survey -> {
                    SubCriteria subCriteria = getTestSubCriteria(survey);
                    MutablePhoto photo = new MutablePhoto();
                    photo.setLocalPath("/local/path.jpg");
                    photo.setRemotePath("https://local/path.jpg");
                    return dataSource.createPhoto(photo, subCriteria.getAnswer().getId())
                            .flatMap(createdPhoto -> {
                                MutablePhoto photo2 = new MutablePhoto();
                                photo2.setLocalPath("/local/path2.jpg");
                                photo2.setRemotePath("https://local/path2.jpg");
                                return dataSource.createPhoto(photo2, subCriteria.getAnswer().getId())
                                        .flatMap(createdPhoto2 -> {
                                            MutablePhoto photo3 = new MutablePhoto();
                                            photo3.setLocalPath("/local/path3.jpg");
                                            photo3.setRemotePath("https://local/path3.jpg");
                                            return dataSource.createPhoto(photo3, subCriteria.getAnswer().getId())
                                                    .flatMap(p -> dataSource.loadSurvey(survey.getId()));
                                        });
                            });
                })
                .flatMap(survey -> {
                    SubCriteria subCriteria = getTestSubCriteria(survey);
                    Photo photo = subCriteria.getAnswer().getPhotos().get(0);
                    return dataSource.deletePhoto(photo.getId())
                            .andThen(dataSource.loadSurvey(survey.getId()));
                })
                .subscribe(testObserver);
        testObserver.awaitTerminalEvent();
        testObserver.assertValue(TestUtil.check(survey -> {
            SubCriteria subCriteria = getTestSubCriteria(survey);
            assertNotNull(subCriteria.getAnswer());
            assertEquals(AnswerState.NOT_ANSWERED, subCriteria.getAnswer().getState());
            assertNotNull(subCriteria.getAnswer().getPhotos());
            assertEquals(2, subCriteria.getAnswer().getPhotos().size());
            assertEquals("/local/path2.jpg", subCriteria.getAnswer().getPhotos().get(0).getLocalPath());
            assertEquals("https://local/path2.jpg", subCriteria.getAnswer().getPhotos().get(0).getRemotePath());
            assertEquals("/local/path3.jpg", subCriteria.getAnswer().getPhotos().get(1).getLocalPath());
            assertEquals("https://local/path3.jpg", subCriteria.getAnswer().getPhotos().get(1).getRemotePath());
        }));
    }

}
