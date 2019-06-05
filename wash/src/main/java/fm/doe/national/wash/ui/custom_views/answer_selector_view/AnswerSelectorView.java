package fm.doe.national.wash.ui.custom_views.answer_selector_view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import fm.doe.national.wash.R;

public class AnswerSelectorView extends FrameLayout {

    private RecyclerView recyclerView;

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

        recyclerView = findViewById(R.id.recyclerview);

    }
}
