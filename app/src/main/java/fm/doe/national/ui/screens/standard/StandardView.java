package fm.doe.national.ui.screens.standard;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import java.util.List;

import fm.doe.national.mock.MockCriteria;
import fm.doe.national.mock.MockStandard;
import fm.doe.national.ui.screens.base.BaseView;

public interface StandardView extends BaseView {
    void bindGlobalInfo(String title, @DrawableRes int icon);
    void bindCriterias(@NonNull List<MockCriteria> criterias);
    void bindProgress(int answered, int total);
    void bindPrevStandard(String title, @DrawableRes int icon);
    void bindNextStandard(String title, @DrawableRes int icon);
    void navigateToOtherStandard(MockStandard otherStandard);
}
