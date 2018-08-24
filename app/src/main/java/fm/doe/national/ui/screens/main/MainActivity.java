package fm.doe.national.ui.screens.main;

import android.annotation.SuppressLint;
import android.os.Bundle;

import java.util.Collections;

import javax.inject.Inject;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.R;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.db.OrmLiteSchool;
import fm.doe.national.ui.screens.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Inject
    DataSource dataSource;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MicronesiaApplication.getAppComponent().inject(this);

        OrmLiteSchool testSchool = new OrmLiteSchool("1", "school");
        dataSource.addSchools(Collections.singletonList(testSchool)).subscribe();

        dataSource.createNewSchoolAccreditationPassing(2018, testSchool).subscribe(schoolAccreditation
                -> dataSource.requestSchoolAccreditationPassings().subscribe(schoolAccreditationPassings -> {
            Answer answer = schoolAccreditationPassings.get(0).getSchoolAccreditation().getGroupStandards().get(0).getStandards().get
                    (0).getCriterias().get(0).getSubCriterias().get(0).getAnswer();

            System.out.println();
        }));
    }
}
