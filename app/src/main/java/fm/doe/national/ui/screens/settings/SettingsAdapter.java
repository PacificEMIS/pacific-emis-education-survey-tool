package fm.doe.national.ui.screens.settings;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.cloud.model.CloudAccountData;
import fm.doe.national.core.ui.screens.base.BaseAdapter;

public class SettingsAdapter extends BaseAdapter<CloudAccountData> {

    @Nullable
    private Callback callback = null;

    public void setCallback(@Nullable Callback callback) {
        this.callback = callback;
    }

    @Override
    protected AccountViewHolder provideViewHolder(ViewGroup parent) {
        return new AccountViewHolder(parent);
    }

    class AccountViewHolder extends ViewHolder {

        @BindView(R.id.imageview_cloud_icon)
        ImageView iconImageView;

        @BindView(R.id.imegaview_done_icon)
        ImageView exportIconImageView;

        @BindView(R.id.textview_name)
        TextView nameTextView;

        @BindView(R.id.textview_email)
        TextView emailTextView;

        @BindView(R.id.textview_export)
        TextView exportTextView;

        @BindView(R.id.textview_import_schools)
        TextView importSchoolsTextView;

        @BindView(R.id.textview_import_survey)
        TextView importSurveyTextView;

        @BindView(R.id.textview_folder_path)
        TextView folderPathTextView;

        @BindView(R.id.textview_choose_folder)
        TextView chooseFolderTextView;

        AccountViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_account);
        }

        @Override
        protected void onBind(CloudAccountData item) {
            switch (item.getType()) {
                case DRIVE:
                    iconImageView.setImageResource(R.drawable.ic_google_drive);
                    nameTextView.setText(R.string.account_drive);
                    break;
                case DROPBOX:
                    iconImageView.setImageResource(R.drawable.ic_dropbox);
                    nameTextView.setText(R.string.account_dropbox);
                    break;
            }
            folderPathTextView.setText(item.getExportPath());

            if (item.isDefault()) {
                exportIconImageView.setVisibility(View.VISIBLE);
                exportTextView.setActivated(true);
                exportTextView.setOnClickListener(null);
                exportTextView.setText(R.string.default_export);
            } else {
                exportIconImageView.setVisibility(View.GONE);
                exportTextView.setActivated(false);
                exportTextView.setOnClickListener(this);
                exportTextView.setText(R.string.change_default_export);
            }

            importSchoolsTextView.setOnClickListener(this);
            importSurveyTextView.setOnClickListener(this);
            chooseFolderTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (callback != null) {
                switch (v.getId()) {
                    case R.id.textview_import_schools:
                        callback.onImportSchoolsClick(getItem());
                        break;
                    case R.id.textview_import_survey:
                        callback.onImportSurveyClick(getItem());
                        break;
                    case R.id.textview_choose_folder:
                        callback.onChooseFolderClick(getItem());
                        break;
                    case R.id.textview_export:
                        callback.onChooseDefaultClick(getItem());
                        break;
                }
            }
        }
    }

    public interface Callback {
        void onImportSchoolsClick(CloudAccountData viewData);

        void onImportSurveyClick(CloudAccountData viewData);

        void onChooseFolderClick(CloudAccountData viewData);

        void onChooseDefaultClick(CloudAccountData viewData);
    }
}
