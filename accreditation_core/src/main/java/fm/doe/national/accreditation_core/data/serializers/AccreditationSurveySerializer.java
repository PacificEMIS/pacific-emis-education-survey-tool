package fm.doe.national.accreditation_core.data.serializers;

import android.util.Log;

import org.simpleframework.xml.core.Persister;

import java.io.StringWriter;
import java.io.Writer;

import fm.doe.national.accreditation_core.data.model.AccreditationSurvey;
import fm.doe.national.accreditation_core.data.serialization.entities.SerializableAccreditationSurvey;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.data.serialization.SurveySerializer;

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
