package fm.doe.national.report_core.ui.recommendations;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;

import fm.doe.national.core.data.model.Category;
import fm.doe.national.core.data.model.Criteria;
import fm.doe.national.core.data.model.Standard;
import fm.doe.national.core.data.model.SubCriteria;
import fm.doe.national.report_core.R;
import fm.doe.national.report_core.model.recommendations.Recommendation;

public class RecommendationsAdapter extends BaseListAdapter<Recommendation> {

    private static final int VIEW_TYPE_CATEGORY = 0;
    private static final int VIEW_TYPE_STANDARD = 1;
    private static final int VIEW_TYPE_CRITERIA = 2;
    private static final int VIEW_TYPE_SUB_CRITERIA = 3;

    @Override
    public int getItemViewType(int position) {
        Object object = getItem(position).getObject();

        if (object instanceof Category) {
            return VIEW_TYPE_CATEGORY;
        }

        if (object instanceof Standard) {
            return VIEW_TYPE_STANDARD;
        }

        if (object instanceof Criteria) {
            return VIEW_TYPE_CRITERIA;
        }

        if (object instanceof SubCriteria) {
            return VIEW_TYPE_SUB_CRITERIA;
        }

        throw new IllegalStateException();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_CATEGORY:
                return new ItemViewHolder(parent, R.layout.item_recommendation_category);
            case VIEW_TYPE_STANDARD:
                return new ItemViewHolder(parent, R.layout.item_recommendation_standard);
            case VIEW_TYPE_CRITERIA:
                return new ItemViewHolder(parent, R.layout.item_recommendation_criteria);
            case VIEW_TYPE_SUB_CRITERIA:
                return new ItemViewHolder(parent, R.layout.item_recommendation_subcriteria);
        }
        throw new IllegalStateException();
    }

    // unused
    @Override
    protected ItemViewHolder provideViewHolder(ViewGroup parent) {
        return null;
    }

    class ItemViewHolder extends ViewHolder {

        ItemViewHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
        }

        @Override
        protected void onBind(Recommendation item) {
            item.getContent().applyTo((TextView) itemView, null);
        }

        @Override
        public void onClick(View v) {
            // Only sub-criteria is clickable
            if (getItem().getObject() instanceof SubCriteria) {
                super.onClick(v);
            }
        }
    }
}
