package org.pacific_emis.surveys.accreditation_core.data.serializers;

import android.util.Log;

import org.simpleframework.xml.core.Persister;

import java.io.StringWriter;
import java.io.Writer;

import org.pacific_emis.surveys.accreditation_core.data.model.AccreditationSurvey;
import org.pacific_emis.surveys.accreditation_core.data.serialization.entities.SerializableAccreditationSurvey;
import org.pacific_emis.surveys.core.data.model.Survey;
import org.pacific_emis.surveys.core.data.serialization.SurveySerializer;

public class AccreditationSurveySerializer implements SurveySerializer {

    private static final String TAG = AccreditationSurveySerializer.class.getName();

    private final Persister persister;

    public AccreditationSurveySerializer(Persister serializer) {
        this.persister = serializer;
    }

    @Override
    public String serialize(Survey data) {
        SerializableAccreditationSurvey serializableSurvey = new SerializableAccreditationSurvey((AccreditationSurvey) data);
        Writer writer = new StringWriter();
        try {
            persister.write(serializableSurvey, writer);
        } catch (Exception ex) {
            Log.e(TAG, "serialize: ", ex );
        }
        return writer.toString();
    }
}
