package fm.doe.national.ui.screens.main;

import android.annotation.SuppressLint;
import android.os.Bundle;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.R;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.SchoolAccreditation;
import fm.doe.national.data.data_source.models.SubCriteria;
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
            Long passingId = schoolAccreditationPassings.get(0).getId();
            dataSource.requestGroupStandards(passingId).subscribe((groupStandards) -> {
                dataSource.requestStandards(passingId, groupStandards.get(1).getId()).subscribe(standards -> {
                    dataSource.requestCriterias(passingId, standards.get(0).getId()).subscribe(criterias -> {
                        dataSource.updateAnswer(passingId, criterias.get(1).getSubCriterias().get(1).getId(), Answer.State.POSITIVE).subscribe();
                        dataSource.requestGroupStandards(passingId).subscribe(groupStandards1 -> {
                            System.out.println();
                        });
                    });
                });
            });

            System.out.println();
        }));
    }
}
