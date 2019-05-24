package fm.doe.national.core.data.serialization.serializers;

import android.util.Log;

import java.io.StringWriter;
import java.io.Writer;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.data.serialization.entities.SerializableSurvey;

public class XmlSurveySerializer implements Serializer<Survey> {

    private static final String TAG = XmlSurveySerializer.class.getName();

    private final org.simpleframework.xml.Serializer serializer;

    public XmlSurveySerializer(org.simpleframework.xml.Serializer serializer) {
        this.serializer = serializer;
    }

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
