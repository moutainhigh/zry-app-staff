package com.zhongmei.yunfu.context.util;

import android.text.InputFilter.LengthFilter;
import android.text.Spanned;


public class CustomLengthFilter extends LengthFilter {
    private int max;    private onFullListener listener;


    public CustomLengthFilter(final int max) {
        super(max);
        this.max = max;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        int mlength = getLength(dest.subSequence(dstart, dend).toString());        int dlength = getLength(dest.toString());        int slength = getLength(source.subSequence(start, end).toString());        int keep = this.max - (dlength - mlength);        if (keep <= 0) {
                        if (null != listener) {
                listener.isFull();
            }
            return "";
        } else if (keep >= slength) {
                        return null;
        } else {
                        int tmp = 0;
            int index;
            for (index = start; index <= end; index++) {
                if (isFullwidthCharacter(source.charAt(index))) {
                    tmp += 2;
                } else {
                    tmp += 1;
                }
                if (tmp > keep) {
                    break;
                }
            }
            if (null != listener) {
                listener.isFull();
            }
            return source.subSequence(start, index);
        }
    }

    public void setOnFullListener(onFullListener listener) {
        this.listener = listener;
    }

    public interface onFullListener {


        void isFull();
    }


    public static boolean isNull(final String str) {
        if (null == str || "".equals(str)) {
            return true;
        } else {
            return false;
        }
    }


    public static int getLength(final String str) {
        if (isNull(str)) {
            return 0;
        }
        int len = str.length();
        for (int i = 0; i < str.length(); i++) {
            if (isFullwidthCharacter(str.charAt(i))) {
                len = len + 1;
            }
        }
        return len;
    }


    public static int getFwCharNum(final String str) {
        if (isNull(str)) {
            return 0;
        }
        int num = 0;
        for (int i = 0; i < str.length(); i++) {
            if (isFullwidthCharacter(str.charAt(i))) {
                num++;
            }
        }
        return num;
    }


    public static boolean isFullwidthCharacter(final char ch) {
        if (ch >= 32 && ch <= 127) {
                        return false;
        } else if (ch >= 65377 && ch <= 65439) {
                        return false;
        } else {
            return true;
        }
    }

}
