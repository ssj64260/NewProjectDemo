package com.android.newprojectdemo.widget;/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * limitations under the License.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.android.newprojectdemo.utils.DisplayUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.IntDef;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Grid 分割线
 */
public class DividerGridItemDecoration extends RecyclerView.ItemDecoration {

    public static final int ALL_DIVIDER = 0x01;//有外边框
    public static final int ONLY_INSIDE = 0x02;//无外边框

    public static final int VERTICAL = 0x03;
    public static final int HORIZONTAL = 0x04;

    @IntDef({ALL_DIVIDER, ONLY_INSIDE})
    @Retention(RetentionPolicy.SOURCE)
    @interface DrawMode {
    }

    @IntDef({VERTICAL, HORIZONTAL})
    @Retention(RetentionPolicy.SOURCE)
    @interface Orientation {
    }

    private Drawable mDivider;
    private int mVerticalDividerSize;
    private int mHorizontalDividerSize;
    private int mDrawMode;
    private int mOrientation;

    public DividerGridItemDecoration() {
        this.size(4, 4)
                .drawMode(ALL_DIVIDER)
                .orientation(VERTICAL);
    }

    public DividerGridItemDecoration color(@ColorInt int color) {
        mDivider = new ColorDrawable(color);
        return this;
    }

    public DividerGridItemDecoration color(Context context, @ColorRes int colorId) {
        mDivider = new ColorDrawable(ContextCompat.getColor(context, colorId));
        return this;
    }

    public DividerGridItemDecoration size(int verticalSizeDp, int horizontalSizeDp) {
        mVerticalDividerSize = DisplayUtils.dip2px(verticalSizeDp);
        mHorizontalDividerSize = DisplayUtils.dip2px(horizontalSizeDp);
        return this;
    }

    public DividerGridItemDecoration drawMode(@DrawMode int drawMode) {
        mDrawMode = drawMode;
        return this;
    }

    public DividerGridItemDecoration orientation(@Orientation int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
        return this;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mDivider == null) {
            return;
        }
        if (VERTICAL == mOrientation) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (!(layoutManager instanceof GridLayoutManager)) {
            return;
        }

