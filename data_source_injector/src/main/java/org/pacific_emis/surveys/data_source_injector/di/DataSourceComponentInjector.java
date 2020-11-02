package org.pacific_emis.surveys.data_source_injector.di;

public class DataSourceComponentInjector {
    public static DataSourceComponent getComponent(Object injectionSource) {
        if (injectionSource instanceof DataSourceComponentProvider) {
            return ((DataSourceComponentProvider) injectionSource).provideDataSourceComponent();
        } else {
            throw new IllegalStateException("Injection source is not a DataSourceComponentProvider");
        }
    }
}
