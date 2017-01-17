package com.mzglinicki.ossomapp.dagger;

import com.mzglinicki.ossomapp.MyApplication;
import com.mzglinicki.ossomapp.database.DatabaseHelper;
import com.mzglinicki.ossomapp.webService.RequestHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Marcin on 15.01.2017.
 */
@Module
public class DaggerModule {

    private final MyApplication myApplication;

    public DaggerModule(final MyApplication myApplication) {
        this.myApplication = myApplication;
    }

    @Provides
    @Singleton
    RequestHelper getRequestHelper() {
        return new RequestHelper();
    }

    @Provides
    @Singleton
    DatabaseHelper getDatabaseHelper() {
        return new DatabaseHelper(myApplication);
    }
}