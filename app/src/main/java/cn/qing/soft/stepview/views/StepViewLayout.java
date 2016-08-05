package cn.qing.soft.stepview.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.qing.soft.stepview.R;

public class StepViewLayout extends LinearLayout {

    private int highDotColor;
    private int defaultDotColor;
    private int radius;
    private int dotPosition;

    private Context mContext;
    private LayoutInflater layoutInflater;
    private List<StepItemData> datas;

    public StepViewLayout(Context context) {
        this(context, null);
    }

    public StepViewLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StepViewLayout, defStyleAttr, 0);
        highDotColor = a.getColor(R.styleable.StepViewLayout_dotHighColor, Color.parseColor("#1c980f"));
        defaultDotColor = a.getColor(R.styleable.StepViewLayout_dotDefaultDotColor, Color.parseColor("#d0d0d0"));
        radius = (int) a.getDimension(R.styleable.StepViewLayout_dotRadius, DisplayUtils.dp2px(context, -1));
        dotPosition = a.getInteger(R.styleable.StepViewLayout_dotPosition, StepDotView.POSITION_CENTER);
        a.recycle();

        mContext = context;
        init();
    }

    private void init() {
        layoutInflater = LayoutInflater.from(mContext);
        setOrientation(VERTICAL);
        datas = new ArrayList<>();
    }

    /**
     * 设置数据,使用默认的布局样式
     * @param data
     */
    public void setData(List<StepItemData> data) {
        this.datas = data;
        inflateContent();
    }

    private void setStepDotViewStyle(StepDotView dotView,int position){
        dotView.setDotPosition(dotPosition);
        dotView.setHighDotColor(highDotColor);
        dotView.setDefaultDotColor(defaultDotColor);
        dotView.setRadius(radius);
        if (position == 0) {
            dotView.setFirstDot();
        } else if (position == datas.size() - 1) {
            dotView.setLastDot();
        }
    }

    private void inflateContent() {
        for (int i = 0; i < datas.size(); i++) {
            View contentView = layoutInflater.inflate(R.layout.step_item, null);
            StepDotView dotView = (StepDotView) contentView.findViewById(R.id.stepDotView);
            setStepDotViewStyle(dotView,i);

            TextView msg = (TextView) contentView.findViewById(R.id.itemMsg);
            TextView date = (TextView) contentView.findViewById(R.id.itemDate);
            StepItemData data = datas.get(i);
            if (i == 0) {
                msg.setTextColor(highDotColor);
                date.setTextColor(highDotColor);
            }
            msg.setText(data.getMsg());
            date.setText(data.getDate());

            addView(contentView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }


    /**
     * 设置数据,并使用自定义的布局,该布局的填充需要自己在回调方法中设置样式
     * @param data
     * @param layoutId
     * @param inflateContentListener
     */
    public void setDataAndView(List data, int layoutId, InflateContentListener inflateContentListener) {
        this.datas = data;
        for (int i = 0; i < datas.size(); i++) {

            LinearLayout stepItem = new LinearLayout(mContext);
            stepItem.setOrientation(LinearLayout.HORIZONTAL);

            StepDotView stepDotView = new StepDotView(mContext);
            setStepDotViewStyle(stepDotView, i);
            LinearLayout.LayoutParams dotParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dotParam.rightMargin = DisplayUtils.dp2px(mContext, 10);
            stepItem.addView(stepDotView, dotParam);

            View contentView = layoutInflater.inflate(layoutId, null);
            LinearLayout.LayoutParams contentParam = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            contentParam.weight = 1;
            stepItem.addView(contentView, contentParam);

            addView(stepItem, new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            if (inflateContentListener != null) {
                inflateContentListener.onContentInflate(i, contentView);
            }
        }
    }

    public interface InflateContentListener {
        void onContentInflate(int position, View contentView);
    }


}
