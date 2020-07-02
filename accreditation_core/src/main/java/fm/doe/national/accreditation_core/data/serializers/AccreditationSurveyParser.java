package fm.doe.national.accreditation_core.data.serializers;

import org.simpleframework.xml.core.Persister;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import fm.doe.national.accreditation_core.data.model.AccreditationSurvey;
import fm.doe.national.accreditation_core.data.serialization.entities.SerializableAccreditationSurvey;
import fm.doe.national.core.data.exceptions.ParseException;
import fm.doe.national.core.data.serialization.SurveyParser;
import fm.doe.national.core.utils.StreamUtils;

public class AccreditationSurveyParser implements SurveyParser {

    private final Persister persister;

    public AccreditationSurveyParser(Persister persister) {
        this.persister = persister;
    }

    @Override
    public AccreditationSurvey parse(InputStream dataStream) throws ParseException {
        SerializableAccreditationSurvey serializableSurvey;
        try {
            Reader reader = new StringReader(StreamUtils.asString(dataStream));
            serializableSurvey = persister.read(SerializableAccreditationSurvey.class, reader);
        } catch (Exception ex) {
            throw new ParseException();
        }
        if (serializableSurvey == null) throw new ParseException();
        return serializableSurvey;
    }
}
