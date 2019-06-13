package fm.doe.national.ui.screens.settings;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.omega_r.libs.omegaintentbuilder.OmegaIntentBuilder;
import com.omega_r.libs.omegarecyclerview.BaseListAdapter;
import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.presenter.InjectPresenter;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.core.preferences.entities.AppRegion;
import fm.doe.national.core.ui.screens.base.BaseActivity;
import fm.doe.national.core.ui.views.InputDialogFragment;
import fm.doe.national.ui.screens.settings.items.Item;

public class SettingsActivity extends BaseActivity implements SettingsView, BaseListAdapter.OnItemClickListener<Item> {

    private static final int REQUEST_CODE_GALLERY = 201;
    private static final String TAG_INPUT_DIALOG = "TAG_INPUT_DIALOG";

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
        presenter.onItemPressed(item);
    }

    @Override
    public void showInputDialog(@Nullable Text title, @Nullable Text existingText, InputListener listener) {
        InputDialogFragment dialog = InputDialogFragment.create(title, existingText);
        dialog.setListener(listener::onInput);
        dialog.show(getSupportFragmentManager(), TAG_INPUT_DIALOG);
    }

    @Override
    public void showRegionSelector(RegionListener listener) {
        final BottomSheetDialog bsd = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.sheet_app_context, null);
        View fcmItemView = sheetView.findViewById(R.id.textview_fcm);
        View rmiItemView = sheetView.findViewById(R.id.textview_rmi);
        TextView titleTextView = sheetView.findViewById(R.id.textview_title);
        titleTextView.setText(R.string.title_choose_context);
        fcmItemView.setOnClickListener((v) -> {
            listener.onRegionSelected(AppRegion.FCM);
            bsd.dismiss();
        });
        rmiItemView.setOnClickListener((v) -> {
            listener.onRegionSelected(AppRegion.RMI);
            bsd.dismiss();
        });
        bsd.setContentView(sheetView);
        bsd.show();
    }
}
