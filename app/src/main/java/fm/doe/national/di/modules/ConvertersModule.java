package fm.doe.national.di.modules;

import dagger.Module;

@Module(includes = {DatabaseHelperModule.class, GsonModule.class})
public class ConvertersModule {

}
