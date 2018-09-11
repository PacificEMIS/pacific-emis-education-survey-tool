package fm.doe.national.ui.screens.standard;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.OnClick;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.data.data_source.models.SubCriteriaQuestion;
import fm.doe.national.ui.screens.base.BaseDialogFragment;
import fm.doe.national.utils.Constants;

public class CommentDialogFragment extends BaseDialogFragment {

    private static final String ARG_DATA = "ARG_DATA";

    @BindView(R.id.textview_subcriteria_name)
    TextView nameTextView;

    @BindView(R.id.textview_interview_question)
    TextView interviewTextView;

    @BindView(R.id.textview_hint)
    TextView hintTextView;

    @BindView(R.id.edittext_comment)
    EditText commentEditText;

    @Nullable
    private OnCommentSubmitListener listener;

    public static CommentDialogFragment create(SubCriteria subCriteria) {
        CommentDialogFragment dialog = new CommentDialogFragment();
        Bundle args = new Bundle();

        SubCriteriaQuestion question = subCriteria.getSubCriteriaQuestion();
        ViewData viewData = new ViewData(
                subCriteria.getName(),
                question.getInterviewQuestion(),
                question.getHint(),
                subCriteria.getAnswer().getComment());

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
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCommentSubmitListener) {
            listener = (OnCommentSubmitListener) context;
        } else {
            throw new RuntimeException("Parent should implement OnCommentSubmitListener");
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
        view.setOnClickListener(v -> dismiss());
        parseArgs();
    }

    private void parseArgs() {
        Bundle bundle = getArguments();
        if (bundle == null) throw new RuntimeException(Constants.Errors.WRONG_FRAGMENT_ARGS);
        ViewData data = (ViewData) bundle.getSerializable(ARG_DATA);
        if (data == null) throw new RuntimeException(Constants.Errors.WRONG_FRAGMENT_ARGS);

        nameTextView.setText(data.name);
        interviewTextView.setText(data.interviewQuestions);
        hintTextView.setText(data.hint);
        commentEditText.setText(data.comment);
    }

    @OnClick(R.id.button_submit)
    public void onSubmitClick(View v) {
        if (listener != null) listener.onCommentSubmit(commentEditText.getText().toString());
        dismiss();
    }

    public interface OnCommentSubmitListener {
        void onCommentSubmit(String comment);
    }

    private static class ViewData implements Serializable {
        public final String name;
        public final String interviewQuestions;
        public final String hint;
        public final String comment;

        ViewData(String name, String interviewQuestions, String hint, String comment) {
            this.name = name;
            this.interviewQuestions = interviewQuestions;
            this.hint = hint;
            this.comment = comment;
        }
    }
}
