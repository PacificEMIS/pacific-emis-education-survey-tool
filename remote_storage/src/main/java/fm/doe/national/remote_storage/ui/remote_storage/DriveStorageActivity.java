package fm.doe.national.remote_storage.ui.remote_storage;

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

import fm.doe.national.core.ui.screens.base.BaseActivity;
import fm.doe.national.remote_storage.R;
import fm.doe.national.remote_storage.data.model.GoogleDriveFileHolder;
import fm.doe.national.remote_storage.di.RemoteStorageComponentInjector;

public class DriveStorageActivity extends BaseActivity implements DriveStorageView, BaseListAdapter.OnItemClickListener<GoogleDriveFileHolder> {

    private final DriveStorageAdapter adapter = new DriveStorageAdapter(this);

    @InjectPresenter
    DriveStoragePresenter presenter;

    private RecyclerView recyclerView;
    private View contentView;
    private TextView contentTextView;

    public static Intent createIntent(Context context) {
        return new Intent(context, DriveStorageActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_drive);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setAdapter(adapter);
        contentTextView = findViewById(R.id.textview_content);
        contentView = findViewById(R.id.scrollview);
    }

    @ProvidePresenter
    DriveStoragePresenter providePresenter() {
        return new DriveStoragePresenter(RemoteStorageComponentInjector.getComponent(getApplication()));
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
    public void onItemClick(GoogleDriveFileHolder item) {
        presenter.onItemPressed(item);
    }
}
