package com.minhtam.petsworld.Util.RecyclerViewUtil;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by st on 6/5/2017.
 */

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {
    private int totalItemCount;
    private int lastVisibleItem;
    private boolean isLoading = false;
    private int visibleThreshold = 10;

    public EndlessScrollListener() {
    }


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        totalItemCount = layoutManager.getItemCount();
        lastVisibleItem = layoutManager.findLastVisibleItemPosition();
        if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
            onLoadMore(totalItemCount);
            isLoading = true;
        }
    }

    // Defines the process for actually loading more data based on page
    // Returns true if more data is being loaded; returns false if there is no more data to load.
    public abstract boolean onLoadMore(int totalItemsCount);

    public void setLoaded() {
        isLoading = false;
    }
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }
}
