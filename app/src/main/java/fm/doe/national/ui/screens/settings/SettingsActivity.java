package fm.doe.national.ui.screens.settings;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.recyclerview.widget.RecyclerView;

import com.omega_r.libs.omegaintentbuilder.OmegaIntentBuilder;
import com.omega_r.libs.omegarecyclerview.BaseListAdapter;
import com.omegar.mvp.presenter.InjectPresenter;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.core.ui.screens.base.BaseActivity;
import fm.doe.national.ui.screens.settings.items.Item;

public class SettingsActivity extends BaseActivity implements SettingsView, BaseListAdapter.OnItemClickListener<Item> {

    private static final int REQUEST_CODE_GALLERY = 201;

    private final SettingsAdapter adapter = new SettingsAdapter(this);

    @InjectPresenter
    SettingsPresenter presenter;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    public static Intent createIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        setTitle(R.string.label_settings);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_settings;
    }

    @Override
    public void pickPhotoFromGallery() {
        OmegaIntentBuilder.from(this)
                .pick()
                .image()
                .multiply(false)
                .createIntentHandler(this)
                .startActivityForResult(REQUEST_CODE_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                presenter.onImagePicked(bitmap);
                return;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setOptions(List<Item> options) {
        adapter.setItems(options);
    }

    @Override
    public void onItemClick(Item item) {

    }
}
