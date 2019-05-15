package fm.doe.national;

import androidx.annotation.Nullable;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;

import fm.doe.national.data.model.Category;
import fm.doe.national.data.model.Criteria;
import fm.doe.national.data.model.Standard;
import fm.doe.national.data.model.SurveyType;
import fm.doe.national.data.model.mutable.MutableCategory;
import fm.doe.national.data.model.mutable.MutableCriteria;
import fm.doe.national.data.model.mutable.MutableStandard;
import fm.doe.national.data.model.mutable.MutableSurvey;
import fm.doe.national.data.serialization.entities.SerializableSurvey;
import fm.doe.national.data.serialization.parsers.Parser;
import fm.doe.national.data.serialization.parsers.XmlSurveyParser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class SerializationTests {

    private static final XmlSurveyParser PARSER = new XmlSurveyParser();

    @Nullable
    private InputStream openSurveyFile() {
        ClassLoader classLoader = this.getClass().getClassLoader();
        if (classLoader == null) {
            return null;
        }
        return classLoader.getResourceAsStream("survey.xml");
    }

    @Test
    public void testOpenFile() {
        assertNotNull(openSurveyFile());
    }

    @Test
    public void testParseNewSurvey() {
        SerializableSurvey survey;
        try {
            survey = new SerializableSurvey(PARSER.parse(openSurveyFile()));
            assertNotNull(survey);
        } catch (Parser.ParseException exception) {
            fail();
            return;
        }

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
    }

    @Test
    public void testSurveyInMutableState() {
        SerializableSurvey survey;
        try {
            survey = new SerializableSurvey(PARSER.parse(openSurveyFile()));
            assertNotNull(survey);
        } catch (Parser.ParseException exception) {
            fail();
            return;
        }
        MutableSurvey mutableSurvey = new MutableSurvey(survey);

        assertEquals(SurveyType.SCHOOL_ACCREDITATION, mutableSurvey.getSurveyType());
        assertEquals(1, mutableSurvey.getVersion());
        assertEquals(4, mutableSurvey.getCategories().size());
        MutableCategory category = mutableSurvey.getCategories().get(3);
        assertEquals("Classroom Observation", category.getTitle());
        assertEquals(5, category.getStandards().size());
        MutableStandard standard = category.getStandards().get(2);
        assertEquals("CO3", standard.getSuffix());
        assertEquals("Teaching and learning", standard.getTitle());
        assertEquals(4, standard.getCriterias().size());
        MutableCriteria criteria = standard.getCriterias().get(0);
        assertEquals("CO3.1", criteria.getSuffix());
        assertFalse(criteria.getSubCriterias().isEmpty());
    }
}
