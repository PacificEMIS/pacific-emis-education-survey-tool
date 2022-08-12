package org.pacific_emis.surveys.ui.screens.logs;

import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.TextView;

import org.pacific_emis.surveys.R;
import org.pacific_emis.surveys.core.data.model.SurveyLog;
import org.pacific_emis.surveys.core.preferences.entities.SurveyType;
import org.pacific_emis.surveys.core.ui.screens.base.BaseAdapter;

import butterknife.BindView;

public class LogsAdapter extends BaseAdapter<SurveyLog> {

    @Override
    protected LogsViewHolder provideViewHolder(ViewGroup parent) {
        return new LogsViewHolder(parent);
    }

    class LogsViewHolder extends ViewHolder {

        @BindView(R.id.textview_id_school)
        TextView schoolIdTextView;

        @BindView(R.id.textview_creation_date)
        TextView creationDate;

        @BindView(R.id.textview_name_school)
        TextView schoolName;

        @BindView(R.id.textview_created_user)
        TextView createdUser;

        @BindView(R.id.textview_survey_log_type)
        TextView surveyLogType;

        @BindView(R.id.textview_log_action)
        TextView logAction;

        LogsViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_logs);
        }

        @Override
        public void onBind(SurveyLog item) {
            schoolIdTextView.setText(item.getSchoolId());
            creationDate.setText(item.getSurveyTag());
            schoolName.setText(item.getSchoolName());
            createdUser.setText(item.getCreateUser());
            surveyLogType.setText(getSurveyTypeText(item.getSurveyType()));
            item.getLogAction().getName().applyTo(logAction);
            switch (item.getLogAction()) {
                case CREATED: {
                    logAction.setTextColor(Color.GREEN);
                    return;
                }
                case EDITED: {
                    logAction.setTextColor(Color.YELLOW);
                    return;
                }
                case DELETED: {
                    logAction.setTextColor(Color.RED);
                }
            }
        }

        private String getSurveyTypeText(SurveyType surveyType) {
            switch (surveyType) {
                case SCHOOL_ACCREDITATION:
                    return getString(R.string.title_survey_type_school_accreditation);
                case WASH:
                    return getString(R.string.title_survey_type_wash);
            }
            throw new IllegalStateException();
        }

    }
}
