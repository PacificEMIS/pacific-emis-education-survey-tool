package fm.doe.national.ui.screens.base;

import android.os.Build;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import butterknife.ButterKnife;
import fm.doe.national.R;

public abstract class BaseRecyclerViewHolder<T> extends OmegaRecyclerView.ViewHolder {

    protected T item;

    public BaseRecyclerViewHolder(View v) {
        super(v);
        ButterKnife.bind(this, v);
    }

    public BaseRecyclerViewHolder(ViewGroup parent, int res) {
        super(parent, res);
        ButterKnife.bind(this, itemView);
    }

    public void bind(T item) {
        this.item = item;
        onBind();
    }

    public abstract void onBind();

    protected void rebindProgress(int total, int done, @Nullable TextView textView, @Nullable ProgressBar progressBar) {
        int progress = (int) ((float)done / total * 100);

        if (textView != null) {
            textView.setActivated(progress == 100);
            textView.setText(getResources().getString(R.string.criteria_progress, done, total));
        }

        if (progressBar != null) {
            progressBar.setActivated(progress == 100);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                progressBar.setProgress(progress, true);
            } else {
                progressBar.setProgress(progress);
            }
        }
    }
}
