package fm.doe.national.ui.screens.categories;

import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.model.Category;
import fm.doe.national.ui.screens.base.BaseAdapter;

public class CategoriesListAdapter
        extends BaseAdapter<Category> {

    public CategoriesListAdapter(OnItemClickListener<Category> clickListener) {
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
        public void onBind(Category item) {
            categoryNameTextView.setText(item.getTitle());

            // TODO: fixme
//            CategoryProgress categoryProgress = item.getCategoryProgress();
//            ViewUtils.rebindProgress(
//                    categoryProgress.getTotalQuestionsCount(),
//                    categoryProgress.getAnsweredQuestionsCount(),
//                    getString(R.string.criteria_progress),
//                    progressTextView, progressBar);

            itemView.setOnClickListener(this);
        }
    }
}