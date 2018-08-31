package fm.doe.national.data.serializers;

import com.tickaroo.tikxml.TikXml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import fm.doe.national.data.data_source.models.serializable.LinkedSchoolAccreditation;
import fm.doe.national.data.data_source.models.serializable.SerializableSchoolAccreditation;
import okio.Okio;

public class XmlSchoolAccreditationSerializer implements Serializer<LinkedSchoolAccreditation> {
    @Override
    public String serialize(LinkedSchoolAccreditation data) {
        SerializableSchoolAccreditation serializableSchoolAccreditation = new SerializableSchoolAccreditation(data);
        TikXml tikXml = new TikXml.Builder().build();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String serializedAccreditation = null;
        try {
            tikXml.write(Okio.buffer(Okio.sink(outputStream)), serializableSchoolAccreditation);
            serializedAccreditation = outputStream.toString();
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serializedAccreditation;
    }
}
