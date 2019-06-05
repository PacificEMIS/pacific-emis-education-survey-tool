package fm.doe.national.wash_core.data.serialization.serializers;

import android.util.Log;

import org.simpleframework.xml.core.Persister;

import java.io.StringWriter;
import java.io.Writer;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.data.serialization.SurveySerializer;
import fm.doe.national.wash_core.data.model.WashSurvey;
import fm.doe.national.wash_core.data.serialization.model.SerializableWashSurvey;

public class WashSurveySerializer implements SurveySerializer {

    private static final String TAG = WashSurveySerializer.class.getName();

    private final Persister persister;

    public WashSurveySerializer(Persister serializer) {
        this.persister = serializer;
    }

    @Override
    public String serialize(Survey data) {
        // TODO: not finished
        SerializableWashSurvey serializableSurvey = new SerializableWashSurvey((WashSurvey) data);
        Writer writer = new StringWriter();
        try {
            persister.write(serializableSurvey, writer);
        } catch (Exception ex) {
            Log.e(TAG, "serialize: ", ex );
        }
        return writer.toString();
    }
}
