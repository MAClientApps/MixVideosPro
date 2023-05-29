package com.marwadisachin78.mixvideospro.utility;

import android.content.Context;
import android.graphics.PointF;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

public class ScrollZoomLayoutManager extends RecyclerView.LayoutManager {

    private static final float SCALE_RATE = 1.2f;

    Context context;

    private int mDecoratedChildWidth;
    private int mDecoratedChildHeight;

    private int startLeft;
    private int startTop;
    private int offsetScroll;
    private int contentOffsetY = -1;

    private int itemSpace = 0;
    private int offsetDistance;

    private float maxScale;

    SparseBooleanArray itemAttached = new SparseBooleanArray();
    SparseArray<Integer> itemsX = new SparseArray<>();


    public ScrollZoomLayoutManager(Context context, int itemSpace) {
        this.context = context;
        offsetScroll = 0;
        this.itemSpace = itemSpace;
        maxScale = SCALE_RATE;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0) {
            detachAndScrapAttachedViews(recycler);
            offsetScroll = 0;
            return;
        }

        if (getChildCount() == 0) {
            View scrap = recycler.getViewForPosition(0);
            addView(scrap);
            measureChildWithMargins(scrap, 0, 0);
            mDecoratedChildWidth = getDecoratedMeasuredWidth(scrap);
            mDecoratedChildHeight = getDecoratedMeasuredHeight(scrap);
            offsetDistance = (int) (mDecoratedChildWidth * ((maxScale - 1f) / 2f + 1) + itemSpace);
            startLeft = (getHorizontalSpace() - mDecoratedChildWidth) / 2;
            startTop = contentOffsetY == -1 ? (getVerticalSpace() - mDecoratedChildHeight) / 2 : contentOffsetY;
            detachAndScrapView(scrap, recycler);
        }

        int x = 0;
        for (int i = 0; i < getItemCount(); i++) {
            itemsX.put(i, x);
            itemAttached.put(i, false);
            x += offsetDistance;
        }

        detachAndScrapAttachedViews(recycler);
        fixScrollOffset();
        layoutItems(recycler, state);
    }

    private void layoutItems(RecyclerView.Recycler recycler,
                             RecyclerView.State state) {
        if (state.isPreLayout()) return;

        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            int position = getPosition(view);
            if (itemsX.get(position) - offsetScroll + startLeft > getHorizontalSpace()
                    || itemsX.get(position) - offsetScroll + startLeft < -mDecoratedChildWidth - getPaddingLeft()) {
                itemAttached.put(position, false);
                removeAndRecycleView(view, recycler);
            }
        }

        for (int i = 0; i < getItemCount(); i++) {
            if (itemsX.get(i) - offsetScroll + startLeft <= getHorizontalSpace()
                    && itemsX.get(i) - offsetScroll + startLeft >= -mDecoratedChildWidth - getPaddingLeft()) {
                if (!itemAttached.get(i)) {
                    View scrap = recycler.getViewForPosition(i);
                    measureChildWithMargins(scrap, 0, 0);
                    addView(scrap);
                    int x = itemsX.get(i) - offsetScroll;

                    float scale = calculateScale(startLeft + x);
                    scrap.setRotation(0);
                    layoutDecorated(scrap, startLeft + x, startTop,
                            startLeft + x + mDecoratedChildWidth, startTop + mDecoratedChildHeight);
                    itemAttached.put(i, true);
                    scrap.setScaleX(scale);
                    scrap.setScaleY(scale);
                }
            }
        }
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int willScroll = dx;

        int targetX = offsetScroll + dx;

        if (targetX < 0) {
            willScroll = -offsetScroll;
        } else if (targetX > getMaxOffsetX()) {
            willScroll = getMaxOffsetX() - offsetScroll;
        }

        offsetScroll += willScroll;

        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            float scale = calculateScale(v.getLeft());
            layoutDecorated(v, v.getLeft() - willScroll, v.getTop(), v.getRight() - willScroll, v.getBottom());
            v.setScaleX(scale);
            v.setScaleY(scale);
        }

        layoutItems(recycler, state);
        return willScroll;
    }

    private float calculateScale(int x) {
        int deltaX = Math.abs(x - (getHorizontalSpace() - mDecoratedChildWidth) / 2);
        float diff = 0f;
        if ((mDecoratedChildWidth - deltaX) > 0) diff = mDecoratedChildWidth - deltaX;
        return (maxScale - 1f) / mDecoratedChildWidth * diff + 1;
    }

    private int getHorizontalSpace() {
        return getWidth() - getPaddingRight() - getPaddingLeft();
    }

    private int getVerticalSpace() {
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }

    private void fixScrollOffset() {
        if (offsetScroll < 0) {
            offsetScroll = 0;
        }
        if (offsetScroll > getMaxOffsetX()) {
            offsetScroll = getMaxOffsetX();
        }
    }

    private int getMaxOffsetX() {
        return (getItemCount() - 1) * offsetDistance;
    }

    private PointF computeScrollVectorForPosition(int targetPosition) {
        if (getChildCount() == 0) {
            return null;
        }
        final int firstChildPos = getPosition(getChildAt(0));
        final int direction = targetPosition < firstChildPos ? -1 : 1;
        return new PointF(direction, 0);
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public void scrollToPosition(int position) {
        if (position < 0 || position > getItemCount() - 1) return;
        int target = position * offsetDistance;
        if (target == offsetScroll) return;
        offsetScroll = target;
        fixScrollOffset();
        requestLayout();
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        LinearSmoothScroller smoothScroller = new LinearSmoothScroller(context) {
            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return ScrollZoomLayoutManager.this.computeScrollVectorForPosition(targetPosition);
            }
        };
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }

    @Override
    public void onAdapterChanged(RecyclerView.Adapter oldAdapter, RecyclerView.Adapter newAdapter) {
        removeAllViews();
        offsetScroll = 0;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public int getCurrentPosition() {
        return Math.round(offsetScroll / (float) offsetDistance);
    }

    public int getOffsetCenterView() {
        return getCurrentPosition() * offsetDistance - offsetScroll;
    }

    public int getContentOffsetY() {
        return contentOffsetY;
    }

    public void setContentOffsetY(int contentOffsetY) {
        this.contentOffsetY = contentOffsetY;
    }

    public float getMaxScale() {
        return maxScale;
    }

    public void setMaxScale(float maxScale) {
        this.maxScale = maxScale;
    }
}
