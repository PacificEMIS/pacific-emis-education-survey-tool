package fm.doe.national.ui.screens.shool_accreditation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.presenter.InjectPresenter;

import fm.doe.national.R;
import fm.doe.national.ui.screens.base.ButterKnifeFragment;

/**
 * Created by Alexander Chibirev on 8/10/2018.
 */

public class SchoolAccreditationFragment extends ButterKnifeFragment implements SchoolAccreditationView {

    @InjectPresenter
    SchoolAccreditationPresenter schoolAccreditationPresenter;

    public static SchoolAccreditationFragment newInstance() {
        return new SchoolAccreditationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_school_accreditation, container, false);
    }

}
