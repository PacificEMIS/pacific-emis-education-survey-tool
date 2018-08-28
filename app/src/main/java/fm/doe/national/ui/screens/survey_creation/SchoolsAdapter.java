package fm.doe.national.ui.screens.survey_creation;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.School;
import fm.doe.national.ui.screens.base.BaseClickableAdapter;
import fm.doe.national.ui.screens.base.BaseRecyclerViewHolder;

public class SchoolsAdapter extends BaseClickableAdapter<School, SchoolsAdapter.SchoolViewHolder> {

    @Override
    protected SchoolViewHolder provideViewHolder(ViewGroup parent) {
        return new SchoolViewHolder(parent);
    }

    class SchoolViewHolder extends BaseRecyclerViewHolder<School> implements View.OnClickListener {

        @BindView(R.id.textview_name)
        TextView nameTextView;

        SchoolViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_school);
        }

        @Override
        public void onBind() {
            nameTextView.setText(item.getName());
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClick(item);
        }
    }
}
