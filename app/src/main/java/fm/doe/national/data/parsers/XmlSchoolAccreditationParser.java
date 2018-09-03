package fm.doe.national.data.parsers;

import android.util.Log;

import org.simpleframework.xml.core.Persister;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import fm.doe.national.data.data_source.models.serializable.LinkedSchoolAccreditation;
import fm.doe.national.data.data_source.models.serializable.SerializableSchoolAccreditation;
import fm.doe.national.utils.StreamUtils;

public class XmlSchoolAccreditationParser implements Parser<LinkedSchoolAccreditation> {
    private static final String TAG = XmlSchoolAccreditationParser.class.getName();

    @Override
    public LinkedSchoolAccreditation parse(InputStream dataStream) {
        LinkedSchoolAccreditation schoolAccreditation = null;
        try {
            Reader reader = new StringReader(StreamUtils.asString(dataStream));
            Persister serializer = new Persister();
            schoolAccreditation = serializer.read(SerializableSchoolAccreditation.class, reader);
        } catch (Exception ex) {
            Log.e(TAG, "parse: ", ex);
        }
        return schoolAccreditation;
    }
}
