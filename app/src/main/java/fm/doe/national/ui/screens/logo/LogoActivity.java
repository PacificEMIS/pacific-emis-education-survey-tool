package fm.doe.national.ui.screens.logo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.omega_r.libs.omegatypes.image.Image;
import com.omegar.mvp.presenter.InjectPresenter;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import fm.doe.national.R;
import fm.doe.national.core.ui.screens.base.BaseActivity;
import fm.doe.national.core.utils.ViewUtils;

public class LogoActivity extends BaseActivity implements LogoView {

    private static final String MIME_TYPE_ANY_IMAGE = "image/*";
    private static final int REQUEST_CODE_GALLERY = 201;

    @InjectPresenter
    LogoPresenter presenter;

    @BindView(R.id.imageview_logo)
    ImageView logoImageView;

    public static Intent createIntent(Context context) {
        return new Intent(context, LogoActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.label_logo);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_logo;
    }

    @Override
    public void setLogo(Image logo) {
        ViewUtils.setImageTo(logoImageView, logo);
    }

    @Override
    public void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT)
                .addCategory(Intent.CATEGORY_OPENABLE)
                .setType(MIME_TYPE_ANY_IMAGE);
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_GALLERY:
                if (resultCode == RESULT_OK && data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        presenter.onImagePicked(bitmap);
                        return;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @OnClick(R.id.button_default)
    void onSetDefaultPressed() {
        presenter.onSetDefaultPressed();
    }

    @OnClick(R.id.button_change)
    void onChangeLogoPressed() {
        presenter.onChangeLogoPressed();
    }
}
