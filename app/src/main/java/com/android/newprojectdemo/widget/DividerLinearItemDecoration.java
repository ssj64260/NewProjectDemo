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

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Linear 分割线
 */
public class DividerLinearItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private int mDividerSize;

    private int mPaddingLeft;
    private int mPaddingRight;

    private int mOrientation;

    public DividerLinearItemDecoration(Context context, @ColorRes int colorId) {
        mDivider = new ColorDrawable(ContextCompat.getColor(context, colorId));
        this.setDividerSize(1)
                .setOrientation(RecyclerView.VERTICAL)
                .setPaddingLeft(0)
                .setPaddingRight(0);
    }

    public DividerLinearItemDecoration setDividerSize(int sizeDp) {
        mDividerSize = DisplayUtils.dip2px(sizeDp);
        return this;
    }

    public DividerLinearItemDecoration setOrientation(@RecyclerView.Orientation int orientation) {
        if (orientation != RecyclerView.HORIZONTAL
                && orientation != RecyclerView.VERTICAL) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
        return this;
    }

    public DividerLinearItemDecoration setPaddingLeft(int paddingLeftDP) {
        this.mPaddingLeft = DisplayUtils.dip2px(paddingLeftDP);
        return this;
    }

    public DividerLinearItemDecoration setPaddingRight(int paddingRightDP) {
        this.mPaddingRight = DisplayUtils.dip2px(paddingRightDP);
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
        final int left = parent.getPaddingLeft() + mPaddingLeft;
        final int right = parent.getWidth() - parent.getPaddingRight() - mPaddingRight;

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            //RecyclerView v = new RecyclerView(parent.getContext());
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDividerSize;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDividerSize;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (RecyclerView.VERTICAL == mOrientation) {
            outRect.set(0, 0, 0, mDividerSize);
        } else {
            outRect.set(0, 0, mDividerSize, 0);
        }
    }
}