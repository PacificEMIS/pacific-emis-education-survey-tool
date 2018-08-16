package fm.doe.national.ui.screens.standard;

import java.util.List;

import fm.doe.national.mock.MockCriteria;
import fm.doe.national.ui.screens.base.BaseView;

public interface StandardView extends BaseView {
    void bindCriterias(List<MockCriteria> criterias);
}
