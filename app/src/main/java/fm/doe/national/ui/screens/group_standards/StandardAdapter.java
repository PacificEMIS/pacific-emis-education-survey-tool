package fm.doe.national.ui.screens.group_standards;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.ModelsExt;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.ui.screens.base.BaseClickableAdapter;
import fm.doe.national.ui.screens.base.BaseRecyclerViewHolder;

public class StandardAdapter extends BaseClickableAdapter<Standard, StandardAdapter.CategoryViewHolder> {

    @Override
    protected CategoryViewHolder provideViewHolder(ViewGroup parent) {
        return new CategoryViewHolder(parent);
    }

    protected class CategoryViewHolder extends BaseRecyclerViewHolder<Standard> implements View.OnClickListener {

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
        public void onBind() {
            standardIconImageView.setImageResource(R.drawable.ic_format_list_bulleted);

            categoryNameTextView.setText(item.getName());

            rebindProgress(
                    ModelsExt.getTotalQuestionsCount(item),
                    ModelsExt.getAnsweredQuestionsCount(item),
                    progressTextView, progressBar);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClick(item);
        }
    }
}