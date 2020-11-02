package org.pacific_emis.surveys.ui.screens.region;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;
import com.omegar.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindView;
import org.pacific_emis.surveys.R;
import org.pacific_emis.surveys.core.preferences.entities.AppRegion;
import org.pacific_emis.surveys.core.ui.screens.base.BaseActivity;
import org.pacific_emis.surveys.core.ui.views.BottomNavigatorView;
import org.pacific_emis.surveys.ui.screens.menu.MainMenuActivity;

public class ChooseRegionActivity extends BaseActivity implements
        ChooseRegionView,
        BottomNavigatorView.Listener,
        BaseListAdapter.OnItemClickListener<AppRegion> {

    @InjectPresenter
    ChooseRegionPresenter presenter;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.bottomnavigatorview)
    BottomNavigatorView bottomNavigatorView;

    private final RegionsAdapter adapter = new RegionsAdapter(this);

    public static Intent createIntent(Context context) {
        return new Intent(context, ChooseRegionActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bottomNavigatorView.setListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_choose_region;
    }

    @Override
    public void setRegions(List<AppRegion> regions) {
        adapter.setItems(regions);
    }

    @Override
    public void navigateToMenu() {
        startActivity(MainMenuActivity.createIntent(this));
        finishAffinity();
    }

    @Override
    public void onPrevPressed() {
        // nothing
    }

    @Override
    public void onNextPressed() {
        presenter.onContinuePressed();
    }

    @Override
    public void onItemClick(AppRegion item) {
        presenter.onRegionSelected(item);
    }
}
