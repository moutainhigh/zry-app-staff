package com.zhongmei.yunfu.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;


public class MathDecimal {

    public static float add(float v1, float v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).floatValue();
    }

    public static BigDecimal add(BigDecimal v1, float v2) {
        BigDecimal b2 = new BigDecimal(v2);
        return v1.add(b2);
    }


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


    public static float round(float v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        return b.setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }


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


    public static BigDecimal roundUp(BigDecimal v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        return v.setScale(scale, BigDecimal.ROUND_UP);
    }


    public static BigDecimal roundDown(BigDecimal v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        return v.setScale(scale, BigDecimal.ROUND_DOWN);
    }


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


    public static float div(float v1, float v2) {
        if (v2 == 0) {
            return 0;
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2).floatValue();
    }


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


    public static BigDecimal getDiscountValue(BigDecimal discount) {
        BigDecimal one = new BigDecimal(1);
        return one.subtract(discount.divide(new BigDecimal(100)));
    }


    public static BigDecimal getDiscountValueTen(BigDecimal discount) {
        BigDecimal one = new BigDecimal(1);
        return one.subtract(discount.divide(new BigDecimal(10)));
    }


    public static BigDecimal makeDiscountView(BigDecimal discount) {
        BigDecimal one = new BigDecimal(1);
        return one.subtract(discount.divide(new BigDecimal(10)));
    }


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
                if (value2 == null || value2.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return value1.divide(value2, RoundingMode.HALF_UP);
    }


    public static BigDecimal divDown(BigDecimal value1, BigDecimal value2, int scale) {
                if (value2 == null || value2.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return value1.divide(value2, scale, RoundingMode.DOWN);
    }

    public static BigDecimal divUp(BigDecimal value1, BigDecimal value2, int scale) {
                if (value2 == null || value2.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return value1.divide(value2, scale, RoundingMode.UP);
    }

    public CarryBitRule mathRule(BigDecimal before, BigDecimal after) {

        int length =
                MathDecimal.trimZero(before).toString().replace(".", "").length()
                        - MathDecimal.trimZero(after).toString().replace(".", "").length();
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


    public static boolean isCompareZeroGe(BigDecimal compare) {
        if (compare == null) {
            return false;
        }
        return compare.compareTo(BigDecimal.ZERO) > 0;
    }
}
