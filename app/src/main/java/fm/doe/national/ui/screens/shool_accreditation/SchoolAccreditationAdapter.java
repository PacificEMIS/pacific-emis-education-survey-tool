package fm.doe.national.ui.screens.shool_accreditation;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.models.survey.School;
import fm.doe.national.ui.screens.base.BaseRecyclerAdapter;

/**
 * Created by Alexander Chibirev on 8/16/2018.
 */
public class SchoolAccreditationAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseViewHolder> {

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SchoolViewHolder(inflateView(parent, R.layout.item_school));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class SchoolViewHolder extends BaseViewHolder {

        @BindView(R.id.textview_name_school)
        TextView NameSchoolTextview;

        public SchoolViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onClick(int position) {

        }


    }

    public interface Callback {
        void onSchoolClicked(School school);
    }
}