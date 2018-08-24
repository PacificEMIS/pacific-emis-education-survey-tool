package fm.doe.national.ui.screens.choose_category;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.mock.MockStandard;
import fm.doe.national.ui.screens.base.BaseRecyclerAdapter;

/**
 * Created by Alexander Chibirev on 8/17/2018.
 */

public class ChooseCategoryAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseViewHolder> {

    private List<MockStandard> standards = new ArrayList<>();
    @Nullable
    private Callback callback;

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(inflateView(parent, R.layout.item_category));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        ((CategoryViewHolder) holder).update(standards.get(position));
    }

    @Override
    public int getItemCount() {
        return standards.size();
    }

    public void updateCategory(List<MockStandard> categories) {
        this.standards = categories;
        notifyDataSetChanged();
    }

    public void setCallback(@Nullable Callback callback) {
        this.callback = callback;
    }

    class CategoryViewHolder extends BaseViewHolder {

        @BindView(R.id.imageview_category_icon)
        ImageView standardIconImageView;

        @BindView(R.id.textview_category_name)
        TextView categoryNameTextView;

        @BindView(R.id.textview_progress)
        TextView progressTextView;

        @BindView(R.id.progressbar)
        ProgressBar progressBar;

        public CategoryViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onClick(int position) {
       //     if (callback != null) callback.onCategoryClicked(standards.get(position));
        }

        public void update(MockStandard standard) {
            standardIconImageView.setImageResource(standard.getIcon());
            categoryNameTextView.setText(String.format(
                    itemView.getContext().getString(R.string.category_name),
                    getAdapterPosition(), standard.getName()));
            rebindProgress(standard.getCriterias().get(0).getSubcriterias(), progressTextView, progressBar);
        }

    }

    public interface Callback {
        void onCategoryClicked(SchoolAccreditationPassing standard);
    }
}