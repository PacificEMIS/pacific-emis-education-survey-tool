package com.micronesia.di;


import com.micronesia.di.modules.ContextModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ContextModule.class)
public interface AppComponent {

    

}
