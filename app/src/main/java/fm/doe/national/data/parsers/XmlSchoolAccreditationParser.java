package fm.doe.national.data.parsers;

import com.tickaroo.tikxml.TikXml;

import java.io.IOException;
import java.io.InputStream;

import fm.doe.national.data.data_source.models.serializable.LinkedSchoolAccreditation;
import fm.doe.national.data.data_source.models.serializable.SerializableSchoolAccreditation;
import okio.BufferedSource;
import okio.Okio;

public class XmlSchoolAccreditationParser implements Parser<LinkedSchoolAccreditation> {

    @Override
    public LinkedSchoolAccreditation parse(InputStream dataStream) {
        LinkedSchoolAccreditation schoolAccreditation = null;
        try {
            TikXml tikXml = new TikXml.Builder()
                    .writeDefaultXmlDeclaration(false)
                    .build();
            BufferedSource bufferedSource = Okio.buffer(Okio.source(dataStream));
            schoolAccreditation = tikXml.read(bufferedSource, SerializableSchoolAccreditation.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return schoolAccreditation;
    }
}
