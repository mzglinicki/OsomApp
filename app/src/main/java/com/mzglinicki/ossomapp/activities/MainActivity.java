package com.mzglinicki.ossomapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;

import com.mzglinicki.ossomapp.ListItemDecorator;
import com.mzglinicki.ossomapp.R;
import com.mzglinicki.ossomapp.adapters.ListAdapter;
import com.mzglinicki.ossomapp.webService.ListItem;
import com.mzglinicki.ossomapp.webService.ServerData;
import com.mzglinicki.ossomapp.webService.ServerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.mzglinicki.ossomapp.Constants.ITEM_ID;
import static com.mzglinicki.ossomapp.Constants.RANDOM_NICE_IMAGE;

public class MainActivity extends ActivityParent implements ListAdapter.ListClickable {

    @Bind(R.id.recyclerView)
    protected RecyclerView recyclerView;
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.toolbarImage)
    protected ImageView collapsingToolbar;
    @Bind(R.id.swipeContainer)
    protected SwipeRefreshLayout swipeContainer;
    @Bind(R.id.scrollFAB)
    protected FloatingActionButton scrollFAB;

    private final static String ITEM_KEY = "itemKey";
    private final static int FAB_VISIBILITY_POINT = 10;
    private final static int LAST_PAGE_ID = 4;
    private final static int OVERLAP = 5;

    private ListAdapter adapter;
    private ArrayList<ListItemDecorator> listForAdapter;
    private LinearLayoutManager linearLayoutManager;
    private boolean loading;
    private int pastVisibleItems;
    private int visibleItemCount;
    private int totalItemCount;
    private int page;

    public MainActivity() {
        layoutResId = R.layout.activity_main;
    }

    @Override
    protected void onCreateActivity(final Bundle savedInstanceState) {
        initVariables();
        loadSquareThumb(RANDOM_NICE_IMAGE, collapsingToolbar);
        setupRecyclerView(recyclerView);
        setupToolbar(toolbar);
        setupScrollListener(recyclerView);
        setupRefreshListener(swipeContainer);

        if (savedInstanceState == null) {
            refreshList();
        } else {
            listForAdapter = savedInstanceState.getParcelableArrayList(ITEM_KEY);
            setupRecyclerView(recyclerView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        doubleClickToClose(R.string.clickToClose);
    }

    @Override
    protected void onResume() {
        setupScrollToBeginBtn(linearLayoutManager.findFirstVisibleItemPosition());
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        outState.putParcelableArrayList(ITEM_KEY, listForAdapter);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        setupScrollToBeginBtn(0);
    }

    @OnClick(R.id.scrollFAB)
    public void onScrollFABClick() {
        recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onItemClickListener(final ListItemDecorator model) {

        if (!isNetworkAvailable()) {
            showErrorMessage(getString(R.string.checkNetConnection));
            return;
        }

        startActivity(new Intent(this, DetailsActivity.class)
                .putExtra(ITEM_ID, model.getId()));
        overridePendingTransition(R.animator.trans_left_in, R.animator.trans_left_out);
    }

    private void initVariables() {
        listForAdapter = new ArrayList<>();
    }

    private void setupRecyclerView(final RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager = new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter = new ListAdapter(listForAdapter, this));
    }

    private void setupToolbar(final Toolbar toolbar) {
        toolbar.setTitle(R.string.toolbarTitle);
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);
    }

    private void getDataFromServer(final int page) {

        requestHelper.getAllData(page, new ServerListener<ServerData>() {
            @Override
            public void onSuccessfulResponse(final Response<ServerData> response) {
                loadItemsToList(response.body());
                onPostLoadedItem(null);
            }

            @Override
            public void onUnsuccessfulResponse(final Response<ServerData> response) {
                onPostLoadedItem(response.message());
            }

            @Override
            public void onFailureResponse() {
                onPostLoadedItem(getString(R.string.checkNetConnection));
            }
        });
    }

    private void loadItemsToList(final ServerData serverData) {

        for (final ListItem listItem : serverData.getData()) {
            listForAdapter.add(new ListItemDecorator(listItem));
        }
        adapter.notifyDataSetChanged();
    }

    private void refreshList() {
        if (!isNetworkAvailable()) {
            onNetworkUnavailable();
            return;
        }

        listForAdapter.clear();
        page = 1;
        getDataFromServer(page);

        if (swipeContainer.isRefreshing()) {
            return;
        }
        swipeContainer.setRefreshing(true);
    }

    private void onNetworkUnavailable() {
        showErrorMessage(getString(R.string.checkNetConnection));
        swipeContainer.setRefreshing(false);
    }

    private void onPostLoadedItem(@Nullable final String errorMessage) {
        if (swipeContainer.isRefreshing()) {
            swipeContainer.setRefreshing(false);
        }
        loading = true;

        if (errorMessage == null) {
            return;
        }
        showErrorMessage(errorMessage);
    }

    private void setupScrollListener(final RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                setupScrollToBeginBtn(pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition());

                if (dy <= 0 || page == LAST_PAGE_ID) {
                    return;
                }
                visibleItemCount = linearLayoutManager.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();

                if (!loading) {
                    return;
                }
                if ((visibleItemCount + pastVisibleItems) >= (totalItemCount - OVERLAP)) {
                    page++;
                    loading = false;
                    getDataFromServer(page);
                }
            }
        });
    }

    private void setupScrollToBeginBtn(final int pastVisibleItems) {
        scrollFAB.setVisibility(pastVisibleItems >= FAB_VISIBILITY_POINT ? VISIBLE : GONE);
    }

    private void setupRefreshListener(final SwipeRefreshLayout swipeContainer) {

        swipeContainer.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });
    }
}