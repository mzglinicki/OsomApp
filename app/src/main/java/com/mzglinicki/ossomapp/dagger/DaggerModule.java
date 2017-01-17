package com.mzglinicki.ossomapp.dagger;

import com.mzglinicki.ossomapp.webService.RequestHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Marcin on 15.01.2017.
 */
@Module
public class DaggerModule {

    @Provides
    @Singleton
    RequestHelper getRequestHelper() {
        return new RequestHelper();
    }
}