package fm.doe.national.remote_storage.ui.default_storage;

import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import fm.doe.national.core.ui.screens.base.BaseView;

@StateStrategyType(OneExecutionStateStrategy.class)
public interface DefaultStorageView extends BaseView {

    void showPicker();

    void close();

}
