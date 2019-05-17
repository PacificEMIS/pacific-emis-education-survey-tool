package fm.doe.national.ui.screens.report.levels;

import android.view.ViewGroup;
import android.widget.TextView;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.doe.national.R;

public class EvalutaionFormsAdapter extends BaseListAdapter<AccreditationForm> {

    @Override
    protected ItemViewHolder provideViewHolder(ViewGroup parent) {
        return new ItemViewHolder(parent);
    }

    class ItemViewHolder extends ViewHolder {

        @BindView(R.id.textview_name)
        TextView nameTextView;

        @BindView(R.id.textview_obtained_score)
        TextView obtainedScoreTextView;

        @BindView(R.id.textview_multiplier)
        TextView multiplierTextView;

        @BindView(R.id.textview_final_score)
        TextView finalScoreTextView;

        public ItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_school_accreditation_level);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void onBind(AccreditationForm item) {
            item.getName().applyTo(nameTextView, null);
            obtainedScoreTextView.setText(String.valueOf(item.getObtainedScore()));
            multiplierTextView.setText(String.valueOf(item.getMultiplier()));
            finalScoreTextView.setText(String.valueOf(item.getFinalScore()));
        }
    }
}
