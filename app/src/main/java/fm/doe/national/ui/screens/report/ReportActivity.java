package fm.doe.national.ui.screens.report;

import com.omegar.mvp.presenter.InjectPresenter;

import fm.doe.national.R;
import fm.doe.national.ui.screens.base.BaseActivity;

public class ReportActivity extends BaseActivity implements ReportView {

    @InjectPresenter
    ReportPresenter presenter;

    @Override
    protected int getContentView() {
        return R.layout.activity_report;
    }
}
