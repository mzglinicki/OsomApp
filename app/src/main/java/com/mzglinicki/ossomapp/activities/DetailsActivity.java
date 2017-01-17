package com.mzglinicki.ossomapp.activities;

import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mzglinicki.ossomapp.R;
import com.mzglinicki.ossomapp.webService.ListItem;
import com.mzglinicki.ossomapp.webService.ServerListener;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.mzglinicki.ossomapp.Constants.ITEM_ID;

/**
 * Created by User on 2017-01-16.
 */

public class DetailsActivity extends ActivityParent {

    @Bind(R.id.speakerPhotoField)
    protected ImageView speakerPhotoField;
    @Bind(R.id.seekBar)
    protected SeekBar seekBar;
    @Bind(R.id.seekBarValue)
    protected TextView seekBarValue;
    @Bind(R.id.nameField)
    protected TextView nameField;
    @Bind(R.id.descriptionField)
    protected TextView descriptionField;
    @Bind(R.id.speakerNameField)
    protected TextView speakerNameField;
    @Bind(R.id.scoreField)
    protected TextView scoreField;
    @Bind(R.id.progressBar)
    protected ProgressBar progressBar;

    @Bind(R.id.sendNoteBtn)
    protected Button sendNoteBtn;

    private final static int ENLARGE_TEXT_SIZE = 20;
    private final static int NORMAL_TEXT_SIZE = 14;
    private final static int ENLARGE_TEXT_POST_DELAY = 250;

    public DetailsActivity() {
        layoutResId = R.layout.activity_details;
    }

    @Override
    protected void onCreate() {
        loadClickedItemData(getIntent().getExtras().getInt(ITEM_ID));
        setupSeekBarChangeListener(seekBar);
        seekBarValue.setText(String.valueOf(seekBar.getProgress()));
    }

    @Override
    public void onBackPressed() {
        if (!isNetworkAvailable()) {
            doubleClickToClose(R.string.onNetConnectionFailed);
            return;
        }

        finish();
        overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);
    }

    @OnClick(R.id.sendNoteBtn)
    public void onSendNoteBtnClick() {

        progressBar.setVisibility(VISIBLE);
        sendNoteBtn.setEnabled(false);

        final int newScore = Integer.parseInt(scoreField.getText().toString()) + seekBar.getProgress();
        requestHelper.updateItem(new ListItem(newScore), getIntent().getExtras().getInt(ITEM_ID), new ServerListener<Void>() {
            @Override
            public void onSuccessfulResponse(final Response<Void> response) {
                changeCurrentNote(newScore);
                progressBar.setVisibility(GONE);
            }

            @Override
            public void onUnsuccessfulResponse(final Response<Void> response) {
                showErrorMessage(response.message());
                progressBar.setVisibility(GONE);
            }

            @Override
            public void onFailureResponse() {
                showErrorMessage(getString(R.string.checkNetConnection));
                progressBar.setVisibility(GONE);
            }
        });
    }

    private void loadDataOnView(final ListItem loadedItem) {
        nameField.setText(loadedItem.getName());
        descriptionField.setText(loadedItem.getDescription());
        speakerNameField.setText(loadedItem.getSpeakerName());
        scoreField.setText(String.valueOf(loadedItem.getScore()));
        loadSquareThumb(loadedItem.getSpeakerPhoto(), speakerPhotoField);
    }

    private void setupSeekBarChangeListener(final SeekBar seekBar) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
                seekBarValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(final SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
            }
        });
    }

    private void loadClickedItemData(final int itemId) {

        requestHelper.getItem(itemId, new ServerListener<ListItem>() {
            @Override
            public void onSuccessfulResponse(final Response<ListItem> response) {
                loadDataOnView(response.body());
            }

            @Override
            public void onUnsuccessfulResponse(final Response<ListItem> response) {
                showErrorMessage(response.message());
            }

            @Override
            public void onFailureResponse() {
                showErrorMessage(getString(R.string.checkNetConnection));
            }
        });
    }

    private void changeCurrentNote(final int newScore) {
        scoreField.setTextSize(ENLARGE_TEXT_SIZE);
        scoreField.setTextColor(ContextCompat.getColor(DetailsActivity.this, R.color.colorAccent));
        scoreField.setText(String.valueOf(newScore));

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                scoreField.setTextSize(NORMAL_TEXT_SIZE);
                scoreField.setTextColor(ContextCompat.getColor(DetailsActivity.this, R.color.primaryText));
            }
        }, ENLARGE_TEXT_POST_DELAY);
    }
}