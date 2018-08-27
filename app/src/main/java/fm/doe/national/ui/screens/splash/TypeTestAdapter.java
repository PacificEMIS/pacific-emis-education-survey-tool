package fm.doe.national.ui.screens.splash;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.School;
import fm.doe.national.ui.screens.base.BaseRecyclerViewHolder;

public class TypeTestAdapter extends RecyclerView.Adapter<TypeTestAdapter.TypeTestViewHolder> {

    private List<School> schools = new ArrayList<>();

    @Nullable
    private Callback callback;

    @NonNull
    @Override
    public TypeTestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TypeTestViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeTestViewHolder holder, int position) {
        holder.bind(schools.get(position));
    }

    @Override
    public int getItemCount() {
        return schools.size();
    }

    public void setSchools(List<School> schools) {
        this.schools = schools;
        notifyDataSetChanged();
    }

    public void setCallback(@Nullable Callback callback) {
        this.callback = callback;
    }

    class TypeTestViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.textview_type_test)
        TextView typeTestTextView;


        public TypeTestViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_type_test);
        }

        @Override
        public void onClick(View v) {
            //TODO changed logic after add correct type test model
            if (callback != null) {
                callback.onTypeTestClicked();
            }
        }

        public void bind(School school) {
            typeTestTextView.setText(school.getName());
            itemView.setOnClickListener(this);
        }

    }

    public interface Callback {
        void onTypeTestClicked();
    }


}
