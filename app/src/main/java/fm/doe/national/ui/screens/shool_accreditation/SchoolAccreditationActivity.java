package fm.doe.national.ui.screens.shool_accreditation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.ButterKnife;
import fm.doe.national.R;
import fm.doe.national.ui.screens.menu.drawer.BaseDrawerActivity;

/**
 * Created by Alexander Chibirev on 8/10/2018.
 */

public class SchoolAccreditationActivity extends BaseDrawerActivity implements SchoolAccreditationView {

    @InjectPresenter
    SchoolAccreditationPresenter schoolAccreditationPresenter;

    public static Intent createIntent(Context context) {
        return new Intent(context, SchoolAccreditationActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_accreditation);
        ButterKnife.bind(this);
    }

}
