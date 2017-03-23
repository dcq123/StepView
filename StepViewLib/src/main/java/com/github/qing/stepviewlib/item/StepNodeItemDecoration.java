package com.github.qing.stepviewlib.item;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.qing.stepviewlib.utils.ScreenUtils;

/**
 * Created by dcq on 2017/3/23.
 * <p>
 * StepView每个Item左侧的步骤节点绘制
 */

public class StepNodeItemDecoration extends RecyclerView.ItemDecoration {
    private static final int DEFAULT_DOT_STROKE_WIDTH = 2;
    private static final int DEFAULT_SPACE = 4;

    public static final int POSITION_TOP = 0;
    public static final int POSITION_CENTER = 1;

    private Builder mBuilder;
    private Paint mLinePaint;
    private Paint mDotPaint;
    private Paint mDotHighPaint;
    private Paint mDotHighRingPaint;
    private int leftPadding;
    private int ringRadius, radius;
    private int space;

    private StepNodeItemDecoration(Builder builder) {
        mBuilder = builder;
        initPaint();
    }

    private void initPaint() {
        leftPadding = mBuilder.leftMargin + mBuilder.rightMargin;

        radius = mBuilder.radius;
        ringRadius = radius + ScreenUtils.dp2px(mBuilder.context, DEFAULT_DOT_STROKE_WIDTH);
        space = ScreenUtils.dp2px(mBuilder.context, DEFAULT_SPACE);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(mBuilder.lineColor);
        mLinePaint.setStrokeWidth(mBuilder.lineWidth);

        mDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDotPaint.setStyle(Paint.Style.FILL);
        mDotPaint.setColor(mBuilder.defaultDotColor);

        mDotHighPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDotHighPaint.setStyle(Paint.Style.FILL);
        mDotHighPaint.setColor(mBuilder.highDotColor);

        mDotHighRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDotHighRingPaint.setStyle(Paint.Style.STROKE);
        mDotHighRingPaint.setStrokeWidth(5);
        mDotHighRingPaint.setColor(ScreenUtils.getColorWithAlpha(0.3f, mBuilder.highDotColor));

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        c.save();
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            // 起始x位置
            int sx = child.getLeft() - params.leftMargin - mBuilder.rightMargin;
            // 起始y位置
            int sy = child.getTop() - space;
            int dy = child.getBottom() - params.bottomMargin;


            int cy = dy - child.getHeight() / 2;
            int maxCy = radius;
            if (mBuilder.dotPosition == POSITION_TOP) {
                cy = dy - child.getHeight() / 3 * 2;
            }

            // 获取当前child在Adapter中的position
            int adapterPosition = parent.getChildAdapterPosition(child);
            // 绘制节点圆圈
            if (adapterPosition == 0) {
                // 使用设置的drawable资源作为圆点
                if (mBuilder.highDotDrawable != null) {
                    int left = mBuilder.leftMargin - mBuilder.highDotDrawable.getIntrinsicWidth() / 2;
                    int top = cy - mBuilder.highDotDrawable.getIntrinsicHeight() / 2;
                    int right = left + mBuilder.highDotDrawable.getIntrinsicWidth();
                    int bottom = top + mBuilder.highDotDrawable.getIntrinsicHeight();
                    mBuilder.highDotDrawable.setBounds(left, top, right, bottom);
                    mBuilder.highDotDrawable.draw(c);
                    maxCy = mBuilder.highDotDrawable.getIntrinsicHeight() / 2;
                } else {
                    c.drawCircle(sx, cy, radius, mDotHighPaint);
                    c.drawCircle(sx, cy, ringRadius, mDotHighRingPaint);
                    maxCy = ringRadius;
                }
            } else {
                if (mBuilder.defaultDotDrawable != null) {
                    maxCy = mBuilder.defaultDotDrawable.getIntrinsicHeight() / 2;
                    int left = mBuilder.leftMargin - mBuilder.defaultDotDrawable.getIntrinsicWidth() / 2;
                    int top = cy - mBuilder.defaultDotDrawable.getIntrinsicHeight() / 2;
                    int right = left + mBuilder.defaultDotDrawable.getIntrinsicWidth();
                    int bottom = top + mBuilder.defaultDotDrawable.getIntrinsicHeight();
                    mBuilder.defaultDotDrawable.setBounds(left, top, right, bottom);
                    mBuilder.defaultDotDrawable.draw(c);
                } else {
                    c.drawCircle(sx, cy, radius, mDotPaint);
                    maxCy = radius;
                }
            }

            // 绘制上竖线
            if (adapterPosition > 0) {
                c.drawLine(sx, sy, sx, cy - maxCy - space, mLinePaint);
            }

            // 绘制下竖线
            if (adapterPosition < parent.getAdapter().getItemCount() - 1) {
                c.drawLine(sx, cy + maxCy + space, sx, dy, mLinePaint);
            }
        }
        c.restore();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(leftPadding, 0, 0, 0);
    }

    public static class Builder {

        private int leftMargin, rightMargin;
        private int lineColor;
        private int lineWidth;
        private int defaultDotColor;
        private int highDotColor;
        private int dotPosition;
        private Drawable defaultDotDrawable;
        private Drawable highDotDrawable;
        private int radius;
        private Context context;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setRadius(int radius) {
            this.radius = radius;
            return this;
        }

        public Builder setDefaultDotDrawable(Drawable defaultDotDrawable) {
            this.defaultDotDrawable = defaultDotDrawable;
            return this;
        }

        public Builder setHighDotDrawable(Drawable highDotDrawable) {
            this.highDotDrawable = highDotDrawable;
            return this;
        }

        public Builder setDotPosition(int dotPosition) {
            this.dotPosition = dotPosition;
            return this;
        }

        public Builder setDefaultDotColor(int defaultDotColor) {
            this.defaultDotColor = defaultDotColor;
            return this;
        }

        public Builder setHighDotColor(int highDotColor) {
            this.highDotColor = highDotColor;
            return this;
        }

        public Builder setLineWidth(int lineWidth) {
            this.lineWidth = lineWidth;
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

        public Builder setLineColor(int lineColor) {
            this.lineColor = lineColor;
            return this;
        }

        public StepNodeItemDecoration build() {
            return new StepNodeItemDecoration(this);
        }
    }
}
