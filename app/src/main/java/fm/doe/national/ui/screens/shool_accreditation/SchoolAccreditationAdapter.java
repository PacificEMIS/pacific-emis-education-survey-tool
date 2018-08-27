package fm.doe.national.ui.screens.shool_accreditation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.ModelsExt;
import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.ui.screens.base.BaseRecyclerViewHolder;

public class SchoolAccreditationAdapter extends RecyclerView.Adapter<SchoolAccreditationAdapter.SchoolViewHolder> {

    private List<SchoolAccreditationPassing> items = new ArrayList<>();

    @Nullable
    private Callback callback;

    @NonNull
    @Override
    public SchoolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SchoolViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull SchoolViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<SchoolAccreditationPassing> schools) {
        this.items = schools;
        notifyDataSetChanged();
    }

    public void setCallback(@Nullable Callback callback) {
        this.callback = callback;
    }

    protected class SchoolViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.textview_name_school)
        TextView nameSchoolTextview;

        @BindView(R.id.textview_progress)
        TextView progressTextView;

        @BindView(R.id.textview_created_year)
        TextView createdYearTextView;

        @BindView(R.id.progressbar)
        ProgressBar progressBar;

        private SchoolAccreditationPassing item;

        SchoolViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_school);
        }

        @Override
        public void onClick(View v) {
            if (callback != null) {
                callback.onClick(item);
            }
        }

        void bind(SchoolAccreditationPassing accreditationPassing) {
            item = accreditationPassing;
            nameSchoolTextview.setText(accreditationPassing.getSchool().getName());
            createdYearTextView.setText(String.valueOf(accreditationPassing.getYear()));

            rebindProgress(
                    ModelsExt.getTotalQuestionsCount(accreditationPassing.getSchoolAccreditation()),
                    ModelsExt.getAnsweredQuestionsCount(accreditationPassing.getSchoolAccreditation()),
                    progressTextView, progressBar);
        }

    }

    public interface Callback {
        void onClick(SchoolAccreditationPassing schoolAccreditationPassing);
    }
}