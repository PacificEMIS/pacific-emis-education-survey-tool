package fm.doe.national.data.serialization.serializers;

import android.util.Log;

import java.io.StringWriter;
import java.io.Writer;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.model.Survey;
import fm.doe.national.data.serialization.entities.SerializableSurvey;

public class XmlSurveySerializer implements Serializer<Survey> {
    private static final String TAG = XmlSurveySerializer.class.getName();
    private final org.simpleframework.xml.Serializer serializer = MicronesiaApplication.getAppComponent().getPersister();

    @Override
    public String serialize(Survey data) {
        SerializableSurvey serializableSurvey = new SerializableSurvey(data);
        Writer writer = new StringWriter();
        try {
            serializer.write(serializableSurvey, writer);
        } catch (Exception ex) {
            Log.e(TAG, "serialize: ", ex );
        }
        return writer.toString();
    }
}
