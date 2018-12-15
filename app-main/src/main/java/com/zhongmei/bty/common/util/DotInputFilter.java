package com.zhongmei.bty.common.util;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * 小数点位数过滤器
 * Created by demo on 2018/12/15
 */

public class DotInputFilter implements InputFilter {
    //小数点位数
    private int DECIMAL_DIGITS = 2;

    public void setDigits(int digits) {
        DECIMAL_DIGITS = digits;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        // 删除等特殊字符，直接返回
        if ("".equals(source.toString())) {
            return null;
        }
        String dValue = dest.toString();
        String[] splitArray = dValue.split("//.");
        if (splitArray.length > 1) {
            String dotValue = splitArray[1];
            int diff = dotValue.length() + 1 - DECIMAL_DIGITS;
            if (diff > 0) {
                return source.subSequence(start, end - diff);
            }
        }
        return null;
    }
}
