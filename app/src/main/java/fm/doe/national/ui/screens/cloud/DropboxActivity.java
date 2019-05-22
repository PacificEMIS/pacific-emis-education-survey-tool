package fm.doe.national.ui.screens.cloud;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.dropbox.core.android.Auth;
import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

import butterknife.BindView;
import fm.doe.national.BuildConfig;
import fm.doe.national.R;
import fm.doe.national.data.cloud.dropbox.BrowsingTreeObject;
import fm.doe.national.core.ui.screens.base.BaseActivity;
import fm.doe.national.core.ui.screens.base.BaseAdapter;

public class DropboxActivity extends BaseActivity implements
        DropboxView,
        BaseAdapter.OnItemClickListener<BrowsingTreeObject> {

    private static final int ACTION_DEFAULT = -1;
    private static final int ACTION_AUTH = 0;
    private static final int ACTION_OPEN_FILE = 1;
    private static final int ACTION_SELECT_FOLDER = 2;

    private static final String EXTRA_ACTION = "EXTRA_ACTION";
    private static final String EXTRA_BROWSING_ROOT = "EXTRA_BROWSING_ROOT";

    private float titleTextSizePrimary;
    private float titleTextSizeSecondary;

    @BindView(R.id.layout_picker)
    View pickerView;

    @BindView(R.id.textview_title)
    TextView titleTextView;

    @BindView(R.id.textview_path)
    TextView pathTextView;

    @BindView(R.id.recyclerview_browse)
    RecyclerView browseRecycler;

    @InjectPresenter
    DropboxPresenter presenter;

    private boolean haveBeenPaused = false;
    private FilePickerListAdapter adapter = new FilePickerListAdapter(this);
    private BrowsingTreeObject currentBrowsingItem;
    private MenuItem selectMenuItem;
    private boolean shouldShowConfirm;


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
    protected int getContentView() {
        return R.layout.activity_dropbox;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pickerView.setVisibility(View.GONE);
        browseRecycler.setAdapter(adapter);

        titleTextSizePrimary = getResources().getDimension(R.dimen.textsize_dropbox_title_primary);
        titleTextSizeSecondary = getResources().getDimension(R.dimen.textsize_dropbox_title_secondary);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dropbox, menu);
        selectMenuItem = menu.findItem(R.id.action_confirm);
        selectMenuItem.setVisible(shouldShowConfirm);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_confirm:
                presenter.onBrowsingItemPicked(currentBrowsingItem);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                titleTextView.setText(R.string.title_select_file);
                break;
            case FOLDER:
                shouldShowConfirm = true;
                titleTextView.setText(R.string.title_select_folder);
                break;
        }

        adapter.setPickerType(pickerType);

        currentBrowsingItem = root;
        updateUi();
    }

    @Override
    public void onBackPressed() {
        onUpPressed();
    }

    @Override
    public void onHomePressed() {
        onUpPressed();
    }

    private void onUpPressed() {
        BrowsingTreeObject possibleParent = currentBrowsingItem.getParent();
        if (possibleParent != null) {
            currentBrowsingItem = possibleParent;
            updateUi();
        } else {
            presenter.pickerCancelled();
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

        if (currentBrowsingItem.getParent() != null) {
            titleTextView.setTextSize(titleTextSizeSecondary);
            titleTextView.setAllCaps(true);
            pathTextView.setText(currentBrowsingItem.getPath());
            pathTextView.setVisibility(View.VISIBLE);
        } else {
            titleTextView.setTextSize(titleTextSizePrimary);
            titleTextView.setAllCaps(false);
            pathTextView.setVisibility(View.GONE);
        }
    }
}
