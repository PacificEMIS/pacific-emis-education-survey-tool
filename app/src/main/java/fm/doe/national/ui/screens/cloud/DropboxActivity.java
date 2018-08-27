package fm.doe.national.ui.screens.cloud;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.dropbox.core.android.Auth;
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.io.Serializable;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.cloud.dropbox.BrowsingTreeObject;
import fm.doe.national.ui.adapters.FilePickerAdapter;
import fm.doe.national.ui.listeners.FilePickerListener;
import fm.doe.national.ui.screens.base.BaseActivity;

public class DropboxActivity extends BaseActivity implements DropboxView, View.OnClickListener, FilePickerListener {

    private static final int ACTION_DEFAULT = -1;
    private static final int ACTION_AUTH = 0;
    private static final int ACTION_OPEN_FILE = 1;
    private static final int ACTION_SELECT_FOLDER = 2;

    private static final String EXTRA_ACTION = "EXTRA_ACTION";
    private static final String EXTRA_BROWSING_ROOT = "EXTRA_BROWSING_ROOT";

    @InjectPresenter
    DropboxPresenter presenter;

    @ProvidePresenter
    public DropboxPresenter providePresenter() {
        return new DropboxPresenter(getActionFromIntent(getIntent()), getBrowsingRootFromIntent(getIntent()));
    }

    @BindView(R.id.constraintlayout_picker)
    ConstraintLayout pickerView;

    @BindView(R.id.imageview_back)
    ImageView backImageView;

    @BindView(R.id.textview_title)
    TextView titleTextView;

    @BindView(R.id.textview_path)
    TextView pathTextView;

    @BindView(R.id.recyclerview_browse)
    OmegaRecyclerView browseRecycler;

    @BindView(R.id.button_confirm)
    Button confirmButton;

    @BindView(R.id.button_cancel)
    Button cancelButton;

    private boolean haveBeenPaused = false;
    private FilePickerAdapter adapter = new FilePickerAdapter();
    private BrowsingTreeObject currentBrowsingItem;

    @NonNull
    public static Intent createIntent(@NonNull Context context, Action action) {
        int extraAction = ACTION_DEFAULT;
        switch (action) {
            case AUTH:
                extraAction = ACTION_AUTH;
                break;
            case PICK_FILE:
                extraAction = ACTION_OPEN_FILE;
                break;
            case PICK_FOLDER:
                extraAction = ACTION_SELECT_FOLDER;
                break;
        }
        return new Intent(context, DropboxActivity.class)
                .putExtra(EXTRA_ACTION, extraAction);
    }

    @NonNull
    public static Intent createPickerIntent(@NonNull Context context, Action action, BrowsingTreeObject treeRoot) {
        return createIntent(context, action).putExtra(EXTRA_BROWSING_ROOT, treeRoot);
    }

    private Action getActionFromIntent(@NonNull Intent intent) {
        int action = intent.getIntExtra(EXTRA_ACTION, ACTION_DEFAULT);
        switch (action) {
            case ACTION_AUTH:
                return Action.AUTH;
            case ACTION_OPEN_FILE:
                return Action.PICK_FILE;
            case ACTION_SELECT_FOLDER:
                return Action.PICK_FOLDER;
            default:
                throw new RuntimeException("DropboxActivity started with wrong action");
        }
    }

    @Nullable
    private BrowsingTreeObject getBrowsingRootFromIntent(@NonNull Intent intent) {
        Serializable serializableExtra = intent.getSerializableExtra(EXTRA_BROWSING_ROOT);
        if (serializableExtra == null) {
            return null;
        } else {
            return (BrowsingTreeObject) serializableExtra;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pickerView.setVisibility(View.GONE);
        confirmButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        backImageView.setOnClickListener(this);
        browseRecycler.setAdapter(adapter);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_dropbox;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (haveBeenPaused && !isFinishing()) {
            presenter.onViewResumedFromPause();
        }
        haveBeenPaused = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        haveBeenPaused = true;
    }

    @Override
    public void startAuthentication() {
        Auth.startOAuth2Authentication(this, getString(R.string.dropbox_api_app_key));
    }

    @Override
    public void die() {
        finish();
    }

    @Override
    public void showFilePicker(BrowsingTreeObject root) {
        showPicker(FilePickerAdapter.Kind.FILE, root);
    }

    @Override
    public void showFolderPicker(BrowsingTreeObject root) {
        showPicker(FilePickerAdapter.Kind.FOLDER, root);
    }

    private void showPicker(FilePickerAdapter.Kind kind, BrowsingTreeObject root) {
        pickerView.setVisibility(View.VISIBLE);

        titleTextView.setText(kind == FilePickerAdapter.Kind.FOLDER ? R.string.title_select_folder : R.string.title_select_file);

        if (kind == FilePickerAdapter.Kind.FILE) {
            confirmButton.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);
        }

        adapter.setKind(kind);
        adapter.setListener(this);

        currentBrowsingItem = root;
        updateUi();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_confirm:
                presenter.onBrowsingItemPicked(currentBrowsingItem);
                break;
            case R.id.button_cancel:
                presenter.pickerCancelled();
                break;
            case R.id.imageview_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        BrowsingTreeObject possibleParent = currentBrowsingItem.getParent();
        if (possibleParent != null) {
            currentBrowsingItem = possibleParent;
            updateUi();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onBrowsingObjectPicked(BrowsingTreeObject object) {
        if (object.isDirectory()) {
            currentBrowsingItem = object;
            updateUi();
        } else {
            presenter.onBrowsingItemPicked(object);
        }
    }

    private void updateUi() {
        adapter.setItem(currentBrowsingItem);
        pathTextView.setText(currentBrowsingItem.getPath());
        backImageView.setVisibility(currentBrowsingItem.getParent() != null ? View.VISIBLE : View.GONE);
    }
}
