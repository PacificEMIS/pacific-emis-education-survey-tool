package fm.doe.national.ui.screens.categories;

import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.model.mutable.MutableCategory;
import fm.doe.national.domain.model.Progress;
import fm.doe.national.ui.screens.base.BaseAdapter;
import fm.doe.national.app_support.utils.ViewUtils;

public class CategoriesListAdapter
        extends BaseAdapter<MutableCategory> {

    public CategoriesListAdapter(OnItemClickListener<MutableCategory> clickListener) {
        super(clickListener);
    }

    @Override
    protected CategoryViewHolder provideViewHolder(ViewGroup parent) {
        return new CategoryViewHolder(parent);
    }

    protected class CategoryViewHolder extends ViewHolder {

        @BindView(R.id.textview_category_name)
        TextView categoryNameTextView;

        @BindView(R.id.textview_progress)
        TextView progressTextView;

        @BindView(R.id.progressbar)
        ProgressBar progressBar;

        CategoryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_category);
        }

        @Override
        public void onBind(MutableCategory item) {
            categoryNameTextView.setText(item.getTitle());

            Progress progress = item.getProgress();
            ViewUtils.rebindProgress(
                    progress.total,
                    progress.completed,
                    getString(R.string.criteria_progress),
                    progressTextView,
                    progressBar
            );

            itemView.setOnClickListener(this);
        }
    }
}