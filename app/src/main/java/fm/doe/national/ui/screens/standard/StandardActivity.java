package fm.doe.national.ui.screens.standard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.doe.national.R;
import fm.doe.national.mock.MockCriteria;
import fm.doe.national.ui.adapters.CriteriaAdapter;
import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.ui.screens.presenters.StandardPresenter;

public class StandardActivity extends BaseActivity implements StandardView {

    @BindView(R.id.recyclerview_criterias)
    RecyclerView criteriasRecycler;

    @InjectPresenter
    StandardPresenter presenter;

    private CriteriaAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard);
        ButterKnife.bind(this);
        recyclerAdapter = new CriteriaAdapter();
        criteriasRecycler.setAdapter(recyclerAdapter);
    }

    @Override
    public void bindCriterias(List<MockCriteria> criterias) {
        recyclerAdapter.setCriterias(criterias);
    }
}
