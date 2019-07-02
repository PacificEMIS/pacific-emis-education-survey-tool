package fm.doe.national.wash_core.data.model.mutable;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.core.data.model.BaseSerializableIdentifiedObject;
import fm.doe.national.core.data.model.mutable.BaseMutableEntity;
import fm.doe.national.core.data.model.mutable.MutablePhoto;
import fm.doe.national.core.utils.CollectionUtils;
import fm.doe.national.wash_core.data.model.Answer;
import fm.doe.national.wash_core.data.model.BinaryAnswerState;
import fm.doe.national.wash_core.data.model.Location;
import fm.doe.national.wash_core.data.model.TernaryAnswerState;
import fm.doe.national.wash_core.data.model.Variant;

public class MutableAnswer extends BaseMutableEntity implements Answer {

    @Nullable
    private List<String> items;

    @Nullable
    private String comment;

    @Nullable
    private String inputText;

    @Nullable
    private List<Variant> variants;

    @Nullable
    private Location location;

    @Nullable
    List<MutablePhoto> photos;

    @Nullable
    private BinaryAnswerState binaryAnswerState;

    @Nullable
    private TernaryAnswerState ternaryAnswerState;

    public MutableAnswer() {
        // required for generation
    }

    public MutableAnswer(Answer other) {
        this.items = other.getItems();
        this.comment = other.getComment();
        this.variants = other.getVariants();
        this.location = other.getLocation();
        this.binaryAnswerState = other.getBinaryAnswerState();
        this.ternaryAnswerState = other.getTernaryAnswerState();
        this.id = other.getId();
        this.inputText = other.getInputText();

        if (other.getPhotos() != null) {
            this.photos = other.getPhotos().stream().map(MutablePhoto::new).collect(Collectors.toList());
        }
    }

    @Nullable
    @Override
    public List<String> getItems() {
        return items;
    }

    @Nullable
    @Override
    public String getComment() {
        return comment;
    }

    @Nullable
    @Override
    public List<Variant> getVariants() {
        return variants;
    }

    @Nullable
    @Override
    public Location getLocation() {
        return location;
    }

    @Nullable
    @Override
    public List<MutablePhoto> getPhotos() {
        return photos;
    }

    @Nullable
    @Override
    public BinaryAnswerState getBinaryAnswerState() {
        return binaryAnswerState;
    }

    @Nullable
    @Override
    public TernaryAnswerState getTernaryAnswerState() {
        return ternaryAnswerState;
    }

    @Nullable
    @Override
    public String getInputText() {
        return inputText;
    }

    public void setItems(@Nullable List<String> items) {
        this.items = CollectionUtils.isEmpty(items) ? null : items;
    }

    public void setComment(@Nullable String comment) {
        this.comment = comment;
    }

    public void setVariants(@Nullable List<Variant> variants) {
        this.variants = variants;
    }

    public void setLocation(@Nullable Location location) {
        this.location = location;
    }

    public void setPhotos(@Nullable List<MutablePhoto> photos) {
        this.photos = photos;
    }

    public void setBinaryAnswerState(@Nullable BinaryAnswerState binaryAnswerState) {
        this.binaryAnswerState = binaryAnswerState;
    }

    public void setTernaryAnswerState(@Nullable TernaryAnswerState ternaryAnswerState) {
        this.ternaryAnswerState = ternaryAnswerState;
    }

    public void setInputText(@Nullable String inputText) {
        this.inputText = TextUtils.isEmpty(inputText) ? null : inputText;
    }

    public MutableAnswer merge(Answer other) {
        boolean haveChanges =
                mergeItems(other) ||
                mergeInputText(other) ||
                mergeVariants(other) ||
                mergeLocation(other) ||
                mergeBinaryAnswer(other) ||
                mergeTernaryAnswer(other);

        String externalComment = other.getComment();
        if (externalComment != null) {
            comment = (comment == null ? "" : (comment + "/n")) + externalComment;
            haveChanges = true;
        }

        if (other.getPhotos() != null) {
            List<MutablePhoto> otherUniquePhotos = other.getPhotos().stream()
                    .map(MutablePhoto::new)
                    .filter(mutablePhoto -> this.photos.stream().noneMatch(existing -> existing.isDataEquals(mutablePhoto)))
                    .peek(photo -> photo.setId(BaseSerializableIdentifiedObject.DEFAULT_ID))
                    .collect(Collectors.toList());

            if (!otherUniquePhotos.isEmpty()) {
                if (this.photos == null) {
                    this.photos = new ArrayList<>();
                }

                this.photos.addAll(otherUniquePhotos);
                haveChanges = true;
            }
        }

        return haveChanges ? this : null;
    }

    private boolean mergeItems(Answer answer) {
        if (CollectionUtils.isEmpty(this.items) && !CollectionUtils.isEmpty(answer.getItems())) {
            this.items = answer.getItems();
            return true;
        }

        return false;
    }

    private boolean mergeInputText(Answer answer) {
        if (TextUtils.isEmpty(this.inputText) && !TextUtils.isEmpty(answer.getInputText())) {
            this.inputText = answer.getInputText();
            return true;
        }

        return false;
    }

    private boolean mergeVariants(Answer answer) {
        if (CollectionUtils.isEmpty(this.variants) && !CollectionUtils.isEmpty(answer.getVariants())) {
            this.variants = answer.getVariants();
            return true;
        }

        return false;
    }

    private boolean mergeLocation(Answer answer) {
        if (this.location == null && answer.getLocation() != null) {
            this.location = answer.getLocation();
            return true;
        }

        return false;
    }

    private boolean mergeBinaryAnswer(Answer answer) {
        if (this.binaryAnswerState == null && answer.getBinaryAnswerState() != null) {
            this.binaryAnswerState = answer.getBinaryAnswerState();
            return true;
        }

        return false;
    }

    private boolean mergeTernaryAnswer(Answer answer) {
        if (this.ternaryAnswerState == null && answer.getTernaryAnswerState() != null) {
            this.ternaryAnswerState = answer.getTernaryAnswerState();
            return true;
        }

        return false;
    }
}
