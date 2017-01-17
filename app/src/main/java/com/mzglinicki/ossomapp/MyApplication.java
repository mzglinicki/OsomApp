package com.mzglinicki.ossomapp;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.mzglinicki.ossomapp.dagger.DaggerModule;
import com.mzglinicki.ossomapp.dagger.DaggerStorageComponent;
import com.mzglinicki.ossomapp.dagger.StorageComponent;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Marcin on 16.01.2017.
 */

public class MyApplication extends Application {

    private StorageComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        this.component = DaggerStorageComponent
                .builder()
                .daggerModule(new DaggerModule())
                .build();
    }

    public StorageComponent getComponent() {
        return component;
    }
}