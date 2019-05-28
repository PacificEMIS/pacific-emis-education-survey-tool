package fm.doe.national.report_core.model.recommendations;

import com.omega_r.libs.omegatypes.Text;

public abstract class Recommendation<T> {
    private Text content;
    private T object;

    public Recommendation(T object, Text content) {
        this.object = object;
        this.content = content;
    }

    public Text getContent() {
        return content;
    }

    public T getObject() {
        return object;
    }
}
