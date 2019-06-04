package fm.doe.national.wash_core.data.serialization.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "relation")
public class Relation {

    @Element(name = "id")
    String questionId;

    @ElementList(inline = true)
    List<RelationAnswer> relationAnswers;

    public String getQuestionId() {
        return questionId;
    }

    public List<RelationAnswer> getRelationAnswers() {
        return relationAnswers;
    }
}
