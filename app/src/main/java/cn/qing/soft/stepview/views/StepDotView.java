package cn.qing.soft.stepview.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import cn.qing.soft.stepview.R;

/**
 * 左侧的【步骤点】对应的View,包含点和线。
 * 可以单独使用,放在每一个item的布局中;也可以使用进行简单封装的StepViewLayout
 */
public class StepDotView extends View {
    private static final int DEFAULT_WIDTH_SIZE = 50;
    private static final int DEFAULT_HEIGHT_SIZE = 100;

    private static final int DEFAULT_DOT_RADIUS = 5;
    private static final int DEFAULT_DOT_STROKE_WIDTH = 2;
    private static final int DEFAULT_SPACE = 4;

    public static final int POSITION_TOP = 0;
    public static final int POSITION_CENTER = 1;

    private Context mContext;

    private Paint mDotPaint;
    private Paint mDotHighPaint;
    private Paint mDotHighRingPaint;
    private Paint mLinePaint;

    private int highDotColor;
    private int defaultDotColor;
    private int radius, ringRadius;
    private boolean isHighDot;
    private boolean isFirst, isLast;
    private int dotPosition = POSITION_TOP;

    public StepDotView(Context context) {
        this(context, null);
    }

    public StepDotView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepDotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StepDotView, defStyleAttr, 0);
        highDotColor = a.getColor(R.styleable.StepDotView_highDotColor, Color.parseColor("#1c980f"));
        defaultDotColor = a.getColor(R.styleable.StepDotView_defaultDotColor, Color.parseColor("#d0d0d0"));
        radius = (int) a.getDimension(R.styleable.StepDotView_radius, DisplayUtils.dp2px(context, DEFAULT_DOT_RADIUS));
        dotPosition = a.getInteger(R.styleable.StepDotView_position, POSITION_TOP);
        a.recycle();

        mContext = context;
        init();
    }

    private void init() {
        ringRadius = radius + DisplayUtils.dp2px(mContext, DEFAULT_DOT_STROKE_WIDTH);

        mDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDotPaint.setStyle(Paint.Style.FILL);
        mDotPaint.setColor(defaultDotColor);

        mDotHighPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDotHighPaint.setStyle(Paint.Style.FILL);
        mDotHighPaint.setColor(highDotColor);

        mDotHighRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDotHighRingPaint.setStyle(Paint.Style.STROKE);
        mDotHighRingPaint.setStrokeWidth(5);
        mDotHighRingPaint.setColor(DisplayUtils.getColorWithAlpha(0.3f, highDotColor));


        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setColor(defaultDotColor);
        mLinePaint.setStrokeWidth(1);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = DisplayUtils.dp2px(mContext, DEFAULT_HEIGHT_SIZE);
        }
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = DisplayUtils.dp2px(mContext, DEFAULT_WIDTH_SIZE);
        }

        setMeasuredDimension(widthSize, heightSize);

        cx = widthSize / 2;
        cy = heightSize / 2;

        if (dotPosition == POSITION_TOP) {
            cy /= 2;
        }
    }

    int cx,cy;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int maxCy = radius;

        if (isHighDot) {
            canvas.drawCircle(cx, cy, radius, mDotHighPaint);
            canvas.drawCircle(cx, cy, ringRadius, mDotHighRingPaint);
            maxCy = ringRadius;
        } else {
            canvas.drawCircle(cx, cy, radius, mDotPaint);
        }

        if (!isFirst) {
            int stopY = cy - maxCy - DisplayUtils.dp2px(mContext, DEFAULT_SPACE);
            canvas.drawLine(cx, 0, cx, stopY, mLinePaint);
        }

        if (!isLast) {
            int bStartY = cy + maxCy + DisplayUtils.dp2px(mContext, DEFAULT_SPACE);
            int bStopY = getMeasuredHeight();
            canvas.drawLine(cx, bStartY, cx, bStopY, mLinePaint);
        }
    }

    public void setFirstDot() {
        isFirst = true;
        isHighDot = true;
        invalidate();
    }

    public void setLastDot() {
        isLast = true;
        invalidate();
    }

    public void setDotPosition(int dotPosition) {
        this.dotPosition = dotPosition;
    }

    public int getDefaultDotColor() {
        return defaultDotColor;
    }

    public void setDefaultDotColor(int defaultDotColor) {
        this.defaultDotColor = defaultDotColor;
    }

    public int getDotPosition() {
        return dotPosition;
    }

    public int getHighDotColor() {
        return highDotColor;
    }

    public void setHighDotColor(int highDotColor) {
        this.highDotColor = highDotColor;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
