package fm.doe.national.ui.screens.base;

import android.view.View;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import butterknife.ButterKnife;

public class BaseRecyclerViewHolder extends OmegaRecyclerView.ViewHolder {
    public BaseRecyclerViewHolder(View v) {
        super(v);
        ButterKnife.bind(this, v);
    }
}
