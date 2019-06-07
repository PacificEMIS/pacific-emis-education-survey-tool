package fm.doe.national.wash.ui.questions;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;

import java.util.stream.Collectors;

import fm.doe.national.wash.R;
import fm.doe.national.wash_core.data.model.BinaryAnswerState;
import fm.doe.national.wash_core.data.model.Variant;
import fm.doe.national.wash_core.data.model.VariantItem;
import fm.doe.national.wash_core.data.model.mutable.MutableQuestion;

public class VariantsAdapter extends BaseListAdapter<Variant> {

    private static final int VIEW_TYPE_DEFAULT = 0;
    private static final int VIEW_TYPE_BLOCKER = 1;

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
        return getItem(position).isBlocker() ? VIEW_TYPE_BLOCKER : VIEW_TYPE_DEFAULT;
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
            case VIEW_TYPE_BLOCKER:
                return new BlockerViewHolder(parent);
        }
        throw new IllegalStateException();
    }

    private void notifyQuestionChangedFromDefaultViewHolder() {
        listener.onAnswerChange();

        for (int i = getItemCount() - 1; i >= 0; i--) {
            if (getItem(i).isBlocker()) {
                notifyItemChanged(i);
                break;
            }
        }
    }

    private void notifyQuestionChangedFromBlockerViewHolder() {
        listener.onAnswerChange();
        notifyDataSetChanged();
    }

    @Override
    protected ViewHolder provideViewHolder(ViewGroup parent) {
        return new ItemViewHolder(parent);
    }

    class NumericItemViewHolder extends ItemViewHolder implements ComplexNumericAnswerAdapter.OnNumericAnswerChangeListener {

        private final ComplexNumericAnswerAdapter adapter = new ComplexNumericAnswerAdapter(this);

        NumericItemViewHolder(ViewGroup parent) {
            super(parent);
            recyclerView.setAdapter(adapter);
        }

        @Override
        protected void onBind(Variant item) {
            super.onBind(item);
            adapter.setItems(item.getOptions().stream().map(VariantItem::getName).collect(Collectors.toList()));
            adapter.setAnswerStates(question.getAnswerValuesOfVariant(item));
        }

        @Override
        public void onBinaryAnswerChange(int atPosition, @Nullable String input) {
            question.setVariantAnswer(getItem(), atPosition, input);
            notifyQuestionChangedFromDefaultViewHolder();
        }

    }

    class BinaryItemViewHolder extends ItemViewHolder implements ComplexBinaryAnswerAdapter.OnBinaryAnswerChangeListener {

        private final ComplexBinaryAnswerAdapter adapter = new ComplexBinaryAnswerAdapter(this);

        BinaryItemViewHolder(ViewGroup parent) {
            super(parent);
            recyclerView.setAdapter(adapter);
        }

        @Override
        protected void onBind(Variant item) {
            super.onBind(item);
            adapter.setItems(item.getOptions().stream().map(VariantItem::getName).collect(Collectors.toList()));
            adapter.setAnswerStates(question.getBinaryAnswerStatesOfVariant(getContext(), item));
        }

        @Override
        public void onBinaryAnswerChange(int atPosition, @Nullable BinaryAnswerState binaryAnswerState) {
            question.setVariantAnswer(getItem(), atPosition, getBinaryAnswerStateAnswer(binaryAnswerState));
            notifyQuestionChangedFromDefaultViewHolder();
        }

        @Nullable
        private String getBinaryAnswerStateAnswer(@Nullable BinaryAnswerState binaryAnswerState) {
            return binaryAnswerState == null ? null : binaryAnswerState.getText().getString(getContext());
        }
    }

    class BlockerViewHolder extends ViewHolder {

        private TextView textView = findViewById(R.id.textview);
        private RadioButton radioButton = findViewById(R.id.radiobutton);

        private boolean isChecked;

        BlockerViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_selectable_answer_single);
            itemView.setOnClickListener(this);
        }

        @Override
        protected void onBind(Variant item) {
            isChecked = question.haveAnswerOnVariant(item);
            itemView.setActivated(isChecked);
            radioButton.setChecked(isChecked);
            textView.setText(item.getName());
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                question.setBlockerVariantAnswered(getItem(), !isChecked);
                notifyQuestionChangedFromBlockerViewHolder();
            }
        }
    }

    class ItemViewHolder extends ViewHolder {

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        private TextView titleTextView = findViewById(R.id.textview_title);

        ItemViewHolder(ViewGroup parent) {
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
