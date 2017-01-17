package com.mzglinicki.ossomapp.dagger;

import com.mzglinicki.ossomapp.activities.ActivityParent;
import com.mzglinicki.ossomapp.activities.DetailsActivity;
import com.mzglinicki.ossomapp.activities.MainActivity;
import com.mzglinicki.ossomapp.webService.RequestHelper;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Marcin on 16.01.2017.
 */

@Singleton
@Component(modules = DaggerModule.class)
public interface StorageComponent {

    void inject(ActivityParent activityParent);
}