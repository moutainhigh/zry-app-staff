package com.zhongmei.bty.commonmodule.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class NumberUtil {
        private final static String REGEX_MOBILEPHONE = "^0?1[34578]\\d{9}$";

        private final static String REGEX_FIXEDPHONE = "^(010|02\\d|0[3-9]\\d{2})?\\d{6,8}$";

        private final static String REGEX_ZIPCODE = "^(010|02\\d|0[3-9]\\d{2})\\d{6,8}$";

    private static Pattern PATTERN_MOBILEPHONE;
    private static Pattern PATTERN_FIXEDPHONE;
    private static Pattern PATTERN_ZIPCODE;

    static {
        PATTERN_FIXEDPHONE = Pattern.compile(REGEX_FIXEDPHONE);
        PATTERN_MOBILEPHONE = Pattern.compile(REGEX_MOBILEPHONE);
        PATTERN_ZIPCODE = Pattern.compile(REGEX_ZIPCODE);
    }

    public enum PhoneType {

        CELLPHONE,


        FIXEDPHONE,


        INVALIDPHONE
    }

    public static class Numbers {
                private PhoneType type;

        private String formatNumber;

        private String code;

        public Numbers(PhoneType type, String code, String formatNumber) {
            this.type = type;
            this.formatNumber = formatNumber;
            this.code = code;
        }

        public PhoneType getType() {
            return type;
        }

        public String toString() {
            return String.format("[formatNumber:%s, type:%s, code:%s]", formatNumber, type.name(), code);
        }
    }


    public static boolean isCellPhone(String number) {
        Matcher match = PATTERN_MOBILEPHONE.matcher(number);
        return match.matches();
    }


    public static boolean isFixedPhone(String number) {
        Matcher match = PATTERN_FIXEDPHONE.matcher(number);
        return match.matches();
    }


    public static String getZipFromHomephone(String strNumber) {
        Matcher matcher = PATTERN_ZIPCODE.matcher(strNumber);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }



    public static Numbers checkNumber(String _number) {
        String number = _number;
        Numbers rtNum = null;

        if (number != null && number.length() > 0) {
            if (isCellPhone(number)) {
                                if (number.charAt(0) == '0') {
                    number = number.substring(1);
                }
                String first = number.substring(0, 3);
                String seconed = number.substring(3, 7);

                String formatNum = number.replace(first, first + "-")
                        .replace(seconed, seconed + "-");
                rtNum = new Numbers(PhoneType.CELLPHONE, number.substring(0, 7), formatNum);
            } else if (isFixedPhone(number)) {
                                String zipCode = getZipFromHomephone(number);
                String formatNumber = number;
                if (null != zipCode) {
                    formatNumber = number.replace(zipCode, zipCode + "-");
                }
                rtNum = new Numbers(PhoneType.FIXEDPHONE, zipCode, formatNumber);
            } else {
                rtNum = new Numbers(PhoneType.INVALIDPHONE, null, number);
            }
        }

        return rtNum;
    }

    public static String getFormatNum(String phone) {
        return checkNumber(phone).formatNumber;
    }
}
