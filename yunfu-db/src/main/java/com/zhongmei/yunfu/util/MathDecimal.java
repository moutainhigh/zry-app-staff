package com.zhongmei.yunfu.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * @Date：2015-3-26 上午9:24:14
 * @Description: TODO
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class MathDecimal {
    /**
     * 提供加法运算。
     *
     * @Title: add
     * @Description: TODO
     * @Param @param v1
     * @Param @param v2
     * @Param @return TODO
     * @Return float 返回类型
     */
    public static float add(float v1, float v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).floatValue();
    }

    public static BigDecimal add(BigDecimal v1, float v2) {
        BigDecimal b2 = new BigDecimal(v2);
        return v1.add(b2);
    }

    /**
     * 提供减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static float sub(float v1, float v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2).floatValue();
    }

    public static BigDecimal sub(BigDecimal v1, float v2) {
        BigDecimal b2 = new BigDecimal(v2);
        return v1.subtract(b2);
    }

    public static BigDecimal subBigDecimal(float v1, float v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2);
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static float mul(float v1, float v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).floatValue();
    }

    public static BigDecimal multiply(float v1, float v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2);
    }

    public static BigDecimal mul(BigDecimal v1, float v2) {
        BigDecimal b2 = new BigDecimal(v2);
        return v1.multiply(b2);
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static float round(float v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        return b.setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     * @Title: round
     * @Description: TODO
     * @Param @param d
     * @Param @param scale
     * @Param @return TODO
     * @Return double 返回类型
     */
    public static double round(double d, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(d));
        return b.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static BigDecimal round(BigDecimal v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        return v.setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * @Title: round
     * @Description: 无条件进位
     * @Param v
     * @Param scale
     * @Param @return TODO
     * @Return BigDecimal 返回类型
     */
    public static BigDecimal roundUp(BigDecimal v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        return v.setScale(scale, BigDecimal.ROUND_UP);
    }

    /**
     * @Title: round
     * @Description: 无条件进位
     * @Param v
     * @Param scale
     * @Param @return TODO
     * @Return BigDecimal 返回类型
     */
    public static BigDecimal roundDown(BigDecimal v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        return v.setScale(scale, BigDecimal.ROUND_DOWN);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static float div(float v1, float v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        if (v2 == 0) {
            return 0;
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public static float div(BigDecimal v1, float v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        if (v2 == 0) {
            return 0;
        }
        BigDecimal b2 = new BigDecimal(v2);
        return v1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * 求商 保留小数点后数据
     *
     * @Title: div
     * @Description: TODO
     * @Param @param v1
     * @Param @param v2
     * @Param @return TODO
     * @Return double 返回类型
     */
    public static float div(float v1, float v2) {
        if (v2 == 0) {
            return 0;
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2).floatValue();
    }

    /**
     * 优惠比例
     *
     * @Title: getDiscountValue
     * @Description: TODO
     * @Param @param discount
     * @Param @return TODO
     * @Return float 返回类型
     */
    public static float getDiscountValue(float discount) {
        BigDecimal dishDiscount = new BigDecimal(discount);
        BigDecimal one = new BigDecimal(1);
        return one.subtract(dishDiscount.divide(new BigDecimal(100))).floatValue();
    }

    public static BigDecimal getDiscountValueTen(double discount) {
        BigDecimal dishDiscount = new BigDecimal(discount);
        BigDecimal one = new BigDecimal(1);
        return one.subtract(dishDiscount.divide(new BigDecimal(10)));
    }

    /**
     * @Title: getDiscountValue
     * @Description: 传入打折信息例如 80折扣时转换为 0.2
     * @Param @param discount
     * @Param @return TODO
     * @Return BigDecimal 返回类型
     */
    public static BigDecimal getDiscountValue(BigDecimal discount) {
        BigDecimal one = new BigDecimal(1);
        return one.subtract(discount.divide(new BigDecimal(100)));
    }

    /**
     * @Title: getDiscountValueTen
     * @Description: 传入打折信息例如 8.0折扣时转换为 0.2
     * @Param @param discount
     * @Param @return TODO
     * @Return BigDecimal 返回类型
     */
    public static BigDecimal getDiscountValueTen(BigDecimal discount) {
        BigDecimal one = new BigDecimal(1);
        return one.subtract(discount.divide(new BigDecimal(10)));
    }

    /**
     * @Title: makeDiscountView
     * @Description: 传入打折信息例如 8折扣时转换为 0.2
     * @Param @param discount
     * @Param @return TODO
     * @Return BigDecimal 返回类型
     */
    public static BigDecimal makeDiscountView(BigDecimal discount) {
        BigDecimal one = new BigDecimal(1);
        return one.subtract(discount.divide(new BigDecimal(10)));
    }

    /**
     * 去除小数点后无意义的0。如13.00 --> 13
     *
     * @param value
     * @return
     */
    public static BigDecimal trimZero(BigDecimal value) {
        if (value == null) {
            return value;
        }
        if (value.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        String str = value.toString();
        if (str.indexOf('.') < 0 || !str.endsWith("0")) {
            return value;
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(100);
        numberFormat.setGroupingUsed(false);
        return new BigDecimal(numberFormat.format(Double.valueOf(str)));
    }

    /**
     * @Title: toTrimZeroString
     * @Description: TODO
     * @Param @param value
     * @Param @return TODO
     * @Return String 返回类型
     */
    public static String toTrimZeroString(Number value) {
        if (value == null) {
            return "";
        }
        String str = value.toString();
        if (str.indexOf('.') < 0 || !str.endsWith("0")) {
            return str;
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(100);
        numberFormat.setGroupingUsed(false);
        return numberFormat.format(Double.valueOf(str));
    }

    /**
     * @Title: toTrimZeroString
     * @Description: TODO
     * @Param @param value
     * @Param @return TODO
     * @Return String 返回类型
     */
    public static String toTrimThreeZeroString(Number value) {
        if (value == null) {
            return "";
        }
        String str = value.toString();
        if (str.indexOf('.') < 0 || !str.endsWith("0")) {
            return str;
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(1000);
        numberFormat.setGroupingUsed(false);
        return numberFormat.format(Double.valueOf(str));
    }

    /**
     * @Title: toAbsTrimZeroString 取绝对值
     * @Description: TODO
     * @Param @param value
     * @Param @return TODO
     * @Return String 返回类型
     */
    public static String toAbsTrimZeroString(Number value) {
        if (value == null) {
            return "";
        }
        String str = value.toString();
        if (!str.startsWith("-") && (str.indexOf('.') < 0 || !str.endsWith("0"))) {
            return str;
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(100);
        numberFormat.setGroupingUsed(false);
        return numberFormat.format(Math.abs(Double.valueOf(str)));
    }

    /**
     * 固定数字保留小数点两位
     *
     * @param value
     * @return
     */
    public static String toDecimalFormatString(Number value) {
        if (value == null) {
            return "";
        }
        String str = value.toString();
        if (str.indexOf('.') < 0) {
            return str;
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setGroupingUsed(false);
        return numberFormat.format(Double.valueOf(str));
    }

    /**
     * 如果传入的值为null则返回0，否则返回传入的值
     *
     * @param value
     * @return
     */
    public static BigDecimal nullToZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    public static BigDecimal negate(BigDecimal value) {
        return value == null ? null : value.negate();
    }

    public static BigDecimal add(BigDecimal value1, BigDecimal value2) {
        if (value1 == null) {
            if (value2 != null) {
                return value2;
            }
            return BigDecimal.ZERO;
        } else if (value2 == null) {
            return value1;
        }
        return Decimal.valueOf(value1.add(value2));
    }

    public static BigDecimal div(BigDecimal value1, BigDecimal value2) {
        //加入被除数为0的判断
        if (value2 == null || value2.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return value1.divide(value2, RoundingMode.HALF_UP);
    }

    /**
     * @Title: divDown
     * @Description: 两数相除，保留几位小数点
     * @Param @param value1
     * @Param @param value2
     * @Param @param scale
     * @Param @return TODO
     * @Return BigDecimal 返回类型
     */
    public static BigDecimal divDown(BigDecimal value1, BigDecimal value2, int scale) {
        //加入被除数为0的判断
        if (value2 == null || value2.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return value1.divide(value2, scale, RoundingMode.DOWN);
    }

    public static BigDecimal divUp(BigDecimal value1, BigDecimal value2, int scale) {
        //加入被除数为0的判断
        if (value2 == null || value2.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return value1.divide(value2, scale, RoundingMode.UP);
    }

    public CarryBitRule mathRule(BigDecimal before, BigDecimal after) {

        int length =
                MathDecimal.trimZero(before).toString().replace(".", "").length()
                        - MathDecimal.trimZero(after).toString().replace(".", "").length();
        //这是一段硬编码，其中2是指小数位数最长为2
        int limit = 2 - length;

        if (limit < 0) {
            return null;
        }
        if (after.compareTo(before) == 0) {
            return null;
        } else if (MathDecimal.round(before, limit).compareTo(after) == 0) {
            return CarryBitRule.ROUND_UP;
        } else if (MathDecimal.roundUp(before, limit).compareTo(after) == 0) {
            return CarryBitRule.CARRY;
        } else if (MathDecimal.roundDown(before, limit).compareTo(after) == 0) {
            return CarryBitRule.MALING;
        } else {
            return null;
        }
    }

    public static boolean isZero(BigDecimal compareValue) {
        if (compareValue == null) {
            return false;
        }
        return compareValue.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * 判断两个Long是否相等
     *
     * @param compare1
     * @param compare2
     * @return
     */
    public static boolean isLongEqual(Long compare1, Long compare2) {
        if (compare1 == null || compare2 == null) {
            return false;
        }
        return compare1.compareTo(compare2) == 0;
    }

    public static BigDecimal threeEightUp(BigDecimal totalPrice, int limit) {
        if (limit <= 0) {
            return totalPrice.setScale(0, RoundingMode.DOWN);
        }

        int intValue = totalPrice.multiply(BigDecimal.valueOf(Math.pow(10, limit))).intValue() % 10;
        int atom = Math.abs(intValue);
        int a1 = atom / 5;
        int a2 = (atom % 5) >= 3 ? 5 : 0;
        double b = BigDecimal.valueOf(a1 * 5 + a2).divide(BigDecimal.valueOf(Math.pow(10f, limit))).doubleValue();
        b = intValue > 0 ? b : -b;
        BigDecimal bbb = totalPrice.setScale(limit - 1, RoundingMode.DOWN).add(BigDecimal.valueOf(b));
        return bbb.setScale(limit, RoundingMode.DOWN);
    }

    /**
     * 是否比0大
     *
     * @param compare
     * @return
     */
    public static boolean isCompareZeroGe(BigDecimal compare) {
        if (compare == null) {
            return false;
        }
        return compare.compareTo(BigDecimal.ZERO) > 0;
    }
}
