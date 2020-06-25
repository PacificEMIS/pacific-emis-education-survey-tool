package fm.doe.national.report_core.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import fm.doe.national.report_core.domain.ReportInteractor;
import fm.doe.national.report_core.ui.base.BaseReportFragment;

public class ReportPage {

    private Class<? extends BaseReportFragment> fragmentClass;
    private ReportInteractor interactorInstance;

    public ReportPage(Class<? extends BaseReportFragment> fragmentClass, ReportInteractor interactorInstance) {
        this.fragmentClass = fragmentClass;
        this.interactorInstance = interactorInstance;
    }

    public BaseReportFragment buildFragment() {
        try {
            Constructor<? extends BaseReportFragment> constructor = fragmentClass.getConstructor(ReportInteractor.class);
            return constructor.newInstance(interactorInstance);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

}
