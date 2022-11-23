package org.pacific_emis.surveys.remote_storage.ui.remote_storage;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;
import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

import java.util.List;

import org.pacific_emis.surveys.core.di.CoreComponentInjector;
import org.pacific_emis.surveys.core.ui.screens.base.BaseActivity;
import org.pacific_emis.surveys.remote_storage.R;
import org.pacific_emis.surveys.remote_storage.data.model.GoogleDriveFileHolder;
import org.pacific_emis.surveys.remote_storage.di.RemoteStorageComponentInjector;

public class DriveStorageActivity extends BaseActivity implements
        DriveStorageView,
        BaseListAdapter.OnItemClickListener<GoogleDriveFileHolder>,
        BaseListAdapter.OnItemLongClickListener<GoogleDriveFileHolder> {

    private static final String EXTRA_IS_DEBUG_VIEWER = "EXTRA_IS_DEBUG_VIEWER";

    @InjectPresenter
    DriveStoragePresenter presenter;

    private RecyclerView recyclerView;
    private View contentView;
    private TextView contentTextView;
    private DriveStorageAdapter adapter;
    private Boolean isDebugViewer = null;

    public static Intent createIntent(Context context, boolean isDebugViewer) {
        return new Intent(context, DriveStorageActivity.class)
                .putExtra(EXTRA_IS_DEBUG_VIEWER, isDebugViewer);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isDebugViewer()) {
            if (presenter.isDeletingCloudFileMode()) {
                adapter = new DriveStorageAdapter(this, this);
            } else {
                adapter = new DriveStorageAdapter(this);
            }
        } else {
            adapter = new DriveStorageAdapter(this);
        }

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setAdapter(adapter);
        contentTextView = findViewById(R.id.textview_content);
        contentView = findViewById(R.id.scrollview);
    }

    @ProvidePresenter
    DriveStoragePresenter providePresenter() {
        Application app = getApplication();
        return new DriveStoragePresenter(
                RemoteStorageComponentInjector.getComponent(app),
                CoreComponentInjector.getComponent(app),
                isDebugViewer()
        );
    }

    private boolean isDebugViewer() {
        if (isDebugViewer == null) {
            isDebugViewer = getIntent().getBooleanExtra(EXTRA_IS_DEBUG_VIEWER, false);
        }

        return isDebugViewer;
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_drive_storage;
    }

    @Override
    public void setItems(List<GoogleDriveFileHolder> items) {
        recyclerView.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.GONE);
        adapter.setItems(items);
    }

    @Override
    public void setContent(String content) {
        recyclerView.setVisibility(View.GONE);
        contentView.setVisibility(View.VISIBLE);
        contentTextView.setText(content);
    }

    @Override
    public void onHomePressed() {
        presenter.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        presenter.onBackPressed();
    }

    @Override
    public void onItemClick(GoogleDriveFileHolder item) {
        presenter.onItemPressed(item);
    }

    @Override
    public void onItemLongClick(GoogleDriveFileHolder item) {
        presenter.onItemLongPressed(item);
    }

    @Override
    public void setParentName(String currentParentName) {
        if (currentParentName == null) {
            setTitle(R.string.title_drive);
        } else {
            setTitle(currentParentName);
        }
    }
}
