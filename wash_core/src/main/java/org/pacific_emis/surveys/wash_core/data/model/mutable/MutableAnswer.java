package org.pacific_emis.surveys.wash_core.data.model.mutable;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.pacific_emis.surveys.core.data.exceptions.NotImplementedException;
import org.pacific_emis.surveys.core.data.model.BaseSerializableIdentifiedObject;
import org.pacific_emis.surveys.core.data.model.ConflictResolveStrategy;
import org.pacific_emis.surveys.core.data.model.Photo;
import org.pacific_emis.surveys.core.data.model.mutable.BaseMutableEntity;
import org.pacific_emis.surveys.core.data.model.mutable.MutablePhoto;
import org.pacific_emis.surveys.core.utils.CollectionUtils;
import org.pacific_emis.surveys.wash_core.data.model.Answer;
import org.pacific_emis.surveys.wash_core.data.model.BinaryAnswerState;
import org.pacific_emis.surveys.wash_core.data.model.Location;
import org.pacific_emis.surveys.wash_core.data.model.TernaryAnswerState;
import org.pacific_emis.surveys.wash_core.data.model.Variant;

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

    public MutableAnswer merge(Answer other, ConflictResolveStrategy strategy, boolean isOtherAnswered) {
        if (strategy != ConflictResolveStrategy.THEIRS) {
            // MINE is not supported
            throw new NotImplementedException();
        }

        String otherComment = other.getComment();
        List<? extends Photo> otherPhotos = other.getPhotos();
        boolean haveChanges = isOtherAnswered ||
                !CollectionUtils.isEmpty(other.getPhotos()) ||
                !TextUtils.isEmpty(otherComment);

        if (!haveChanges) {
            return null;
        }

        mergeItemsTheirs(other);
        mergeInputTextTheirs(other);
        mergeVariantsTheirs(other);
        mergeLocationTheirs(other);
        mergeBinaryAnswerTheirs(other);
        mergeTernaryAnswerTheirs(other);

        comment = otherComment;

        if (otherPhotos != null) {
            photos = otherPhotos
                    .stream()
                    .map(MutablePhoto::new)
                    .peek(photo -> photo.setId(BaseSerializableIdentifiedObject.DEFAULT_ID))
                    .collect(Collectors.toList());
        } else {
            photos = null;
        }

        return this;
    }

    private boolean mergeItemsMine(Answer answer) {
        if (CollectionUtils.isEmpty(this.items) && !CollectionUtils.isEmpty(answer.getItems())) {
            this.items = answer.getItems();
            return true;
        }

        return false;
    }

    private boolean mergeInputTextMine(Answer answer) {
        if (TextUtils.isEmpty(this.inputText) && !TextUtils.isEmpty(answer.getInputText())) {
            this.inputText = answer.getInputText();
            return true;
        }

        return false;
    }

    private boolean mergeVariantsMine(Answer answer) {
        if (CollectionUtils.isEmpty(this.variants) && !CollectionUtils.isEmpty(answer.getVariants())) {
            this.variants = answer.getVariants();
            return true;
        }

        return false;
    }

    private boolean mergeLocationMine(Answer answer) {
        if (this.location == null && answer.getLocation() != null) {
            this.location = answer.getLocation();
            return true;
        }

        return false;
    }

    private boolean mergeBinaryAnswerMine(Answer answer) {
        if (this.binaryAnswerState == null && answer.getBinaryAnswerState() != null) {
            this.binaryAnswerState = answer.getBinaryAnswerState();
            return true;
        }

        return false;
    }

    private boolean mergeTernaryAnswerMine(Answer answer) {
        if (this.ternaryAnswerState == null && answer.getTernaryAnswerState() != null) {
            this.ternaryAnswerState = answer.getTernaryAnswerState();
            return true;
        }

        return false;
    }

    private boolean mergeItemsTheirs(Answer answer) {
        if (Objects.equals(this.items, answer.getItems())) {
            return false;
        }

        this.items = answer.getItems();
        return true;
    }

    private boolean mergeInputTextTheirs(Answer answer) {
        if (Objects.equals(this.inputText, answer.getInputText())) {
            return false;
        }

        this.inputText = answer.getInputText();
        return true;
    }

    private boolean mergeVariantsTheirs(Answer answer) {
        if (Objects.equals(this.variants, answer.getVariants())) {
            return false;
        }

        this.variants = answer.getVariants();
        return true;
    }

    private boolean mergeLocationTheirs(Answer answer) {
        if (Objects.equals(this.location, answer.getLocation())) {
            return false;
        }

        this.location = answer.getLocation();
        return true;
    }

    private boolean mergeBinaryAnswerTheirs(Answer answer) {
        if (Objects.equals(this.binaryAnswerState, answer.getBinaryAnswerState())) {
            return false;
        }

        this.binaryAnswerState = answer.getBinaryAnswerState();
        return true;
    }

    private boolean mergeTernaryAnswerTheirs(Answer answer) {
        if (Objects.equals(this.ternaryAnswerState, answer.getTernaryAnswerState())) {
            return false;
        }

        this.ternaryAnswerState = answer.getTernaryAnswerState();
        return true;
    }

    public void clear() {
        items = null;
        comment = null;
        inputText = null;
        variants = null;
        location = null;
        photos = null;
        binaryAnswerState = null;
        ternaryAnswerState = null;
    }
}
