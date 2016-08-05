package cn.qing.soft.stepview;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.qing.soft.stepview.views.StepItemData;
import cn.qing.soft.stepview.views.StepViewLayout;

public class MainActivity extends AppCompatActivity {
    List<StepItemData> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < 30; i++) {
            StepItemData data = new StepItemData();
            if (i % 2 == 0) {
                data.setMsg("[北京市] 包裹已到达,北京市朝阳区 \n 联系电话:15912345678 ");
            } else {
                data.setMsg("[杭州市] 包裹已派发至转运中心,转运中心已发出。");
            }
            data.setDate("2016年08月03日");
            datas.add(data);
        }

        StepViewLayout stepView = (StepViewLayout) findViewById(R.id.stepView);

        /**
         * 使用该方法就是采用默认的右侧布局,一段文案,一个时间的方式来展现
         */
//        stepView.setData(datas);

        /**
         * 使用该方式可以传递一个自定义的右侧的布局,这样控制的权利就交给回调方法中自己来控制
         */
        stepView.setDataAndView(datas, R.layout.custom_step_item, new StepViewLayout.InflateContentListener() {
            @Override
            public void onContentInflate(int position, View contentView) {
                StepItemData data = datas.get(position);
                TextView title = (TextView) contentView.findViewById(R.id.itemMsg);
                TextView date = (TextView) contentView.findViewById(R.id.itemDate);
                title.setText(data.getMsg());
                date.setText(data.getDate());
                if (position == 0) {
                    title.setTextColor(Color.parseColor("#1c980f"));
                    date.setTextColor(Color.parseColor("#1c980f"));
                }
            }
        });
    }
}
