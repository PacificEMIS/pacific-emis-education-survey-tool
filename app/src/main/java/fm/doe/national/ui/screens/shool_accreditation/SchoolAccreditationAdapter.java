package fm.doe.national.ui.screens.shool_accreditation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.mock.MockSchool;
import fm.doe.national.ui.screens.base.BaseRecyclerAdapter;

/**
 * Created by Alexander Chibirev on 8/16/2018.
 */
public class SchoolAccreditationAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseViewHolder> {

    private List<MockSchool> schools = new ArrayList<>();
    @Nullable
    private Callback callback;

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SchoolViewHolder(inflateView(parent, R.layout.item_school));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        ((SchoolViewHolder) holder).update(schools.get(position));
    }

    @Override
    public int getItemCount() {
        return schools.size();
    }

    public void updateSchools(List<MockSchool> schools) {
        this.schools = schools;
        notifyDataSetChanged();
    }

    public void setCallback(@Nullable Callback callback) {
        this.callback = callback;
    }

    class SchoolViewHolder extends BaseViewHolder {

        @BindView(R.id.textview_name_school)
        TextView nameSchoolTextview;

        @BindView(R.id.textview_progress)
        TextView progressTextView;

        @BindView(R.id.textview_created_year)
        TextView createdYearTextview;

        @BindView(R.id.progressbar)
        ProgressBar progressBar;

        public SchoolViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onClick(int position) {
            if(callback != null) {
                callback.onSchoolClicked(schools.get(position));
            }
        }

        public void update(MockSchool school) {
            nameSchoolTextview.setText(school.getName());
            createdYearTextview.setText(String.valueOf(school.getYear()));
            rebindProgress(school.getSubCriterias(), progressTextView, progressBar);
        }

    }

    public interface Callback {
        void onSchoolClicked(MockSchool school);
    }
}