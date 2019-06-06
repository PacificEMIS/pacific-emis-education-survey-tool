package fm.doe.national.wash.ui.custom_views.answer_selector_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fm.doe.national.core.utils.CollectionUtils;
import fm.doe.national.wash.R;

public class AnswerSelectorView extends FrameLayout implements
        AnswerSelectorAdapter.OnCheckedChangeListener {

    private RecyclerView recyclerView;
    private AnswerSelectorAdapter adapter;
    private List<String> possibleAnswers = Collections.emptyList();
    private ArrayList<Integer> selectedIndexes = CollectionUtils.emptyArrayList();

    @Nullable
    private Listener listener;

    public AnswerSelectorView(@NonNull Context context) {
        this(context, null);
    }

    public AnswerSelectorView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnswerSelectorView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public AnswerSelectorView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        inflate(context, R.layout.view_answer_selector, this);

        bindViews();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AnswerSelectorView, defStyleAttr, defStyleRes);

        setSelectorType(AnswerSelectionType.createFromValue(a.getInt(R.styleable.AnswerSelectorView_selectionType, 0)));

        a.recycle();
    }

    private void bindViews() {
        recyclerView = findViewById(R.id.recyclerview);
    }

    public void setSelectorType(AnswerSelectionType type) {
        adapter = new AnswerSelectorAdapter(type, this);
        updateAdapterData(possibleAnswers, selectedIndexes);
        recyclerView.setAdapter(adapter);
    }

    public void setItems(List<String> possibleAnswers, ArrayList<Integer> selectedIndexes) {
        this.possibleAnswers = possibleAnswers;
        if (adapter != null) {
            updateAdapterData(possibleAnswers, selectedIndexes);
        }
    }

    private void updateAdapterData(List<String> possibleAnswers, ArrayList<Integer> selectedIndexes) {
        adapter.setItems(possibleAnswers);
        adapter.setSelectedIndexes(selectedIndexes);
    }

    public void setListener(@Nullable Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onCheckedChange(int atPosition, boolean checked) {
        if (listener != null) {
            listener.onCheckedChange(atPosition, checked);
        }
    }

    public interface Listener {
        void onCheckedChange(int atPosition, boolean checked);
    }
}
