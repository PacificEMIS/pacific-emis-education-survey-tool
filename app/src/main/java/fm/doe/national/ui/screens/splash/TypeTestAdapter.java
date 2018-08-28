package fm.doe.national.ui.screens.splash;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.School;
import fm.doe.national.ui.screens.base.BaseClickableAdapter;
import fm.doe.national.ui.screens.base.BaseRecyclerViewHolder;

public class TypeTestAdapter extends BaseClickableAdapter<School, TypeTestAdapter.TypeTestViewHolder> {

    @Override
    protected TypeTestViewHolder provideViewHolder(ViewGroup parent) {
        return new TypeTestViewHolder(parent);
    }

    class TypeTestViewHolder extends BaseRecyclerViewHolder<School> implements View.OnClickListener {

        @BindView(R.id.textview_type_test)
        TextView typeTestTextView;

        public TypeTestViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_type_test);
        }

        @Override
        public void onClick(View v) {
            //TODO changed logic after add correct type test model
            onItemClick(item);
        }

        @Override
        public void onBind() {
            typeTestTextView.setText(item.getName());
            itemView.setOnClickListener(this);
        }

    }
}
