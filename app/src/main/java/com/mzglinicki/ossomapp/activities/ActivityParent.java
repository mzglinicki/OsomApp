package com.mzglinicki.ossomapp.activities;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mzglinicki.ossomapp.MyApplication;
import com.mzglinicki.ossomapp.R;
import com.mzglinicki.ossomapp.database.DatabaseHelper;
import com.mzglinicki.ossomapp.webService.RequestHelper;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by Marcin on 16.01.2017.
 */

public abstract class ActivityParent extends AppCompatActivity {

    @Inject
    protected RequestHelper requestHelper;
    @Inject
    protected DatabaseHelper databaseHelper;

    private final static int CLOSE_APP_ON_BACK = 2000;

    private boolean doubleBackToExitPressedOnce;
    protected int layoutResId;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutResId);

        ((MyApplication) getApplication()).getComponent().inject(this);
        ButterKnife.bind(this);

        onCreateActivity(savedInstanceState);
    }

    protected abstract void onCreateActivity(final Bundle savedInstanceState);

    protected boolean isNetworkAvailable() {
        final ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    protected void doubleClickToClose(final int toastMessageResId) {
        if (doubleBackToExitPressedOnce) {
            finish();
        } else {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, toastMessageResId, Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, CLOSE_APP_ON_BACK);
        }
    }

    protected void showErrorMessage(final String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void loadSquareThumb(final String imageUrl, final ImageView imageView) {

        Glide
                .with(this)
                .load(imageUrl)
                .asBitmap()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }
}