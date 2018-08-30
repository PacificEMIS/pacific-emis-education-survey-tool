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
import fm.doe.national.ui.screens.base.BaseAdapter;
import fm.doe.national.utils.ViewUtils;

public class StandardListAdapter extends BaseAdapter<Standard> {

    @Override
    protected CategoryViewHolder provideViewHolder(ViewGroup parent) {
        return new CategoryViewHolder(parent);
    }

    protected class CategoryViewHolder extends ViewHolder implements View.OnClickListener {

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
        public void onBind(Standard item) {
            standardIconImageView.setImageResource(R.drawable.ic_format_list_bulleted);

            categoryNameTextView.setText(item.getName());

            ViewUtils.rebindProgress(
                    ModelsExt.getTotalQuestionsCount(item),
                    ModelsExt.getAnsweredQuestionsCount(item),
                    getString(R.string.criteria_progress),
                    progressTextView, progressBar);

            itemView.setOnClickListener(this);
        }

    }
}