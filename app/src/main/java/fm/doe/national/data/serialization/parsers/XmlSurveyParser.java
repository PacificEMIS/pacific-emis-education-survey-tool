package fm.doe.national.data.serialization.parsers;

import org.simpleframework.xml.core.Persister;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.serialization.entities.SerializableSurvey;
import fm.doe.national.utils.StreamUtils;

public class XmlSurveyParser implements Parser<SerializableSurvey> {

    private final Persister serializer = MicronesiaApplication.getAppComponent().getPersister();

    @Override
    public SerializableSurvey parse(InputStream dataStream) throws ParseException {
        SerializableSurvey serializableSurvey;
        try {
            Reader reader = new StringReader(StreamUtils.asString(dataStream));
            serializableSurvey = serializer.read(SerializableSurvey.class, reader);
        } catch (Exception ex) {
            throw new ParseException();
        }
        if (serializableSurvey == null) throw new ParseException();
        return serializableSurvey;
    }
}
