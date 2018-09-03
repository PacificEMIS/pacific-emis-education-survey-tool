package fm.doe.national.data.serializers;

import android.util.Log;

import org.simpleframework.xml.core.Persister;

import java.io.StringWriter;
import java.io.Writer;

import fm.doe.national.data.data_source.models.serializable.LinkedSchoolAccreditation;
import fm.doe.national.data.data_source.models.serializable.SerializableSchoolAccreditation;

public class XmlSchoolAccreditationSerializer implements Serializer<LinkedSchoolAccreditation> {
    private static final String TAG = XmlSchoolAccreditationSerializer.class.getName();

    @Override
    public String serialize(LinkedSchoolAccreditation data) {
        SerializableSchoolAccreditation serializableSchoolAccreditation = new SerializableSchoolAccreditation(data);
        Writer writer = new StringWriter();
        org.simpleframework.xml.Serializer serializer = new Persister();
        try {
            serializer.write(serializableSchoolAccreditation, writer);
        } catch (Exception ex) {
            Log.e(TAG, "serialize: ", ex );
        }
        return writer.toString();
    }
}
