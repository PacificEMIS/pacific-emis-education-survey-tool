package fm.doe.national.ui.screens.base;

import android.view.View;
import android.view.ViewGroup;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import butterknife.ButterKnife;

public class BaseRecyclerViewHolder extends OmegaRecyclerView.ViewHolder {

    public BaseRecyclerViewHolder(View v) {
        super(v);
        ButterKnife.bind(this, v);
    }

    public BaseRecyclerViewHolder(ViewGroup parent, int res) {
        super(parent, res);
        ButterKnife.bind(this, itemView);
    }

}
