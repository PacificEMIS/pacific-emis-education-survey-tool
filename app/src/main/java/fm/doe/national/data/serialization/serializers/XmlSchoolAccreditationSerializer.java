package fm.doe.national.data.serialization.serializers;

import android.util.Log;

import java.io.StringWriter;
import java.io.Writer;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.data_source.models.serializable.LinkedSchoolAccreditation;
import fm.doe.national.data.data_source.models.serializable.SerializableSchoolAccreditation;

public class XmlSchoolAccreditationSerializer implements Serializer<LinkedSchoolAccreditation> {
    private static final String TAG = XmlSchoolAccreditationSerializer.class.getName();
    private final org.simpleframework.xml.Serializer serializer = MicronesiaApplication.getAppComponent().getPersister();

    @Override
    public String serialize(LinkedSchoolAccreditation data) {
        SerializableSchoolAccreditation serializableSchoolAccreditation = new SerializableSchoolAccreditation(data);
        Writer writer = new StringWriter();
        try {
            serializer.write(serializableSchoolAccreditation, writer);
        } catch (Exception ex) {
            Log.e(TAG, "serialize: ", ex );
        }
        return writer.toString();
    }
}
