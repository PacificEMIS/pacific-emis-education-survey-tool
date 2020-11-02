package org.pacific_emis.surveys.accreditation_core.data.serializers;

import org.simpleframework.xml.core.Persister;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import org.pacific_emis.surveys.accreditation_core.data.model.AccreditationSurvey;
import org.pacific_emis.surveys.accreditation_core.data.serialization.entities.SerializableAccreditationSurvey;
import org.pacific_emis.surveys.core.data.exceptions.ParseException;
import org.pacific_emis.surveys.core.data.serialization.SurveyParser;
import org.pacific_emis.surveys.core.utils.StreamUtils;

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
