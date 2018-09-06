package fm.doe.national.ui.screens.standard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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
    private View view;
    private String name = null;
    private String interviewQuestion = null;
    private String hint = null;
    private String comment = null;

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

    public void setListener(@Nullable CommentDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.KeyboardDialog);
        parseArguments();
    }

    private void parseArguments() {
        Bundle bundle = getArguments();
        if (bundle == null) return;
        name = bundle.getString(ARG_NAME);
        interviewQuestion = bundle.getString(ARG_INTERVIEW);
        hint = bundle.getString(ARG_HINT);
        comment = bundle.getString(ARG_COMMENT);
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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initView();
        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private void initView() {
        if (view != null || getActivity() == null) return;

        view = getActivity().getLayoutInflater().inflate(R.layout.dialog_comment, null);
        TextView nameTextView = view.findViewById(R.id.textview_subcriteria_name);
        TextView interviewTextView = view.findViewById(R.id.textview_interview_question);
        TextView hintTextView = view.findViewById(R.id.textview_hint);
        EditText commentEditText = view.findViewById(R.id.edittext_comment);

        nameTextView.setText(name);
        interviewTextView.setText(interviewQuestion);
        hintTextView.setText(hint);
        commentEditText.setText(comment);

        view.findViewById(R.id.button_submit).setOnClickListener(v -> {
            if (listener != null) listener.onCommentSubmit(commentEditText.getText().toString());
            dismiss();
        });
        view.setOnClickListener(v -> dismiss());
    }

    public interface CommentDialogListener {
        void onCommentSubmit(String comment);
    }
}
