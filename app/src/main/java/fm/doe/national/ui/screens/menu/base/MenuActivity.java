package fm.doe.national.ui.screens.menu.base;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.omega_r.libs.omegaintentbuilder.OmegaIntentBuilder;
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.School;
import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.ui.screens.shool_accreditation.SchoolAccreditationActivity;
import fm.doe.national.ui.screens.splash.TypeTestAdapter;

/**
 * Created by Alexander Chibirev on 8/10/2018.
 */
public abstract class MenuActivity extends BaseActivity implements MenuView, TypeTestAdapter.Callback {

    private static final int REQUEST_CODE_GALLERY = 201;

    protected abstract MenuPresenter getPresenter();

    protected TypeTestAdapter typeTestAdapter = new TypeTestAdapter();

    @BindView(R.id.drawer_recyclerview)
    protected OmegaRecyclerView recyclerView;

    @BindView(R.id.imageview_logo)
    protected CircleImageView logoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        typeTestAdapter.setCallback(this);
        recyclerView.setAdapter(typeTestAdapter);
    }

    @OnClick(R.id.imageview_logo)
    public void onLogoClicked() {
        getPresenter().onLogoClicked();
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
            pickImage(data);
        }
    }

    private void pickImage(Intent data) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
            logoImageView.setImageBitmap(bitmap);
            bitmap.recycle();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public void showSchoolAccreditationScreen() {
        Intent intent = SchoolAccreditationActivity.createIntent(this);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    @Override //TODO changed logic after add correct type test model
    public void setTests(List<School> schools) {
        typeTestAdapter.update(schools);
    }

    @Override
    public void onTypeTestClicked() {
        //TODO changed logic after add correct type test model
        getPresenter().onTypeTestClicked();
    }
}
