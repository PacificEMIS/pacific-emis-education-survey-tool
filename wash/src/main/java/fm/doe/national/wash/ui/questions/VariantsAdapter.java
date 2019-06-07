package fm.doe.national.wash.ui.questions;

import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;

import fm.doe.national.wash.R;
import fm.doe.national.wash_core.data.model.BinaryAnswerState;
import fm.doe.national.wash_core.data.model.Variant;
import fm.doe.national.wash_core.data.model.mutable.MutableQuestion;

public class VariantsAdapter extends BaseListAdapter<Variant> {

    private final Type type;
    private final MutableQuestion question;
    private final OnVarinatChangeListener listener;

    public VariantsAdapter(Type type, MutableQuestion question, OnVarinatChangeListener listener) {
        this.type = type;
        this.question = question;
        this.listener = listener;
    }

    @Override
    protected ViewHolder provideViewHolder(ViewGroup parent) {
        return new ItemViewHolder(parent);
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
//            adapter.setItems(item.getOptions());
//            adapter.setAnswerStates(getAnswerStates());
        }

//        private SparseArray<BinaryAnswerState> getAnswerStates() {
//            SparseArray<BinaryAnswerState> states = new SparseArray<>();
//
//            MutableAnswer answer = question.getAnswer();
//            List<String> questionVariantOptions = getItem().getOptions();
//
//            if (answer == null || CollectionUtils.isEmpty(questionVariantOptions)) {
//                return states;
//            }
//
//            List<Variant> existingAnswerVariants = answer.getVariants();
//
//            if (existingAnswerVariants == null) {
//                return states;
//            }
//
//            Optional<Variant> currentAnswerVariantOp = existingAnswerVariants.stream()
//                    .filter(v -> v.getName().equals(getItem().getName()))
//                    .findFirst();
//
//            if (!currentAnswerVariantOp.isPresent()) {
//                return states;
//            }
//
//            Variant currentAnswerVariant = currentAnswerVariantOp.get();
//
//            for (int position = 0; position < questionVariantOptions.size(); position++) {
//                for (String option : currentAnswerVariant.getOptions()) {
//                    if (questionVariantOptions.get(position).equals(option)) {
//                        states.put(position, BinaryAnswerState.createFromText(getContext(), Text.from()));
//                    }
//                }
//
//            }
//
//
//            for (Variant variant : existingAnswerVariants) {
//                if (CollectionUtils.isEmpty(variant.getOptions())) {
//                    continue;
//                }
//
//                states.put();
//
//                variant.getOptions().get(0)
//            }
//        }

        @Override
        public void onBinaryAnswerChange(int atPosition, @Nullable BinaryAnswerState binaryAnswerState) {

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

    public interface OnVarinatChangeListener {
        void onVariantChange(Variant variant);
    }
}
