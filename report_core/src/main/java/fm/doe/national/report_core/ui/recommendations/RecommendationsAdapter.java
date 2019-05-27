package fm.doe.national.report_core.ui.recommendations;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;

import fm.doe.national.core.utils.ViewUtils;
import fm.doe.national.report_core.R;
import fm.doe.national.report_core.model.recommendations.Recommendation;

public class RecommendationsAdapter extends BaseListAdapter<Recommendation> {

    @Override
    protected ItemViewHolder provideViewHolder(ViewGroup parent) {
        return new ItemViewHolder(parent);
    }

    class ItemViewHolder extends ViewHolder {

        private View marginerView;
        private TextView contentTextView;

        private final int marginStep = (int) getResources().getDimension(R.dimen.margin_start_recommendation);

        public ItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_recommendation);
            bindViews();
        }

        private void bindViews() {
            marginerView = findViewById(R.id.view_marginer);
            contentTextView = findViewById(R.id.textview_content);
        }

        @Override
        protected void onBind(Recommendation item) {
            contentTextView.setText(item.getContent());

            ViewUtils.updateMargins(marginerView, marginStep * item.getLevel(), 0, 0, 0);
        }

    }
}
