package fm.doe.national.ui.screens.choose_category;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.ModelsExt;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.ui.screens.base.BaseRecyclerViewHolder;
import fm.doe.national.utils.ViewUtils;

public class ChooseCategoryAdapter extends RecyclerView.Adapter<ChooseCategoryAdapter.CategoryViewHolder> {

    private List<Standard> items = new ArrayList<>();

    @Nullable
    private Callback callback;

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<Standard> standards) {
        this.items = standards;
        notifyDataSetChanged();
    }

    public void setCallback(@Nullable Callback callback) {
        this.callback = callback;
    }

    protected class CategoryViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

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

        void bind(Standard standard) {
            if (getAdapterPosition() < ViewUtils.STANDARD_ICONS.length) {
                standardIconImageView.setImageResource(ViewUtils.STANDARD_ICONS[getAdapterPosition()]);
                standardIconImageView.setActivated(true);
            }

            categoryNameTextView.setText(String.format(
                    itemView.getContext().getString(R.string.category_name),
                    getAdapterPosition(), standard.getName()));

            rebindProgress(
                    ModelsExt.getTotalQuestionsCount(standard),
                    ModelsExt.getAnsweredQuestionsCount(standard),
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