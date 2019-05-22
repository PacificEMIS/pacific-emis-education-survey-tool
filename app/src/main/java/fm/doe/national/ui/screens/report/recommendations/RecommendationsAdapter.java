package fm.doe.national.ui.screens.report.recommendations;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.doe.national.R;
import fm.doe.national.app_support.utils.ViewUtils;
import fm.doe.national.core.data.model.recommendations.Recommendation;

public class RecommendationsAdapter extends BaseListAdapter<Recommendation> {

    @Override
    protected ItemViewHolder provideViewHolder(ViewGroup parent) {
        return new ItemViewHolder(parent);
    }

    class ItemViewHolder extends ViewHolder {

        @BindView(R.id.view_marginer)
        View marginerView;

        @BindView(R.id.textview_content)
        TextView contentTextView;

        private final int marginStep = (int) getResources().getDimension(R.dimen.margin_start_recommendation);

        public ItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_recommendation);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void onBind(Recommendation item) {
            contentTextView.setText(item.getContent());

            ViewUtils.updateMargins(marginerView, marginStep * item.getLevel(), 0, 0, 0);
        }

    }
}
