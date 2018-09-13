package fm.doe.national.ui.screens.cloud;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.dropbox.core.android.Auth;

import butterknife.BindView;
import fm.doe.national.BuildConfig;
import fm.doe.national.R;
import fm.doe.national.data.cloud.dropbox.BrowsingTreeObject;
import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.ui.screens.base.BaseAdapter;

public class DropboxActivity extends BaseActivity implements
        DropboxView,
        View.OnClickListener,
        BaseAdapter.OnItemClickListener<BrowsingTreeObject> {

    private static final int ACTION_DEFAULT = -1;
    private static final int ACTION_AUTH = 0;
    private static final int ACTION_OPEN_FILE = 1;
    private static final int ACTION_SELECT_FOLDER = 2;

    private static final String EXTRA_ACTION = "EXTRA_ACTION";
    private static final String EXTRA_BROWSING_ROOT = "EXTRA_BROWSING_ROOT";

    @BindView(R.id.constraintlayout_picker)
    ConstraintLayout pickerView;

    @BindView(R.id.imageview_back)
    ImageView backImageView;

    @BindView(R.id.textview_title)
    TextView titleTextView;

    @BindView(R.id.textview_path)
    TextView pathTextView;

    @BindView(R.id.recyclerview_browse)
    RecyclerView browseRecycler;

    @BindView(R.id.button_confirm)
    Button confirmButton;

    @BindView(R.id.button_cancel)
    Button cancelButton;

    @InjectPresenter
    DropboxPresenter presenter;

    private boolean haveBeenPaused = false;
    private FilePickerListAdapter adapter = new FilePickerListAdapter();
    private BrowsingTreeObject currentBrowsingItem;


    public static Intent createAuthIntent(@NonNull Context context) {
        return createIntent(context, ACTION_AUTH, null);
    }

    public static Intent createOpenFileIntent(@NonNull Context context, @NonNull BrowsingTreeObject treeRoot) {
        return createIntent(context, ACTION_OPEN_FILE, treeRoot);
    }

    public static Intent createSelectFolderIntent(@NonNull Context context, @NonNull BrowsingTreeObject treeRoot) {
        return createIntent(context, ACTION_SELECT_FOLDER, treeRoot);
    }

    @NonNull
    private static Intent createIntent(@NonNull Context context, int action, @Nullable BrowsingTreeObject treeRoot) {
        return new Intent(context, DropboxActivity.class)
                .putExtra(EXTRA_ACTION, action)
                .putExtra(EXTRA_BROWSING_ROOT, treeRoot);
    }

    private Action getActionFrom(@NonNull Intent intent) {
        int action = intent.getIntExtra(EXTRA_ACTION, ACTION_DEFAULT);
        switch (action) {
            case ACTION_AUTH:
                return Action.AUTH;
            case ACTION_OPEN_FILE:
            case ACTION_SELECT_FOLDER:
                return Action.PICK;
            default:
                throw new RuntimeException("Action not specified (" + action + " )");
        }
    }

    @Nullable
    private PickerType getPickerTypeFrom(@NonNull Intent intent) {
        int action = intent.getIntExtra(EXTRA_ACTION, ACTION_DEFAULT);
        switch (action) {
            case ACTION_AUTH:
                return null;
            case ACTION_OPEN_FILE:
                return PickerType.FILE;
            case ACTION_SELECT_FOLDER:
                return PickerType.FOLDER;
            default:
                throw new RuntimeException("Action not specified (" + action + " )");
        }
    }

    @ProvidePresenter
    public DropboxPresenter providePresenter() {
        Intent intent = getIntent();
        return new DropboxPresenter(getActionFrom(intent), getPickerTypeFrom(intent), getSerializableExtra(EXTRA_BROWSING_ROOT));
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
            presenter.checkAuthResult();
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
        Auth.startOAuth2Authentication(this, BuildConfig.DROPBOX_API_KEY);
    }

    @Override
    public void exit() {
        finish();
    }

    @Override
    public void showPicker(PickerType pickerType, BrowsingTreeObject root) {
        pickerView.setVisibility(View.VISIBLE);

        switch (pickerType) {
            case FILE:
                confirmButton.setVisibility(View.GONE);
                cancelButton.setVisibility(View.GONE);
                titleTextView.setText(R.string.title_select_file);
                break;
            case FOLDER:
                confirmButton.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.VISIBLE);
                titleTextView.setText(R.string.title_select_folder);
                break;
        }

        adapter.setPickerType(pickerType);
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
            presenter.pickerCancelled();
            super.onBackPressed();
        }
    }

    @Override
    public void onItemClick(BrowsingTreeObject item) {
        if (item.isDirectory()) {
            currentBrowsingItem = item;
            updateUi();
        } else {
            presenter.onBrowsingItemPicked(item);
        }
    }

    private void updateUi() {
        adapter.setItems(currentBrowsingItem.getChilds());
        pathTextView.setText(currentBrowsingItem.getPath());
        backImageView.setVisibility(currentBrowsingItem.getParent() != null ? View.VISIBLE : View.GONE);
    }
}
