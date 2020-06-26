package org.pacific_emis.surveys.wash_core.data.serialization.serializers;

import org.simpleframework.xml.core.Persister;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import org.pacific_emis.surveys.core.data.exceptions.ParseException;
import org.pacific_emis.surveys.core.data.serialization.SurveyParser;
import org.pacific_emis.surveys.core.utils.StreamUtils;
import org.pacific_emis.surveys.wash_core.data.model.WashSurvey;
import org.pacific_emis.surveys.wash_core.data.serialization.model.SerializableWashSurvey;

public class WashSurveyParser implements SurveyParser {

    private final Persister persister;

    public WashSurveyParser(Persister persister) {
        this.persister = persister;
    }

    @Override
    public WashSurvey parse(InputStream dataStream) throws ParseException {
        SerializableWashSurvey serializableSurvey;
        try {
            Reader reader = new StringReader(StreamUtils.asString(dataStream));
            serializableSurvey = persister.read(SerializableWashSurvey.class, reader);
        } catch (Exception ex) {
            throw new ParseException();
        }
        if (serializableSurvey == null) throw new ParseException();
        return serializableSurvey;
    }
}
