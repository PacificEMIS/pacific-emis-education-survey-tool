package fm.doe.national.ui.screens.menu.base;

import android.content.Intent;
import android.os.Bundle;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;
import com.omega_r.libs.omegatypes.Text;

import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import fm.doe.national.R;
import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.ui.screens.menu.MenuListAdapter;
import fm.doe.national.ui.screens.school_accreditation.SchoolAccreditationActivity;

public abstract class MenuActivity extends BaseActivity implements MenuView {

    protected final MenuListAdapter menuAdapter = new MenuListAdapter();

    @BindView(R.id.recyclerview_drawer)
    protected OmegaRecyclerView recyclerView;

    @BindView(R.id.imageview_logo)
    protected CircleImageView logoImageView;

    protected abstract MenuPresenter getPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menuAdapter.setListener(this::onRecyclerItemClick);
        recyclerView.setAdapter(menuAdapter);
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
}
