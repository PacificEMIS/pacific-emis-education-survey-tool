package fm.doe.national.ui.dialogs.merge_progress;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.presenter.InjectPresenter;

import butterknife.BindView;
import butterknife.OnClick;
import fm.doe.national.R;
import fm.doe.national.core.data.model.mutable.MutableProgress;
import fm.doe.national.core.ui.screens.base.BaseDialogFragment;
import fm.doe.national.core.utils.ViewUtils;

public class MergeProgressDialogFragment extends BaseDialogFragment implements MergeProgressView {

    @InjectPresenter
    MergeProgressPresenter presenter;

    @BindView(R.id.textview_description)
    TextView desciptionTextView;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    public static MergeProgressDialogFragment create() {
        return new MergeProgressDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.setCancelable(false);
        return inflater.inflate(R.layout.dialog_merge_progress, container, false);
    }

    @OnClick(R.id.button_end_session)
    void onEndSessionPressed() {
        presenter.onEndSessionPressed();
    }

    @Override
    public void close() {
        this.dismiss();
    }

    @Override
    public void setProgress(int progress) {
        ViewUtils.rebindProgress(new MutableProgress(100, progress), null, progressBar);
    }

    @Override
    public void setDescription(Text text) {
        text.applyTo(desciptionTextView);
    }
}
