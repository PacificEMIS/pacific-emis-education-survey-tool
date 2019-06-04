package fm.doe.national.wash_core.data.serialization.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import fm.doe.national.core.data.model.Progress;
import fm.doe.national.core.data.model.mutable.MutableProgress;
import fm.doe.national.wash_core.data.model.Question;
import fm.doe.national.wash_core.data.model.SubGroup;

@Root(name = "subgroup")
public class SerializableSubGroup implements SubGroup {

    @Element(name = "id")
    String prefix;

    @Element(name = "name")
    String title;

    @ElementList(inline = true)
    List<SerializableQuestion> questions;

    @NonNull
    @Override
    public String getTitle() {
        return title;
    }

    @NonNull
    @Override
    public String getPrefix() {
        return prefix;
    }

    @Nullable
    @Override
    public List<? extends Question> getQuestions() {
        return questions;
    }

    @Override
    public long getId() {
        return 0;
    }

    @NonNull
    @Override
    public Progress getProgress() {
        return MutableProgress.createEmptyProgress();
    }
}
