package fm.doe.national.ui.screens.survey_creation;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.omegar.mvp.presenter.InjectPresenter;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import fm.doe.national.R;
import fm.doe.national.core.data.model.School;
import fm.doe.national.core.ui.screens.base.BaseActivity;
import fm.doe.national.core.ui.screens.base.BaseAdapter;
import fm.doe.national.core.ui.views.BottomNavigatorView;
import fm.doe.national.core.utils.DateUtils;
import fm.doe.national.survey.ui.SurveyActivity;

public class CreateSurveyActivity extends BaseActivity implements
        CreateSurveyView,
        BaseAdapter.OnItemClickListener<School>,
        SearchView.OnQueryTextListener, DatePickerDialog.OnDateSetListener, BottomNavigatorView.Listener {

    private final SchoolsListAdapter adapter = new SchoolsListAdapter(this);

    @BindView(R.id.textview_year)
    TextView yearTextView;

    @BindView(R.id.recyclerview_schools)
    RecyclerView schoolsRecycler;

    @BindView(R.id.bottomnavigatorview)
    BottomNavigatorView bottomNavigatorView;

    @InjectPresenter
    CreateSurveyPresenter presenter;

    public static Intent createIntent(Context context) {
        return new Intent(context, CreateSurveyActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        setTitle(R.string.label_create_survey);

        schoolsRecycler.setAdapter(adapter);
        bottomNavigatorView.setListener(this);
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
    public void setStartDate(Date date) {
        yearTextView.setText(DateUtils.formatMonthYear(date));
    }

    @Override
    public void navigateToSurvey() {
        startActivity(new Intent(this, SurveyActivity.class));
    }

    @Override
    public void showDatePicker(int currentYear, int currentMonth, int currentDay) {
        new DatePickerDialog(this, this, currentYear, currentMonth, currentDay)
                .show();
    }

    @Override
    public void onItemClick(School item) {
        presenter.onSchoolPicked(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_surveys, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        // SearchView is not MATCH_PARENT in Toolbar, it's bounded by MaxWidth
        // To make it MATCH_PARENT just set MaxValue to MAX_INT
        searchView.setMaxWidth(Integer.MAX_VALUE);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        presenter.onSearchQueryChanged(newText);
        return true;
    }

    @OnClick(R.id.layout_date)
    public void onEditButtonClick() {
        presenter.onEditButtonClick();
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        presenter.onDatePicked(year, month, dayOfMonth);
    }

    @Override
    public void setContinueEnabled(boolean isEnabled) {
        bottomNavigatorView.setNextButtonEnabled(isEnabled);
    }

    @Override
    public void onPrevPressed() {
        // nothing
    }

    @Override
    public void onNextPressed() {
        presenter.onContinuePressed();
    }
}
