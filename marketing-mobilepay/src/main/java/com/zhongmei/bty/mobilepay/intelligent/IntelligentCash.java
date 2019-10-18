package com.zhongmei.bty.mobilepay.intelligent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.text.TextUtils;

public class IntelligentCash {
    private final static int[] CASH_PAR_VALUE = {100, 50, 20, 10, 5};

    public static final ArrayList<Money> getPossibleMoney(String money) {
        if (!TextUtils.isEmpty(money) && isNumeric(money)) {
            return getPossibleMoney(new BigDecimal(money));
        } else {
            return null;
        }
    }

    public static final ArrayList<Money> getPossibleMoney(BigDecimal money) {
        BigDecimal intValue = money.multiply(BigDecimal.valueOf(CASH_PAR_VALUE[0]));
        BigDecimal m100 = intValue.remainder(BigDecimal.valueOf(CASH_PAR_VALUE[0]));
        if (m100.compareTo(BigDecimal.ZERO) > 0) {
            return getPossibleMoney((intValue.divide(BigDecimal.valueOf(CASH_PAR_VALUE[0])).add(BigDecimal.ONE)).longValue());
        } else {
            return getPossibleMoney((intValue.divide(BigDecimal.valueOf(CASH_PAR_VALUE[0]))).longValue());
        }
    }

    public static final ArrayList<Money> getPossibleMoney(long money) {
        return chargeNumber(money);
    }

    private static final ArrayList<Money> chargeNumber(long cashMoney) {
        ArrayList<Money> possibleList = new ArrayList<Money>();
        for (int i = 0; i < CASH_PAR_VALUE.length; i++) {
            Money test = new Money(getPossibleMoney(cashMoney, i));
            if (!possibleList.contains(test)) {
                possibleList.add(test);
            }
        }
        Money test = new Money(cashMoney);
        if (!possibleList.contains(test)) {
            possibleList.add(test);
        }
        Collections.sort(possibleList, new Comparator<Money>() {

            @Override
            public int compare(Money cur, Money last) {
                return (int) (cur.getValue() - last.getValue());
            }

        });

        if (possibleList.size() == 7) {
            possibleList.remove(6);
        }


        return possibleList;
    }

    private static long getPossibleMoney(long money, int idx) {
        long r100 = money / CASH_PAR_VALUE[0];
        long m100 = money % CASH_PAR_VALUE[0];
        long rIndex = m100 / CASH_PAR_VALUE[idx];
        long a = r100 * 100;
        long b = (rIndex + 1) * CASH_PAR_VALUE[idx];
        if (a + b - CASH_PAR_VALUE[idx] == money) {
            return money;
        }
        return a + b;
    }

    private static boolean isNumeric(String str) {
        java.util.regex.Pattern pattern =
                java.util.regex.Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$");         java.util.regex.Matcher match = pattern.matcher(str);
        if (match.matches() == false) {
            return false;
        } else {
            return true;
        }
    }

}
