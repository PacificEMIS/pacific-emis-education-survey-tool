package fm.doe.national.ui.screens.menu.base;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.util.List;

import androidx.annotation.Nullable;
import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.core.ui.screens.base.BaseActivity;
import fm.doe.national.ui.screens.menu.MainMenuItem;
import fm.doe.national.ui.screens.menu.MenuListAdapter;
import fm.doe.national.ui.screens.all_surveys.AllSurveysActivity;
import fm.doe.national.core.utils.ViewUtils;

public abstract class MenuActivity extends BaseActivity implements MenuView {

    protected final MenuListAdapter menuAdapter = new MenuListAdapter(this::onRecyclerItemClick);

    @BindView(R.id.recyclerview_drawer)
    protected OmegaRecyclerView recyclerView;

    @Nullable
    @BindView(R.id.imageview_logo)
    protected ImageView logoImageView;

    protected abstract MenuPresenter getPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerView.setAdapter(menuAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPresenter().notifyReturnedFromBackground();
    }

    @Override
    public void navigateToSchoolAccreditationScreen() {
        Intent intent = AllSurveysActivity.createIntent(this);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void setItems(List<MainMenuItem> items) {
        menuAdapter.setItems(items);
    }

    public void onRecyclerItemClick(MainMenuItem item) {
        getPresenter().onTypeTestClicked(item);
    }

    @Override
    public void setLogo(String path) {
        if (logoImageView != null) ViewUtils.setImageTo(logoImageView, path);
    }
}
