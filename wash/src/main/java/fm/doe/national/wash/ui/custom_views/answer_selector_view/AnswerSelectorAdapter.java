package fm.doe.national.wash.ui.custom_views.answer_selector_view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fm.doe.national.wash.R;

public class AnswerSelectorAdapter extends BaseListAdapter<String> {

    private final Type type;
    private final OnCheckedChangeListener listener;

    private List<Integer> checkedPositions = Collections.emptyList();

    public AnswerSelectorAdapter(Type type, OnCheckedChangeListener listener) {
        this.type = type;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (type) {
            case SINGLE:
                return new SingleViewHolder(parent);
            case MULTIPLE:
                return new MultipleViewHolder(parent);
        }
        throw new IllegalStateException();
    }

    @Override
    protected ViewHolder provideViewHolder(ViewGroup parent) {
        return null;
    }

    private void updateSelection(int position) {
        boolean isChecked = checkedPositions.contains(position);
        switch (type) {
            case SINGLE:
                uncheckAll();
                if (!isChecked) {
                    checkItemAtPosition(position);
                }
                break;
            case MULTIPLE:
                if (!isChecked) {
                    checkItemAtPosition(position);
                } else {
                    uncheckItemAtPosition(position);
                }
                break;
        }
    }

    private void checkItemAtPosition(int position) {
        checkedPositions.add(position);
        listener.onCheckedChange(position, true);
    }

    private void uncheckItemAtPosition(int position) {
        checkedPositions.remove(position);
        listener.onCheckedChange(position, false);
    }

    private void uncheckAll() {
        List<Integer> positions = new ArrayList<>(checkedPositions);
        checkedPositions.clear();
        positions.forEach(p -> {
            this.notifyItemChanged(p);
            listener.onCheckedChange(p, false);
        });
    }

    class SingleViewHolder extends BaseViewHolder {

        private RadioButton radioButton = findViewById(R.id.radiobutton);

        public SingleViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_selectable_answer_single);
        }

        @Override
        protected void onBind(String item) {
            radioButton.setChecked(isChecked());
        }
    }

    class MultipleViewHolder extends BaseViewHolder {

        private CheckBox checkBox = findViewById(R.id.checkbox);

        public MultipleViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_selectable_answer_multiple);
        }

        @Override
        protected void onBind(String item) {
            checkBox.setChecked(isChecked());
        }
    }

    class BaseViewHolder extends ViewHolder {

        private TextView textView = findViewById(R.id.textview);

        public BaseViewHolder(ViewGroup parent, int res) {
            super(parent, res);
        }

        @Override
        protected void onBind(String item) {
            textView.setText(item);
            itemView.setActivated(isChecked());
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            updateSelection(getAdapterPosition());
        }

        protected boolean isChecked() {
            return checkedPositions.contains(getAdapterPosition());
        }
    }

    public enum Type {
        SINGLE, MULTIPLE
    }

    public interface OnCheckedChangeListener {
        void onCheckedChange(int atPosition, boolean checked);
    }
}
