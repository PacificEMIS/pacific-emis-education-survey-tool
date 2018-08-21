package fm.doe.national.ui.screens.menu.base;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.data.data_source.models.School;
import fm.doe.national.ui.screens.base.BasePresenter;

/**
 * Created by Alexander Chibirev on 8/10/2018.
 */

abstract public class MenuPresenter<V extends MenuView> extends BasePresenter<V> {

    public MenuPresenter() {
        List<School> schools = new ArrayList<>();
        schools.add(new School() {
            @Override
            public String getId() {
                return "test";
            }

            @Override
            public String getName() {
                return "School accreditation";
            }
        });
        schools.add(new School() {
            @Override
            public String getId() {
                return "test1";
            }

            @Override
            public String getName() {
                return "School data verification";
            }
        });
        schools.add(new School() {
            @Override
            public String getId() {
                return "test1";
            }

            @Override
            public String getName() {
                return "Monitoring and evalution";
            }
        });
        getViewState().setTests(schools);
    }

    public void onTypeTestClicked() {
        //TODO changed on correct
        getViewState().showSchoolAccreditationScreen();
    }

    public void onLogoClicked() {
        getViewState().pickPhotoFromGallery();
    }

}
