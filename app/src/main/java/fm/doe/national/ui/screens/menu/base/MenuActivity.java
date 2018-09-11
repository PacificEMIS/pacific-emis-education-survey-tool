package fm.doe.national.ui.screens.menu.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;
import com.omega_r.libs.omegatypes.Text;

import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.ui.screens.menu.MenuListAdapter;
import fm.doe.national.ui.screens.school_accreditation.SchoolAccreditationActivity;
import fm.doe.national.utils.ViewUtils;

public abstract class MenuActivity extends BaseActivity implements MenuView {

    protected final MenuListAdapter menuAdapter = new MenuListAdapter();

    @BindView(R.id.recyclerview_drawer)
    protected OmegaRecyclerView recyclerView;

    @Nullable
    @BindView(R.id.imageview_logo)
    protected ImageView logoImageView;

    protected abstract MenuPresenter getPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menuAdapter.setListener(this::onRecyclerItemClick);
        recyclerView.setAdapter(menuAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPresenter().notifyReturnedFromBackground();
    }

    @Override
    public void navigateToSchoolAccreditationScreen() {
        Intent intent = SchoolAccreditationActivity.createIntent(this);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void setItems(List<Text> items) {
        menuAdapter.setItems(items);
    }

    public void onRecyclerItemClick(Text item) {
        getPresenter().onTypeTestClicked();
    }

    @Override
    public void setLogo(String path) {
        if (logoImageView != null) ViewUtils.setImageTo(logoImageView, path);
    }
}
