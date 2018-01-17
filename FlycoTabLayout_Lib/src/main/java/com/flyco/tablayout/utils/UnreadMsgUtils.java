package com.flyco.tablayout.utils;


import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 未读消息提示View,显示小红点或者带有数字的红点:
 * 数字一位,圆
 * 数字两位,圆角矩形,圆角是高度的一半
 * 数字超过两位,显示99+
 */
public class UnreadMsgUtils {
    public static void show(TextView msgView, int num) {
        if (msgView == null) {
            return;
        }

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) msgView.getLayoutParams();
        DisplayMetrics dm = msgView.getResources().getDisplayMetrics();

        msgView.setVisibility(View.VISIBLE);
        if (num <= 0) {
            msgView.setText("");
            lp.width = (int) (10 * dm.density);
            lp.height = (int) (10 * dm.density);
            lp.leftMargin = (int) (-5 * dm.density);
        } else {
            lp.height = (int) (18 * dm.density);
            if (num > 0 && num < 100) {
                lp.width = (int) (18 * dm.density);
                lp.leftMargin = (int) (-9 * dm.density);
                msgView.setText(num + "");
            } else {
                lp.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                lp.leftMargin = (int) (-12 * dm.density);
                msgView.setPadding((int) (4 * dm.density), 0, (int) (4 * dm.density), 0);
                msgView.setText("99+");
            }
        }
        msgView.setLayoutParams(lp);
    }

    public static void setSize(TextView rtv, int size) {
        if (rtv == null) {
            return;
        }
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) rtv.getLayoutParams();
        lp.width = size;
        lp.height = size;
        rtv.setLayoutParams(lp);
    }
}
