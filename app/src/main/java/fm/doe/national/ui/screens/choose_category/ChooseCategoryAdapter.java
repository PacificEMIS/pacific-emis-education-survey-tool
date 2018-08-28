package fm.doe.national.ui.screens.choose_category;

import android.support.annotation.Nullable;
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
import fm.doe.national.utils.ViewUtils;

public class ChooseCategoryAdapter extends BaseClickableAdapter<Standard, ChooseCategoryAdapter.CategoryViewHolder> {

    @Nullable
    private Callback callback;

    @Override
    protected CategoryViewHolder provideViewHolder(ViewGroup parent) {
        return new CategoryViewHolder(parent);
    }

    public void setCallback(@Nullable Callback callback) {
        this.callback = callback;
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
            super(parent, R.layout.item_category);
        }

        @Override
        public void onBind() {
            if (getAdapterPosition() < ViewUtils.STANDARD_ICONS.length) {
                standardIconImageView.setImageResource(ViewUtils.STANDARD_ICONS[getAdapterPosition()]);
                standardIconImageView.setActivated(true);
            }

            categoryNameTextView.setText(String.format(
                    itemView.getContext().getString(R.string.category_name),
                    getAdapterPosition(), item.getName()));

            rebindProgress(
                    ModelsExt.getTotalQuestionsCount(item),
                    ModelsExt.getAnsweredQuestionsCount(item),
                    progressTextView, progressBar);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (callback != null) callback.onClick(getAdapterPosition());
        }
    }

    public interface Callback {
        void onClick(int position);
    }
}