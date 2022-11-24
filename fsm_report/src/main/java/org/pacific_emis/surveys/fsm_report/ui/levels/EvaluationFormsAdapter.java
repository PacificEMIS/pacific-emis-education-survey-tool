package org.pacific_emis.surveys.fsm_report.ui.levels;

import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;

import org.pacific_emis.surveys.fsm_report.R;
import org.pacific_emis.surveys.fsm_report.model.AccreditationForm;

public class EvaluationFormsAdapter extends BaseListAdapter<AccreditationForm> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(parent);
    }

    class ItemViewHolder extends ViewHolder {

        private TextView nameTextView;
        private TextView obtainedScoreTextView;
        private TextView multiplierTextView;
        private TextView finalScoreTextView;

        public ItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_school_accreditation_level);
            bindViews();
        }

        private void bindViews() {
            nameTextView = findViewById(R.id.textview_name);
            obtainedScoreTextView = findViewById(R.id.textview_obtained_score);
            multiplierTextView = findViewById(R.id.textview_multiplier);
            finalScoreTextView = findViewById(R.id.textview_final_score);
        }

        @Override
        protected void onBind(AccreditationForm item) {
            item.getName().applyTo(nameTextView, null);
            obtainedScoreTextView.setText(String.valueOf(item.getObtainedScore()));
            multiplierTextView.setText(String.valueOf(item.getMultiplier()));
            finalScoreTextView.setText(EvaluationFormFormatter.formatTotalScore(item.getFinalScore()));
        }
    }
}
