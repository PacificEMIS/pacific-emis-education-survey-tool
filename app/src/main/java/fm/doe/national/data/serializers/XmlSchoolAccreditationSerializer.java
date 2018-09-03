package fm.doe.national.data.serializers;

import android.util.Log;

import org.simpleframework.xml.core.Persister;

import java.io.StringWriter;
import java.io.Writer;

import fm.doe.national.data.data_source.models.serializable.LinkedSchoolAccreditation;
import fm.doe.national.data.data_source.models.serializable.SerializableSchoolAccreditation;

public class XmlSchoolAccreditationSerializer implements Serializer<LinkedSchoolAccreditation> {
    @Override
    public String serialize(LinkedSchoolAccreditation data) {
        SerializableSchoolAccreditation serializableSchoolAccreditation = new SerializableSchoolAccreditation(data);
        Writer writer = new StringWriter();
        org.simpleframework.xml.Serializer serializer = new Persister();
        try {
            serializer.write(serializableSchoolAccreditation, writer);
            String xml = writer.toString();
        } catch (Exception ex) {
            Log.e("ERROR", "serialize: ", ex );
        }
        return writer.toString();
    }
}
