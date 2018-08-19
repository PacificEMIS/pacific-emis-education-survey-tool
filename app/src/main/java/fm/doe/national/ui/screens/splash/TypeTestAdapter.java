package fm.doe.national.ui.screens.splash;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.School;
import fm.doe.national.ui.screens.base.BaseRecyclerAdapter;

/**
 * Created by Alexander Chibirev on 8/19/2018.
 */

public class TypeTestAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseViewHolder> {

    private List<School> schools = new ArrayList<>();

    @Nullable
    private Callback callback;

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TypeTestViewHolder(inflateView(parent, R.layout.item_type_test));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        ((TypeTestViewHolder) holder).update(schools.get(position));
    }

    @Override
    public int getItemCount() {
        return schools.size();
    }

    public void update(List<School> schools) {
        this.schools = schools;
        notifyDataSetChanged();
    }

    public void setCallback(@Nullable Callback callback) {
        this.callback = callback;
    }

    class TypeTestViewHolder extends BaseViewHolder {

        @BindView(R.id.textview_type_test)
        TextView typeTestTextview;


        public TypeTestViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onClick(int position) {
            //TODO changed logic after add correct type test model
            if (callback != null) {
                callback.onTypeTestClicked();
            }
        }

        public void update(School school) {
            typeTestTextview.setText(school.getName());
        }

    }

    public interface Callback {
        void onTypeTestClicked();
    }


}
