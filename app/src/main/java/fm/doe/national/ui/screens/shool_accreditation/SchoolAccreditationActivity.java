package fm.doe.national.ui.screens.shool_accreditation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.doe.national.R;
import fm.doe.national.ui.screens.menu.base.MenuDrawerActivity;
import fm.doe.national.ui.screens.menu.base.MenuDrawerPresenter;
import fm.doe.national.ui.screens.search.base.BaseSearchActivity;

/**
 * Created by Alexander Chibirev on 8/10/2018.
 */

public class SchoolAccreditationActivity extends MenuDrawerActivity implements SchoolAccreditationView {

    @InjectPresenter
    SchoolAccreditationPresenter schoolAccreditationPresenter;

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    public static Intent createIntent(Context context) {
        return new Intent(context, SchoolAccreditationActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_accreditation);
        ButterKnife.bind(this);
    }

    @Override
    protected MenuDrawerPresenter getPresenter() {
        return schoolAccreditationPresenter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

}
