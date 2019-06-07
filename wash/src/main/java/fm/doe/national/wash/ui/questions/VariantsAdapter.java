package fm.doe.national.wash.ui.questions;

import android.util.SparseArray;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;
import com.omega_r.libs.omegatypes.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import fm.doe.national.core.utils.CollectionUtils;
import fm.doe.national.wash.R;
import fm.doe.national.wash_core.data.model.BinaryAnswerState;
import fm.doe.national.wash_core.data.model.Variant;
import fm.doe.national.wash_core.data.model.VariantItem;
import fm.doe.national.wash_core.data.model.mutable.MutableAnswer;
import fm.doe.national.wash_core.data.model.mutable.MutableQuestion;

public class VariantsAdapter extends BaseListAdapter<Variant> {

    private static final int VIEW_TYPE_DEFAULT = 0;
    private static final int VIEW_TYPE_NONE_SELECTOR = 1;

    private final Type type;
    private final MutableQuestion question;
    private final OnAnswerChangeListener listener;

    public VariantsAdapter(Type type, MutableQuestion question, OnAnswerChangeListener listener) {
        this.type = type;
        this.question = question;
        this.listener = listener;

        if (question.getVariants() != null) {
            setItems(question.getVariants());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (CollectionUtils.isEmpty(getItem(position).getOptions())) {
            return VIEW_TYPE_NONE_SELECTOR;
        } else {
            return VIEW_TYPE_DEFAULT;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_DEFAULT:
                switch (type) {
                    case BINARY:
                        return new BinaryItemViewHolder(parent);
                    case NUMERIC:
                        return new NumericItemViewHolder(parent);
                }
                throw new IllegalStateException();
            case VIEW_TYPE_NONE_SELECTOR:
                return new NoneSelectorViewHolder(parent);
        }
        throw new IllegalStateException();
    }

    private void notifyQuestionChanged() {
        listener.onAnswerChange();
    }

    @Override
    protected ViewHolder provideViewHolder(ViewGroup parent) {
        return new ItemViewHolder(parent);
    }

    class NumericItemViewHolder extends ItemViewHolder {
        public NumericItemViewHolder(ViewGroup parent) {
            super(parent);
        }
    }

    class BinaryItemViewHolder extends ItemViewHolder implements ComplexBinaryAnswerAdapter.OnBinaryAnswerChangeListener {

        private final ComplexBinaryAnswerAdapter adapter = new ComplexBinaryAnswerAdapter(this);

        public BinaryItemViewHolder(ViewGroup parent) {
            super(parent);
            recyclerView.setAdapter(adapter);
        }

        @Override
        protected void onBind(Variant item) {
            super.onBind(item);
            adapter.setItems(item.getOptions().stream().map(VariantItem::getName).collect(Collectors.toList()));
            adapter.setAnswerStates(getAnswerStates());
        }

        private SparseArray<BinaryAnswerState> getAnswerStates() {
            SparseArray<BinaryAnswerState> states = new SparseArray<>();
            MutableAnswer answer = question.getAnswer();
            List<VariantItem> questionVariantOptions = getItem().getOptions();

            if (answer == null || CollectionUtils.isEmpty(questionVariantOptions)) {
                return states;
            }

            List<Variant> existingAnswerVariants = answer.getVariants();

            if (existingAnswerVariants == null) {
                return states;
            }

            Optional<Variant> currentAnswerVariantOp = existingAnswerVariants.stream()
                    .filter(v -> v.getName().equals(getItem().getName()))
                    .findFirst();

            if (!currentAnswerVariantOp.isPresent()) {
                return states;
            }

            Variant currentAnswerVariant = currentAnswerVariantOp.get();

            for (int position = 0; position < questionVariantOptions.size(); position++) {
                for (VariantItem option : currentAnswerVariant.getOptions()) {
                    if (questionVariantOptions.get(position).getName().equals(option.getName())) {
                        states.put(position, BinaryAnswerState.createFromText(getContext(), Text.from(option.getAnswer())));
                    }
                }
            }

            return states;
        }

        @Override
        public void onBinaryAnswerChange(int atPosition, @Nullable BinaryAnswerState binaryAnswerState) {
            MutableAnswer answer = question.getAnswer();

            if (answer == null) {
                return;
            }

            List<Variant> answerVariants = answer.getVariants();
            Variant variantToChange = getItem();
            List<VariantItem> allVariantItems = variantToChange.getOptions();
            VariantItem itemToChange = VariantItem.copy(allVariantItems.get(atPosition));

            // Question not answered at all
            if (answerVariants == null) {
                Variant newVariant = createNewVariant(binaryAnswerState, variantToChange, itemToChange);
                answerVariants = new ArrayList<>();
                answerVariants.add(newVariant);
            } else {
                ArrayList<Variant> wrappedAnswerVariants = new ArrayList<>(answerVariants);
                Optional<Variant> existingAnsweredVariantOptional = wrappedAnswerVariants.stream().filter(variantToChange::equals).findFirst();

                if (!existingAnsweredVariantOptional.isPresent()) {
                    Variant newVariant = createNewVariant(binaryAnswerState, variantToChange, itemToChange);
                    wrappedAnswerVariants.add(newVariant);
                } else {
                    Variant existingAnsweredVariant = existingAnsweredVariantOptional.get();

                    // remove existent one to insert it modified state later
                    wrappedAnswerVariants.remove(existingAnsweredVariant);

                    if (binaryAnswerState == null) {
                        existingAnsweredVariant.getOptions().remove(itemToChange);

                        // don't add variant back if it's empty
                        if (!existingAnsweredVariant.getOptions().isEmpty()) {
                            wrappedAnswerVariants.add(existingAnsweredVariant);
                        }
                    } else {
                        List<VariantItem> answeredOptions = existingAnsweredVariant.getOptions();
                        itemToChange.setAnswer(getBinaryAnswerStateAnswer(binaryAnswerState));
                        answeredOptions.remove(itemToChange); // If already exists - remove
                        answeredOptions.add(itemToChange);
                        wrappedAnswerVariants.add(existingAnsweredVariant);
                    }
                }

                answerVariants = wrappedAnswerVariants;
            }

            answer.setVariants(answerVariants);
            notifyQuestionChanged();
        }

        private Variant createNewVariant(@Nullable BinaryAnswerState binaryAnswerState, Variant variantToChange, VariantItem itemToChange) {
            if (binaryAnswerState != null) {
                itemToChange.setAnswer(getBinaryAnswerStateAnswer(binaryAnswerState));
            }

            Variant newVariant = Variant.copy(variantToChange);
            newVariant.getOptions().clear();
            newVariant.getOptions().add(itemToChange);
            return newVariant;
        }

        private String getBinaryAnswerStateAnswer(BinaryAnswerState binaryAnswerState) {
            return binaryAnswerState.getText().getString(getContext());
        }
    }

    class NoneSelectorViewHolder extends ItemViewHolder {
        public NoneSelectorViewHolder(ViewGroup parent) {
            super(parent);
        }
    }

    class ItemViewHolder extends ViewHolder {

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        private TextView titleTextView = findViewById(R.id.textview_title);

        public ItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_variant);
        }

        @Override
        protected void onBind(Variant item) {
            titleTextView.setText(item.getName());
        }
    }

    public enum Type {
        BINARY, NUMERIC
    }

    public interface OnAnswerChangeListener {
        void onAnswerChange();
    }
}
