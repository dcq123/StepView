package com.github.qing.stepview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

import com.github.qing.stepviewlib.StepView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    List<StepItemData> datas = new ArrayList<>();
    StepView stepView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stepView = (StepView) findViewById(R.id.stepView);

        initData();
        stepView.setDatas(datas);
        stepView.setBindViewListener(new StepView.BindViewListener() {
            @Override
            public void onBindView(TextView itemMsg, TextView itemDate, Object data) {
                StepItemData sid = (StepItemData) data;
                itemMsg.setText(formatPhoneNumber(itemMsg, sid.getMsg()));
                itemDate.setText(sid.getDate());
            }
        });
    }

    private void initData() {
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
    }

    private static final String PATTERN_PHONE = "((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}";
    private static final String SCHEME_TEL = "tel:";

    /**
     * 格式化TextView的显示格式，识别手机号
     *
     * @param textView
     * @param source
     * @return
     */
    private SpannableStringBuilder formatPhoneNumber(TextView textView, String source) {
        // 若要部分 SpannableString 可点击，需要如下设置
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        // 将要格式化的 String 构建成一个 SpannableStringBuilder
        SpannableStringBuilder value = new SpannableStringBuilder(source);

        // 使用正则表达式匹配电话
        Linkify.addLinks(value, Pattern.compile(PATTERN_PHONE), SCHEME_TEL);

        // 获取上面到所有 addLinks 后的匹配部分(这里一个匹配项被封装成了一个 URLSpan 对象)
        URLSpan[] urlSpans = value.getSpans(0, value.length(), URLSpan.class);
        for (final URLSpan urlSpan : urlSpans) {
            if (urlSpan.getURL().startsWith(SCHEME_TEL)) {
                int start = value.getSpanStart(urlSpan);
                int end = value.getSpanEnd(urlSpan);
                value.removeSpan(urlSpan);
                value.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        String phone = urlSpan.getURL().replace(SCHEME_TEL, "");
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("是否拨打电话：" + phone);
                        builder.setNegativeButton("取消", null);
                        builder.setPositiveButton("确定", null);
                        builder.create().show();
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(Color.parseColor("#3f8de2"));
                        ds.setUnderlineText(true);
                    }
                }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return value;
    }
}
