package com.mzglinicki.ossomapp.activities;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mzglinicki.ossomapp.R;
import com.mzglinicki.ossomapp.webService.ListItem;
import com.mzglinicki.ossomapp.webService.ServerListener;

import java.util.List;

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
    @Bind(R.id.nameField)
    protected TextView nameField;
    @Bind(R.id.descriptionField)
    protected TextView descriptionField;
    @Bind(R.id.speakerNameField)
    protected TextView speakerNameField;
    @Bind(R.id.scoreField)
    protected TextView scoreField;
    @Bind(R.id.sendNoteValue)
    protected TextView sendNoteValue;
    @Bind(R.id.setNote)
    protected TextView setNote;
    @Bind(R.id.progressBar)
    protected ProgressBar progressBar;
    @Bind(R.id.sendNoteBtn)
    protected Button sendNoteBtn;

    @Bind({R.id.firstStar, R.id.secondStar, R.id.thirdStar, R.id.fourthStar, R.id.fifthStar})
    protected List<ImageButton> stars;
    @Bind({R.id.sendNote, R.id.sendNoteValue, R.id.megastars})
    protected List<TextView> noteViews;

    private final static int FIRST_STAR_VALUE = 1;
    private final static int SECOND_STAR_VALUE = 2;
    private final static int THIRD_STAR_VALUE = 3;
    private final static int FOURTH_STAR_VALUE = 4;
    private final static int FIFTH_STAR_VALUE = 5;
    private final static int EMPTY_NOTE_SIGN = -1;

    private int note;

    public DetailsActivity() {
        layoutResId = R.layout.activity_details;
    }

    @Override
    protected void onCreate() {
        final int itemId = getIntent().getExtras().getInt(ITEM_ID);
        loadClickedItemData(itemId);

        final boolean isItemInDb = databaseHelper.ifRecordExist(itemId);
        setupEvaluatedView(!isItemInDb, isItemInDb ? databaseHelper.getItemNote(itemId) : EMPTY_NOTE_SIGN);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);
    }

    @OnClick(R.id.sendNoteBtn)
    public void onSendNoteBtnClick() {
        if (!isNetworkAvailable()) {
            showErrorMessage(getString(R.string.checkNetConnection));
            return;
        }

        progressBar.setVisibility(VISIBLE);
        final int itemId = getIntent().getExtras().getInt(ITEM_ID);
        updateItemScore(itemId, Integer.parseInt(scoreField.getText().toString()) + note);
    }

    private void updateItemScore(final int itemId, final int newScore) {
        requestHelper.updateItem(new ListItem(newScore), itemId, new ServerListener<Void>() {
            @Override
            public void onSuccessfulResponse(final Response<Void> response) {
                scoreField.setText(String.valueOf(newScore));
                progressBar.setVisibility(GONE);
                setupEvaluatedView(false, note);
                databaseHelper.insertData(itemId, note);
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

    private void setupEvaluatedView(final boolean isStarsLayout, final int note) {
        sendNoteBtn.setVisibility(isStarsLayout ? VISIBLE : GONE);
        setNote.setVisibility(isStarsLayout ? VISIBLE : GONE);

        for (final ImageButton star : stars) {
            star.setVisibility(isStarsLayout ? VISIBLE : GONE);
        }

        for (final TextView noteView : noteViews) {
            noteView.setVisibility(isStarsLayout ? GONE : VISIBLE);
        }

        if (note == EMPTY_NOTE_SIGN) {
            return;
        }
        sendNoteValue.setText(String.valueOf(note));
    }

    @OnClick({R.id.firstStar, R.id.secondStar, R.id.thirdStar, R.id.fourthStar, R.id.fifthStar})
    public void onStarClick(final View view) {
        setupRate(view.getId());
    }

    private void setupRate(final int starResId) {

        switch (starResId) {
            case R.id.firstStar:
                note = FIRST_STAR_VALUE;
                break;
            case R.id.secondStar:
                note = SECOND_STAR_VALUE;
                break;
            case R.id.thirdStar:
                note = THIRD_STAR_VALUE;
                break;
            case R.id.fourthStar:
                note = FOURTH_STAR_VALUE;
                break;
            case R.id.fifthStar:
                note = FIFTH_STAR_VALUE;
                break;
            default:
                break;
        }

        toggleStars(starResId);
        sendNoteBtn.setVisibility(VISIBLE);
    }

    private void toggleStars(final int starId) {

        for (final ImageButton star : stars) {
            star.setImageResource(R.drawable.ic_star_border);
        }

        for (final ImageButton star : stars) {
            star.setImageResource(R.drawable.ic_star);

            if (star.getId() == starId) {
                return;
            }
        }
    }

    private void loadDataOnView(final ListItem loadedItem) {
        nameField.setText(loadedItem.getName());
        descriptionField.setText(loadedItem.getDescription());
        speakerNameField.setText(loadedItem.getSpeakerName());
        scoreField.setText(String.valueOf(loadedItem.getScore()));
        loadSquareThumb(loadedItem.getSpeakerPhoto(), speakerPhotoField);
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
}