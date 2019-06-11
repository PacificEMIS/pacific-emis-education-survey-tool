package fm.doe.national.wash.ui.custom_views;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;

import fm.doe.national.core.ui.screens.base.BaseDialogFragment;
import fm.doe.national.core.utils.Constants;
import fm.doe.national.wash.R;
import fm.doe.national.wash_core.data.model.Question;

public class CommentDialogFragment extends BaseDialogFragment implements View.OnClickListener {

    private static final String ARG_DATA = "ARG_DATA";

    private TextView nameTextView;
    private TextView interviewTextView;
    private TextView hintTextView;
    private EditText commentEditText;
    private Button submitButton;

    @Nullable
    private OnCommentSubmitListener listener;

    public static CommentDialogFragment create(Question question) {
        CommentDialogFragment dialog = new CommentDialogFragment();
        Bundle args = new Bundle();
        String comment = question.getAnswer() == null ? "" : question.getAnswer().getComment();
        ViewData viewData = new ViewData(question.getTitle(), comment);
        args.putSerializable(ARG_DATA, viewData);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        setupFullScreenLayout();
    }

    private void setupFullScreenLayout() {
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            // Line to fix keyboard and dialog overlapping
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    public void setListener(@Nullable OnCommentSubmitListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_comment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        view.setOnClickListener(v -> dismiss());
        parseArgs();
        submitButton.setOnClickListener(this);
    }

    private void bindViews(View view) {
        nameTextView = view.findViewById(R.id.textview_subcriteria_name);
        interviewTextView = view.findViewById(R.id.textview_interview_question);
        hintTextView = view.findViewById(R.id.textview_hint);
        commentEditText = view.findViewById(R.id.edittext_comment);
        submitButton = view.findViewById(R.id.button_submit);
    }

    private void parseArgs() {
        Bundle bundle = getArguments();
        if (bundle == null) throw new RuntimeException(Constants.Errors.WRONG_FRAGMENT_ARGS);
        ViewData data = (ViewData) bundle.getSerializable(ARG_DATA);
        if (data == null) throw new RuntimeException(Constants.Errors.WRONG_FRAGMENT_ARGS);

        nameTextView.setText(data.name);
        interviewTextView.setVisibility(View.GONE);
        hintTextView.setVisibility(View.GONE);
        commentEditText.setText(data.comment);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_submit) {
            if (listener != null) listener.onCommentSubmit(commentEditText.getText().toString());
            dismiss();
        }
    }

    public interface OnCommentSubmitListener {
        void onCommentSubmit(String comment);
    }

    private static class ViewData implements Serializable {
        public final String name;
        public final String comment;

        ViewData(String name, String comment) {
            this.name = name;
            this.comment = comment;
        }
    }
}
