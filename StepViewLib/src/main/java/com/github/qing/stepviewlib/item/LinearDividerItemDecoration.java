package com.github.qing.stepviewlib.item;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorInt;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.qing.stepviewlib.utils.ScreenUtils;

/**
 * Created by dcq on 2017/3/21.
 * 适合LinearLayoutManager的RecyclerView,支持竖直和水平两个方向
 */

public class LinearDividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int DEFAULT_HEIGHT = 1;
    private ColorDrawable mDivider;
    private Builder builder;

    private LinearDividerItemDecoration(Builder builder) {
        mDivider = new ColorDrawable(Color.GRAY);
        mDivider.setColor(builder.dividerColor);
        this.builder = builder;
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        c.save();
        int left, right, top, bottom;

        int count = parent.getChildCount();
        if (!builder.isShowLastDivider) {
            count -= 1;
        }

        for (int i = 0; i < count; i++) {
            final View child = parent.getChildAt(i);
            int transitionX = (int) ViewCompat.getTranslationX(child);
            int transitionY = (int) ViewCompat.getTranslationY(child);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            if (builder.orientation == LinearLayoutManager.VERTICAL) {
                left = child.getLeft() - params.leftMargin + transitionX + ScreenUtils.dp2px(parent.getContext(), builder.leftMargin);
                top = child.getBottom() + params.bottomMargin;
                right = child.getRight() + params.rightMargin - ScreenUtils.dp2px(parent.getContext(), builder.rightMargin);
                bottom = top + builder.dividerHeight + transitionY;
            } else {
                top = child.getTop() - params.topMargin + ScreenUtils.dp2px(parent.getContext(), builder.topMargin);
                bottom = child.getBottom() + params.bottomMargin + transitionY - ScreenUtils.dp2px(parent.getContext(), builder.bottomMargin);
                left = child.getRight() + params.rightMargin + transitionX;
                right = left + builder.dividerHeight;
            }

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
        c.restore();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        int itemCount = parent.getAdapter().getItemCount();
        if (!builder.isShowLastDivider && position == itemCount - 1) {
            return;
        }
        if (builder.orientation == LinearLayoutManager.VERTICAL) {
            outRect.set(0, 0, 0, builder.dividerHeight);
        } else {
            outRect.set(0, 0, builder.dividerHeight, 0);
        }
    }


    public static class Builder {
        private int dividerHeight = DEFAULT_HEIGHT;
        private int dividerColor = Color.GRAY;
        private int orientation = LinearLayoutManager.VERTICAL;
        private int leftMargin, rightMargin, topMargin, bottomMargin;
        private boolean isShowLastDivider = true;

        public Builder setDividerHeight(int dividerHeight) {
            this.dividerHeight = dividerHeight;
            return this;
        }

        public Builder setDividerColor(@ColorInt int color) {
            this.dividerColor = color;
            return this;
        }

        public Builder setOrientation(int orientation) {
            this.orientation = orientation;
            return this;
        }

        public Builder setLeftMargin(int leftMargin) {
            this.leftMargin = leftMargin;
            return this;
        }

        public Builder setRightMargin(int rightMargin) {
            this.rightMargin = rightMargin;
            return this;
        }

        public Builder setTopMargin(int topMargin) {
            this.topMargin = topMargin;
            return this;
        }

        public Builder setBottomMargin(int bottomMargin) {
            this.bottomMargin = bottomMargin;
            return this;
        }

        public Builder isShowLastDivider(boolean flag) {
            this.isShowLastDivider = flag;
            return this;
        }

        public LinearDividerItemDecoration build() {
            return new LinearDividerItemDecoration(this);
        }
    }

}
