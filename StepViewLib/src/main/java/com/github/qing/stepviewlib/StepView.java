package com.github.qing.stepviewlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.qing.stepviewlib.item.LinearDividerItemDecoration;
import com.github.qing.stepviewlib.item.StepNodeItemDecoration;
import com.github.qing.stepviewlib.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dcq on 2017/3/22.
 * <p>
 * 使用RecyclerView加载并展示每个记录，使用ItemDecoration装饰item，显示节点
 */

public class StepView extends RecyclerView {

    public static final int DEFAULT_LEFT_MARGIN = 30;
    public static final int DEFAULT_RIGHT_MARGIN = 50;
    public static final int DEFAULT_DOT_RADIUS = 6;
    private static final int DEFAULT_LINE_WIDTH = 1;

    private int leftMargin, rightMargin;
    private int lineColor, lineWidth;
    private int defaultDotColor, highDotColor;
    private int dotPosition;
    private int radius;
    private Drawable defaultDotDrawable;
    private Drawable highDotDrawable;
    private int defaultColor = Color.parseColor("#eeeeee");

    private List itemDatas = new ArrayList<>();
    private StepAdapter mAdapter;
    private BindViewListener mListener;

    public StepView(Context context) {
        this(context, null);
    }

    public StepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        leftMargin = ScreenUtils.dp2px(context, DEFAULT_LEFT_MARGIN);
        rightMargin = ScreenUtils.dp2px(context, DEFAULT_RIGHT_MARGIN);
        lineWidth = ScreenUtils.dp2px(context, DEFAULT_LINE_WIDTH);
        radius = ScreenUtils.dp2px(context, DEFAULT_DOT_RADIUS);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StepView, defStyle, 0);

        leftMargin = (int) ta.getDimension(R.styleable.StepView_leftMargin, leftMargin);
        rightMargin = (int) ta.getDimension(R.styleable.StepView_rightMargin, rightMargin);
        lineWidth = (int) ta.getDimension(R.styleable.StepView_lineWidth, lineWidth);
        radius = (int) ta.getDimension(R.styleable.StepView_radius, radius);
        lineColor = ta.getColor(R.styleable.StepView_lineColor, defaultColor);
        defaultDotColor = ta.getColor(R.styleable.StepView_defaultDotColor, Color.parseColor("#d0d0d0"));
        highDotColor = ta.getColor(R.styleable.StepView_highDotColor, Color.parseColor("#1c980f"));
        dotPosition = ta.getInteger(R.styleable.StepView_dotPosition, StepNodeItemDecoration.POSITION_TOP);
        defaultDotDrawable = ta.getDrawable(R.styleable.StepView_defaultDotDrawable);
        highDotDrawable = ta.getDrawable(R.styleable.StepView_highDotDrawable);

        ta.recycle();

        init();
    }

    private void init() {
        mAdapter = new StepAdapter();
        setHasFixedSize(true);
        setLayoutManager(new LinearLayoutManager(getContext()));
        setAdapter(mAdapter);


        // 添加左侧节点item装饰
        addItemDecoration(
                new StepNodeItemDecoration.Builder(getContext())
                        .setLineColor(lineColor)
                        .setLeftMargin(leftMargin)
                        .setRightMargin(rightMargin)
                        .setDefaultDotColor(defaultDotColor)
                        .setHighDotColor(highDotColor)
                        .setLineWidth(lineWidth)
                        .setRadius(radius)
                        .setDefaultDotDrawable(defaultDotDrawable)
                        .setHighDotDrawable(highDotDrawable)
                        .setDotPosition(dotPosition)
                        .build()
        );
        // 添加底部分隔线
        addItemDecoration(
                new LinearDividerItemDecoration.Builder()
                        .setOrientation(LinearLayoutManager.VERTICAL)
                        .setDividerColor(defaultColor)
                        .setDividerHeight(2)
                        .isShowLastDivider(false)
                        .build()
        );
    }

    @SuppressWarnings("unchecked")
    public void setDatas(List datas) {
        itemDatas.clear();
        itemDatas.addAll(datas);
        mAdapter.notifyDataSetChanged();
    }

    class StepViewHolder extends ViewHolder {

        TextView itemMsg;
        TextView itemDate;

        StepViewHolder(View itemView) {
            super(itemView);
            itemMsg = (TextView) itemView.findViewById(R.id.itemMsg);
            itemDate = (TextView) itemView.findViewById(R.id.itemDate);
        }
    }

    private class StepAdapter extends Adapter<StepViewHolder> {

        @Override
        public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_view_item, parent, false);
            return new StepViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(StepViewHolder holder, int position) {
            Object data = itemDatas.get(position);
            if (mListener != null) {
                mListener.onBindView(holder.itemMsg, holder.itemDate, data);
            }
        }

        @Override
        public int getItemCount() {
            return itemDatas.size();
        }
    }

    public static interface BindViewListener {
        void onBindView(TextView itemMsg, TextView itemDate, Object data);
    }

    public void setBindViewListener(BindViewListener listener) {
        this.mListener = listener;
    }
}
