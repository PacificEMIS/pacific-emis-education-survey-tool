package org.pacific_emis.surveys.wash_core.data.serialization.serializers;

import android.util.Log;

import org.simpleframework.xml.core.Persister;

import java.io.StringWriter;
import java.io.Writer;

import org.pacific_emis.surveys.core.data.model.Survey;
import org.pacific_emis.surveys.core.data.serialization.SurveySerializer;
import org.pacific_emis.surveys.wash_core.data.model.WashSurvey;
import org.pacific_emis.surveys.wash_core.data.serialization.model.SerializableWashSurvey;

public class WashSurveySerializer implements SurveySerializer {

    private static final String TAG = WashSurveySerializer.class.getName();

    private final Persister persister;

    public WashSurveySerializer(Persister serializer) {
        this.persister = serializer;
    }

    @Override
    public String serialize(Survey data) {
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
