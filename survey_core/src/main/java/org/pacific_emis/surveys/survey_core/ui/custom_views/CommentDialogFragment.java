package org.pacific_emis.surveys.survey_core.ui.custom_views;

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

import org.pacific_emis.surveys.core.ui.screens.base.BaseDialogFragment;
import org.pacific_emis.surveys.core.utils.Constants;
import org.pacific_emis.surveys.survey_core.R;

public class CommentDialogFragment extends BaseDialogFragment implements View.OnClickListener {

    private static final String ARG_DATA = "ARG_DATA";

    private TextView nameTextView;
    private TextView titleTextView;
    private TextView interviewTextView;
    private EditText commentEditText;
    private Button submitButton;
    private Button cancelButton;
    private String oldComment;

    @Nullable
    private OnCommentSubmitListener listener;

    public static CommentDialogFragment create(ViewData viewData) {
        CommentDialogFragment dialog = new CommentDialogFragment();
        Bundle args = new Bundle();
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
        cancelButton.setOnClickListener(this);
    }

    private void bindViews(View view) {
        titleTextView = view.findViewById(R.id.textview_title);
        nameTextView = view.findViewById(R.id.textview_subcriteria_name);
        interviewTextView = view.findViewById(R.id.textview_interview_question);
        commentEditText = view.findViewById(R.id.edittext_comment);
        submitButton = view.findViewById(R.id.button_ok);
        cancelButton = view.findViewById(R.id.button_cancel);
    }

    private void parseArgs() {
        Bundle bundle = getArguments();
        if (bundle == null) throw new RuntimeException(Constants.Errors.WRONG_FRAGMENT_ARGS);
        ViewData data = (ViewData) bundle.getSerializable(ARG_DATA);
        if (data == null) throw new RuntimeException(Constants.Errors.WRONG_FRAGMENT_ARGS);

        oldComment = data.comment;
        titleTextView.setText(data.id);
        nameTextView.setText(data.name);
        interviewTextView.setText(data.interviewQuestions);
        commentEditText.setText(data.comment);
    }

    @Override
    public void onClick(View v) {
        if (v == submitButton) {
            if (listener != null) listener.onCommentSubmit(commentEditText.getText().toString());
            dismiss();
        } else if (v == cancelButton) {
            if (listener != null) listener.onCommentSubmit(oldComment);
            dismiss();
        }
    }

    public interface OnCommentSubmitListener {
        void onCommentSubmit(String comment);
    }

    public static class ViewData implements Serializable {
        private final String name;
        private final String id;
        private final String interviewQuestions;
        private final String comment;

        public ViewData(String id, String name, String interviewQuestions, String comment) {
            this.name = name;
            this.id = id;
            this.interviewQuestions = interviewQuestions;
            this.comment = comment;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        public String getInterviewQuestions() {
            return interviewQuestions;
        }

        public String getComment() {
            return comment;
        }
    }
}
