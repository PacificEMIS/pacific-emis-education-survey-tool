package fm.doe.national.ui.screens.standard;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import fm.doe.national.R;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.data.data_source.models.SubCriteriaQuestion;

public class CommentDialog extends DialogFragment {

    private static final String ARG_NAME = "ARG_NAME";
    private static final String ARG_INTERVIEW = "ARG_INTERVIEW";
    private static final String ARG_HINT = "ARG_HINT";
    private static final String ARG_COMMENT = "ARG_COMMENT";

    @Nullable
    private CommentDialogListener listener;

    public void setListener(@Nullable CommentDialogListener listener) {
        this.listener = listener;
    }

    public static CommentDialog create(SubCriteria subCriteria) {
        CommentDialog dialog = new CommentDialog();
        Bundle args = new Bundle();
        SubCriteriaQuestion question = subCriteria.getSubCriteriaQuestion();
        args.putString(ARG_NAME, subCriteria.getName());
        args.putString(ARG_INTERVIEW, question.getInterviewQuestion());
        args.putString(ARG_HINT, question.getHint());
        args.putString(ARG_COMMENT, subCriteria.getAnswer().getComment());
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_comment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView nameTextView = view.findViewById(R.id.textview_subcriteria_name);
        TextView interviewTextView = view.findViewById(R.id.textview_interview_question);
        TextView hintTextView = view.findViewById(R.id.textview_hint);
        EditText commentEditText = view.findViewById(R.id.edittext_comment);

        view.findViewById(R.id.button_submit).setOnClickListener(v -> {
            if (listener != null) listener.onCommentSubmit(commentEditText.getText().toString());
            dismiss();
        });
        view.setOnClickListener(v -> dismiss());

        Bundle bundle = getArguments();
        if (bundle == null) return;

        nameTextView.setText(bundle.getString(ARG_NAME));
        interviewTextView.setText(bundle.getString(ARG_INTERVIEW));
        hintTextView.setText(bundle.getString(ARG_HINT));
        commentEditText.setText(bundle.getString(ARG_COMMENT));
    }

    public interface CommentDialogListener {
        void onCommentSubmit(String comment);
    }
}
