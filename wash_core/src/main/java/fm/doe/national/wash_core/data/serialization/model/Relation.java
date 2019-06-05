package fm.doe.national.wash_core.data.serialization.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

@Root(name = "relation")
public class Relation implements Serializable {

    @Element(name = "id")
    String questionId;

    @ElementList(entry = "relationAnswer", inline = true)
    List<String> relationAnswers;

    public String getQuestionId() {
        return questionId;
    }

    public List<String> getRelationAnswers() {
        return relationAnswers;
    }
}