        final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
        final int spanCount = gridLayoutManager.getSpanCount();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            if (mDrawMode == ALL_DIVIDER) {
                final int leftDividerSize = (spanCount - i % spanCount) * mVerticalDividerSize / spanCount;
                final int topDividerSize = i < spanCount ? mHorizontalDividerSize : 0;
                final int rightDividerSize = (i % spanCount + 1) * mVerticalDividerSize / spanCount;
                final int bottomDividerSize = mHorizontalDividerSize;
                drawLeft(c, child, leftDividerSize);
                drawTop(c, child, topDividerSize, leftDividerSize, rightDividerSize);
                drawRight(c, child, rightDividerSize);
                drawBottom(c, child, bottomDividerSize, leftDividerSize, rightDividerSize);
            } else {
                final int leftDividerSize = i % spanCount * mVerticalDividerSize / spanCount;
                final int topDividerSize = i >= spanCount ? mHorizontalDividerSize : 0;
                final int rightDividerSize = (spanCount - i % spanCount - 1) * mVerticalDividerSize / spanCount;
                final int bottomDividerSize = 0;
                drawLeft(c, child, leftDividerSize);
                drawTop(c, child, topDividerSize, leftDividerSize, rightDividerSize);
                drawRight(c, child, rightDividerSize);
                drawBottom(c, child, bottomDividerSize, leftDividerSize, rightDividerSize);
            }
        }
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (!(layoutManager instanceof GridLayoutManager)) {
            return;
        }

        final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
        final int spanCount = gridLayoutManager.getSpanCount();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            if (mDrawMode == ALL_DIVIDER) {
                final int leftDividerSize = i < spanCount ? mVerticalDividerSize : 0;
                final int topDividerSize = (spanCount - i % spanCount) * mHorizontalDividerSize / spanCount;
                final int rightDividerSize = mVerticalDividerSize;
                final int bottomDividerSize = (i % spanCount + 1) * mHorizontalDividerSize / spanCount;

                drawLeft(c, child, leftDividerSize);
                drawTop(c, child, topDividerSize, leftDividerSize, rightDividerSize);
                drawRight(c, child, rightDividerSize);
                drawBottom(c, child, bottomDividerSize, leftDividerSize, rightDividerSize);
            } else {
                final int leftDividerSize = i >= spanCount ? mVerticalDividerSize : 0;
                final int topDividerSize = i % spanCount * mHorizontalDividerSize / spanCount;
                final int rightDividerSize = 0;
                final int bottomDividerSize = (spanCount - i % spanCount - 1) * mHorizontalDividerSize / spanCount;

                drawLeft(c, child, leftDividerSize);
                drawTop(c, child, topDividerSize, leftDividerSize, rightDividerSize);
                drawRight(c, child, rightDividerSize);
                drawBottom(c, child, bottomDividerSize, leftDividerSize, rightDividerSize);
            }
        }
    }

    private void drawLeft(Canvas c, View child, int dividerSize) {
        if (dividerSize <= 0) {
            return;
        }

        int right = child.getLeft();
        int left = right - dividerSize;
        int top = child.getTop();
        int bottom = child.getBottom();
        mDivider.setBounds(left, top, right, bottom);
        mDivider.draw(c);
    }

    private void drawTop(Canvas c, View child, int dividerSize, int leftDividerSize, int rightDividerSize) {
        if (dividerSize <= 0) {
            return;
        }

        int bottom = child.getTop();
        int top = bottom - dividerSize;
        int left = child.getLeft() - leftDividerSize;
        int right = child.getRight() + rightDividerSize;
        mDivider.setBounds(left, top, right, bottom);
        mDivider.draw(c);
    }

    private void drawRight(Canvas c, View child, int dividerSize) {
        if (dividerSize <= 0) {
            return;
        }

        int left = child.getRight();
        int top = child.getTop();
        int right = left + dividerSize;
        int bottom = child.getBottom();
        mDivider.setBounds(left, top, right, bottom);
        mDivider.draw(c);
    }

    private void drawBottom(Canvas c, View child, int dividerSize, int leftDividerSize, int rightDividerSize) {
        if (dividerSize <= 0) {
            return;
        }

        int left = child.getLeft() - leftDividerSize;
        int top = child.getBottom();
        int right = child.getRight() + rightDividerSize;
        int bottom = top + dividerSize;
        mDivider.setBounds(left, top, right, bottom);
        mDivider.draw(c);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (!(layoutManager instanceof GridLayoutManager)) {
            return;
        }

        final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
        final int spanCount = gridLayoutManager.getSpanCount();
        final int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();

        if (VERTICAL == mOrientation) {
            if (mDrawMode == ALL_DIVIDER) {
                final int left = (spanCount - position % spanCount) * mVerticalDividerSize / spanCount;
                final int top = position < spanCount ? mHorizontalDividerSize : 0;
                final int right = (position % spanCount + 1) * mVerticalDividerSize / spanCount;
                outRect.set(left, top, right, mHorizontalDividerSize);
            } else {
                final int left = position % spanCount * mVerticalDividerSize / spanCount;
                final int top = position >= spanCount ? mHorizontalDividerSize : 0;
                final int right = (spanCount - position % spanCount - 1) * mVerticalDividerSize / spanCount;
                outRect.set(left, top, right, 0);
            }
        } else {
            if (mDrawMode == ALL_DIVIDER) {
                final int left = position < spanCount ? mVerticalDividerSize : 0;
                final int top = (spanCount - position % spanCount) * mHorizontalDividerSize / spanCount;
                final int bottom = (position % spanCount + 1) * mHorizontalDividerSize / spanCount;
                outRect.set(left, top, mVerticalDividerSize, bottom);
            } else {
                final int left = position >= spanCount ? mVerticalDividerSize : 0;
                final int top = position % spanCount * mHorizontalDividerSize / spanCount;
                final int bottom = (spanCount - position % spanCount - 1) * mHorizontalDividerSize / spanCount;
                outRect.set(left, top, 0, bottom);
            }
        }
    }
}