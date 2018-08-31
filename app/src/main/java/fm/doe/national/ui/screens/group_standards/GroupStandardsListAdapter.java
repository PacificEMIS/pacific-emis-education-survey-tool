package fm.doe.national.ui.screens.group_standards;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.CategoryProgress;
import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.ui.screens.base.BaseAdapter;
import fm.doe.national.utils.ViewUtils;

public class GroupStandardsListAdapter
        extends BaseAdapter<GroupStandard> {

    @Override
    protected CategoryViewHolder provideViewHolder(ViewGroup parent) {
        return new CategoryViewHolder(parent);
    }

    protected class CategoryViewHolder extends ViewHolder {

        @BindView(R.id.imageview_category_icon)
        ImageView standardIconImageView;

        @BindView(R.id.textview_category_name)
        TextView categoryNameTextView;

        @BindView(R.id.textview_progress)
        TextView progressTextView;

        @BindView(R.id.progressbar)
        ProgressBar progressBar;

        CategoryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_group_standards);
        }

        @Override
        public void onBind(GroupStandard item) {
            if (getAdapterPosition() < ViewUtils.STANDARD_ICONS.length) {
                standardIconImageView.setImageResource(ViewUtils.STANDARD_ICONS[getAdapterPosition()]);
                standardIconImageView.setActivated(true);
            }

            categoryNameTextView.setText(item.getName());

            CategoryProgress categoryProgress = item.getCategoryProgress();
            ViewUtils.rebindProgress(
                    categoryProgress.getTotalQuestionsCount(),
                    categoryProgress.getAnsweredQuestionsCount(),
                    getString(R.string.criteria_progress),
                    progressTextView, progressBar);

            itemView.setOnClickListener(this);
        }
    }
}