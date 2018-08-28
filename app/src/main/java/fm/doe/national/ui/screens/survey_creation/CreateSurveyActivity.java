package fm.doe.national.ui.screens.survey_creation;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.School;
import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.ui.screens.base.BaseClickableAdapter;
import fm.doe.national.ui.screens.choose_category.ChooseCategoryActivity;

public class CreateSurveyActivity extends BaseActivity
        implements CreateSurveyView, BaseClickableAdapter.OnRecyclerItemClickListener<School> {

    @BindView(R.id.textview_year)
    TextView yearTextView;

    @BindView(R.id.imageview_edit)
    ImageView editImageView;

    @BindView(R.id.recyclerview_schools)
    OmegaRecyclerView schoolsRecycler;

    @InjectPresenter
    CreateSurveyPresenter presenter;

    private final SchoolsAdapter adapter = new SchoolsAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar();

        schoolsRecycler.setAdapter(adapter);
        adapter.setListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_create_survey;
    }

    @Override
    public void setSchools(List<School> schools) {
        adapter.setItems(schools);
    }

    @Override
    public void setYear(int year) {
        yearTextView.setText(String.valueOf(year));
    }

    @Override
    public void navigateToCategoryChooser(SchoolAccreditationPassing schoolAccreditationPassing) {
        startActivity(ChooseCategoryActivity.createIntent(this, schoolAccreditationPassing));
    }

    @Override
    public void onRecyclerItemClick(School item) {
        presenter.onSchoolPicked(item);
    }
}
