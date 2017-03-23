package com.github.qing.stepviewlib.utils;

import android.content.Context;

/**
 * Created by dingchangqing on 2017/3/21.
 * 单位转换工具
 */

public class ScreenUtils {


    public static int dp2px(Context context, int dpValue) {
        int density = (int) context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f);
    }

    public static int getColorWithAlpha(float alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }
}
