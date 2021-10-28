package org.pacific_emis.surveys.ui.screens.surveys;

import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import org.pacific_emis.surveys.R;
import org.pacific_emis.surveys.core.data.model.Survey;
import org.pacific_emis.surveys.core.preferences.entities.UploadState;
import org.pacific_emis.surveys.core.ui.screens.base.BaseAdapter;
import org.pacific_emis.surveys.core.utils.ViewUtils;

public class SurveysAdapter extends BaseAdapter<Survey> {

    private boolean isExportEnabled;
    private MenuItemClickListener menuItemClickListener;

    public SurveysAdapter(OnItemClickListener<Survey> clickListener, MenuItemClickListener menuItemClickListener) {
        super(clickListener);
        this.menuItemClickListener = menuItemClickListener;
    }

    @Override
    protected SchoolAccreditationViewHolder provideViewHolder(ViewGroup parent) {
        return new SchoolAccreditationViewHolder(parent);
    }

    public void setExportEnabled(boolean exportEnabled) {
        isExportEnabled = exportEnabled;
        notifyDataSetChanged();
    }

    protected class SchoolAccreditationViewHolder extends ViewHolder implements PopupMenu.OnMenuItemClickListener {

        @BindView(R.id.textview_id_school)
        TextView schoolIdTextView;

        @BindView(R.id.textview_name_school)
        TextView nameSchoolTextView;

        @BindView(R.id.textview_progress)
        TextView progressTextView;

        @BindView(R.id.textview_creation_date)
        TextView creationDateTextView;

        @BindView(R.id.progressbar)
        ProgressBar progressBar;

        @BindView(R.id.button_more)
        ImageButton moreButton;

        @BindView(R.id.imageview_upload_to_cloud)
        ImageView uploadState;

        SchoolAccreditationViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_survey);
        }

        @Override
        public void onBind(Survey item) {
            schoolIdTextView.setText(item.getSchoolId());
            nameSchoolTextView.setText(item.getSchoolName());
            creationDateTextView.setText(item.getSurveyTag());
            uploadState.setImageResource(getUploadState(item.getUploadState()));

            ViewUtils.rebindProgress(item.getProgress(), progressTextView, progressBar);
        }

        private int getUploadState(UploadState uploadState) {
            switch (uploadState) {
                case IN_PROGRESS:
                    return R.drawable.ic_in_progress_synced;
                case SUCCESSFULLY:
                    return R.drawable.ic_successfully_synced;
                default:
                    return R.drawable.ic_not_synced;
            }
        }

        @OnClick(R.id.button_more)
        void onMoreButtonClick() {
            PopupMenu popupMenu = new PopupMenu(getContext(), moreButton, Gravity.END);
            popupMenu.inflate(needToShowExport() ? R.menu.menu_survey_with_export : R.menu.menu_survey_without_export);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            return menuItemClickListener.onMenuItemClick(item, getItem());
        }

        private boolean needToShowExport() {
            return isExportEnabled;
        }
    }

    public interface MenuItemClickListener {
        boolean onMenuItemClick(MenuItem menuItem, Survey survey);
    }
}