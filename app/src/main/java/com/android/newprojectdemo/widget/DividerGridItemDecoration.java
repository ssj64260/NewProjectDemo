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

import androidx.annotation.ColorRes;
import androidx.annotation.IntDef;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Grid 分割线
 */
public class DividerGridItemDecoration extends RecyclerView.ItemDecoration {

    public static final int ALL_DIVIDER = 0x01;
    public static final int ONLY_INSIDE = 0x02;

    @IntDef({ALL_DIVIDER, ONLY_INSIDE})
    @Retention(RetentionPolicy.SOURCE)
    @interface DrawMode {
    }

    private Drawable mDivider;
    private int mDividerSize;
    private int mDrawMode;
    private int mOrientation;

    public DividerGridItemDecoration(Context context, @ColorRes int colorId) {
        mDivider = new ColorDrawable(ContextCompat.getColor(context, colorId));
        this.setDividerSize(10)
                .setDrawMode(ALL_DIVIDER)
                .setOrientation(RecyclerView.VERTICAL);
    }

    public DividerGridItemDecoration setDividerSize(int heightDP) {
        mDividerSize = DisplayUtils.dip2px(heightDP);
        return this;
    }

    public DividerGridItemDecoration setDrawMode(@DrawMode int drawMode) {
        mDrawMode = drawMode;
        return this;
    }

    public DividerGridItemDecoration setOrientation(@RecyclerView.Orientation int orientation) {
        if (orientation != RecyclerView.HORIZONTAL
                && orientation != RecyclerView.VERTICAL) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
        return this;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (RecyclerView.VERTICAL == mOrientation) {
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
                drawRight(c, child);
                drawBottom(c, child);

                if (i % spanCount == 0) {
                    drawLeft(c, child);
                }
                if (i < spanCount) {
                    drawTop(c, child);
                }
            } else {
                if (i % spanCount != 0) {
                    drawLeft(c, child);
                }
                if (i >= spanCount) {
                    drawTop(c, child);
                }
            }

            final Object object = child.getTag();
            if (object == null) {
                child.setTag(i);
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
                drawRight(c, child);
                drawBottom(c, child);

                if (i % spanCount == 0) {
                    drawTop(c, child);
                }
                if (i < spanCount) {
                    drawLeft(c, child);
                }
            } else {
                if (i % spanCount != 0) {
                    drawTop(c, child);
                }
                if (i >= spanCount) {
                    drawLeft(c, child);
                }
            }

            final Object object = child.getTag();
            if (object == null) {
                child.setTag(i);
            }
        }
    }

    private void drawLeft(Canvas c, View child) {
        int left = child.getLeft();
        int top = child.getTop();
        int right = left + mDividerSize;
        int bottom = child.getBottom();
        mDivider.setBounds(left, top, right, bottom);
        mDivider.draw(c);
    }

    private void drawTop(Canvas c, View child) {
        int left = child.getLeft();
        int top = child.getTop();
        int right = child.getRight();
        int bottom = top + mDividerSize;
        mDivider.setBounds(left, top, right, bottom);
        mDivider.draw(c);
    }

    private void drawRight(Canvas c, View child) {
        int left = child.getRight();
        int top = child.getTop();
        int right = left + mDividerSize;
        int bottom = child.getBottom();
        mDivider.setBounds(left, top, right, bottom);
        mDivider.draw(c);
    }

    private void drawBottom(Canvas c, View child) {
        int left = child.getLeft();
        int top = child.getBottom();
        int right = child.getRight();
        int bottom = top + mDividerSize;
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
        final Integer position = (Integer) view.getTag();

        if (RecyclerView.VERTICAL == mOrientation) {
            if (mDrawMode == ALL_DIVIDER) {
                final int left = position != null && position % spanCount == 0 ? mDividerSize : 0;
                final int top = position != null && position < spanCount ? mDividerSize : 0;
                outRect.set(left, top, mDividerSize, mDividerSize);
            } else {
                final int left = position != null && position % spanCount != 0 ? mDividerSize : 0;
                final int top = position != null && position >= spanCount ? mDividerSize : 0;
                outRect.set(left, top, 0, 0);
            }
        } else {
            if (mDrawMode == ALL_DIVIDER) {
                final int left = position != null && position < spanCount ? mDividerSize : 0;
                final int top = position != null && position % spanCount == 0 ? mDividerSize : 0;
                outRect.set(left, top, mDividerSize, mDividerSize);
            } else {
                final int left = position != null && position >= spanCount ? mDividerSize : 0;
                final int top = position != null && position % spanCount != 0 ? mDividerSize : 0;
                outRect.set(left, top, 0, 0);
            }
        }
    }
}