package com.zhongmei.yunfu.context.util;

import android.text.TextUtils;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

import com.zhongmei.yunfu.context.data.ShopInfoCfg;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    public static final char PAUSE = ',';

    public static final char WAIT = ';';

    public static final char WILD = 'N';


    public final static boolean isNonSeparator(char c) {
        return (c >= '0' && c <= '9') || c == '*' || c == '#' || c == '+' || c == WILD || c == WAIT || c == PAUSE;
    }

    public static boolean isUserPasswordValid(String userPwd) {
        char[] cs = userPwd.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            if (cs[i] >= '0' && cs[i] <= '9') {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }


    public static String formatPhoneNumber(String phoneNumber) {
        char[] cs = phoneNumber.toCharArray();
        char[] res = new char[cs.length];
        for (int i = cs.length - 1, j = 0; i >= 0; i--, j++) {
            if (j >= 4 && j <= 7) {
                res[i] = '*';
            } else {
                res[i] = cs[i];
            }
        }
        return new String(res);
    }


    public static boolean isMobile(String mobile) {
                                                        String pattern = "^((\\+86)|(86))?0*(1)\\d{10}$";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(mobile);
        boolean flag = m.matches();
        return flag;
    }


    public static boolean isPhoneNumberValid(String number) {
        if (!isMobile(number)) {

            return isTelephoneValid(number);
        }
        return true;
    }


    public static String getStrValue(String value) {
        if (TextUtils.isEmpty(value)) return "";
        else return value;

    }


    public static String getCustomerPhone(String number) {
        if (number != null && number.length() > 7) {
            return "(" + number.substring(0, number.length() - (number.substring(3)).length()) + "****" + number.substring(7) + ")";
        }
        return "";
    }


    public static boolean isUserNameValid(String name) {
        String pattern = "^[a-zA-Z]{1}[a-zA-Z0-9_]{0,15}$";

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(name);

        boolean flag = m.matches();
        return flag;
    }


    public static boolean isTelephoneValid(String telephone) {
        String pattern = "0\\d{2,3}\\d{7,8}||\\d{7,8}";

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(telephone);

        boolean flag = m.matches();
        return flag;
    }


    public static boolean isCustomerNameValid(String name) {
        String pattern = "^([a-zA-Z]{1}[a-zA-Z0-9_]{0,15})|([\u0391-\uFFE5]{0,8})$";

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(name);

        boolean flag = m.matches();
        return flag;
    }

    public static String stripSeparators(String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        int len = phoneNumber.length();
        StringBuilder ret = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            char c = phoneNumber.charAt(i);
            if (isNonSeparator(c)) {
                ret.append(c);
            }
        }

        return ret.toString();
    }

    public static String quoteString(String s) {
        if (s == null) {
            return null;
        }
        if (!s.matches("^\".*\"$")) {
            return "\"" + s + "\"";
        } else {
            return s;
        }
    }

    public static String genUUID() {
        return UUID.randomUUID().toString();
    }


    @Deprecated
    public static String transfer(String param) {
        double tmp = Math.floor(Double.parseDouble(param));
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(tmp);
    }


    public static boolean isNum(String str) {
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }


    public static String transferDot2(String param) {
        if (TextUtils.isEmpty(param))
            param = "0.00";
        double tmp = Double.parseDouble(param);
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(tmp);
    }


    public static String transferDot2(Float param) {
        if (param == null) {
            param = 0F;
        }
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(param);
    }


    public static String transferDot2(BigDecimal param) {
        String tempStr;
        if (param == null) tempStr = "0.00";
        else tempStr = param.toString();
        return transferDot2(tempStr);
    }


    public static int getCharNum(String str) {
        int[] count = new int[2];
                char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if ((chars[i] >= 65 && chars[i] <= 90) || (chars[i] >= 97 && chars[i] <= 122)) {
                                count[0]++;
            } else if (chars[i] >= 48 && chars[i] <= 57) {
                                count[0]++;
            } else if (chars[i] == '!' || chars[i] == '[' || chars[i] == ']' || chars[i] == '-' || chars[i] == '|'
                    || chars[i] == '[') {
                count[0]++;
            } else {
                count[1]++;
            }
        }
        return count[0];
    }


    public static String float2String(float f) {
        DecimalFormat format = new DecimalFormat("##0.0");
        return format.format(f);
    }


    public static boolean isToday(long time) {
        if (time > getActiveDate()[0] && time < getActiveDate()[1]) {
            return true;
        }
        return false;
    }


    public static Long[] getActiveDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = sdf.getCalendar();
        try {
            cal.setTime(sdf.parse(sdf.format(System.currentTimeMillis())));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        Long[] res = new Long[2];
        res[0] = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        res[1] = cal.getTimeInMillis();
        return res;
    }


    public static String getNumberTowDecimal(String price) {
        if ("0.0".equals(price)) {
            return "0";
        }
        return new DecimalFormat("#.0").format(Double.parseDouble(price));
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static String trim(String valueStr) {
        return trim(valueStr, "");
    }

    public static String trim(String valueStr, String def) {
        if (TextUtils.isEmpty(valueStr)) {
            return def;
        }
        return valueStr.trim();
    }

    public static boolean isEqualsZero(BigDecimal bigDecimal) {
        if (bigDecimal == null) return true;
        return bigDecimal.compareTo(BigDecimal.ZERO) == 0;
    }


    public static BigDecimal emptyReturnToZero(BigDecimal bigDecimal) {
        if (bigDecimal == null) return BigDecimal.ZERO;
        return bigDecimal;
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }


    public static <T> List<T> asList(T t) {
        List<T> list = new ArrayList<>();
        list.add(t);

        return list;
    }


    public static String formatPrice(double value) {
        try {
            DecimalFormat df = new DecimalFormat("0.00");
            if (value >= 0) {
                return ShopInfoCfg.getInstance().getCurrencySymbol() + df.format(value);
            } else {
                return "-" + ShopInfoCfg.getInstance().getCurrencySymbol() + df.format(Math.abs(value));
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return value + "";
        }
    }

    public static int size(Collection<?> collection) {
        if (collection == null) return 0;
        return collection.size();
    }

    public static String formatPrice(BigDecimal value) {
        return value == null ? " " : formatPrice(value.doubleValue());
    }

    public static String formatPrice(String value) {
        if (value == null)
            return " ";
        try {
            DecimalFormat df = new DecimalFormat("0.00");
            double val = Double.parseDouble(value);
            if (val >= 0) {
                return ShopInfoCfg.getInstance().getCurrencySymbol() + df.format(val);
            } else {
                return "-" + ShopInfoCfg.getInstance().getCurrencySymbol() + df.format(Math.abs(val));
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return ShopInfoCfg.getInstance().getCurrencySymbol() + value;
        }
    }

    public static Animation shakeAnimation(int counts) {

        Animation translateAnimation = new TranslateAnimation(0, 15, 0, 0);


        translateAnimation.setInterpolator(new CycleInterpolator(counts));

        translateAnimation.setDuration(500);

        return translateAnimation;
    }


    public static boolean isEquals(BigDecimal value1, BigDecimal value2) {
        if (value1 == null && value2 == null) {
            return true;
        }
        if (value1 == null || value2 == null || value1.compareTo(value2) != 0) {
            return false;
        }
        return true;
    }


    public static boolean isNotEquals(BigDecimal value1, BigDecimal value2) {
        return !isEquals(value1, value2);
    }


    public static Boolean checkPointOneIndex(String value) {
        int pointIndex = value.indexOf('.');
        if (pointIndex >= 0 && (value.length() - 1) - pointIndex > 1) {
            return false;
        }
        return true;
    }


    public static Boolean checkPointTwoIndex(String value) {
        int pointIndex = value.indexOf('.');
        if (pointIndex >= 0 && (value.length() - 1) - pointIndex > 2) {
            return false;
        }
        return true;
    }

    private static long lastClickTime;

    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }


    public static <T> List<T> entity2List(T t) {
        List<T> list = new ArrayList<T>();
        list.add(t);

        return list;
    }


    public static boolean equals(Number n1, Number n2) {
        if (n1 == null || n2 == null) {
            return false;
        }

        return n1.equals(n2);
    }

    public static int add(int a, int b) {
        return a + b;
    }


    public static String useStarReplace(String account, int start, int offset) {
        StringBuilder builder = new StringBuilder();

        int end = start + offset;
        if (account.length() > start && account.length() < end) {
            builder.append(account.substring(0, start));
            for (int i = 0; i <= account.length() - offset; i++) {
                builder.append("*");
            }
        } else if (account.length() >= end) {
            builder.append(account.substring(0, start));
            for (int i = 0; i < offset; i++) {
                builder.append("*");
            }
            builder.append(account.substring(end, account.length()));
        } else {
            builder.append(account);
        }

        return builder.toString();
    }


    public static String getDisplayName(String name) {
        return getDisplayStr(name, 5);
    }


    public static String getDisplayStr(String name, int maxDisplayLength) {
        if (name != null && name.length() > maxDisplayLength) {
            StringBuilder builder = new StringBuilder(name.substring(0, maxDisplayLength - 1));
            builder.append("...");
            return builder.toString();
        } else {
            return name;
        }
    }


    public static String toDate(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat sdfnew = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Date date = sdf.parse(dateStr);
        return sdfnew.format(date);
    }


    public static String createGetUrl(final String urlstr,
                                      final Map<String, String> map) {
        StringBuilder sb = new StringBuilder(urlstr);
        int len = 0;
        int mapsize = 0;
        if (map != null && map.size() > 0) {
            sb.append("?");
            mapsize = map.size();
            for (String key : map.keySet()) {
                try {
                    len++;
                    sb.append(key);
                    sb.append("=");
                    sb.append(URLEncoder.encode(map.get(key).trim(), "utf-8"));
                    if (len < mapsize) {
                        sb.append("&");
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
        return sb.toString().trim();
    }

    public static int toInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return 0;
        }
    }

    public static long toLong(String string) {
        if (string == null || string.length() == 0) {
            return 0;
        }
        try {
            return Long.parseLong(string);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return 0;
        }
    }

    public static String listToString(List<String> stringList) {
        if (stringList == null || stringList.size() == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < stringList.size(); i++) {
            if (i == stringList.size() - 1) {
                builder.append(stringList.get(i));
            } else {
                builder.append(stringList.get(i)).append(",");
            }
        }
        return builder.toString();
    }

    public static boolean equal(Object a, Object b) {
        return a == b || a != null && a.equals(b);
    }
}
