package com.zhongmei.bty.common.util.verfity;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * @description (正则表达式 效验)
 * @time 2015年5月25日
 */
public class VerificationUtil {

    /**
     * 金额校验
     *
     * @param price 金额
     * @param iNum  整数位数
     * @param dNum  小数位数
     * @return
     */
    public static boolean checkPrice(String price, int iNum, int dNum) {
        String regex = "^\\d{0," + iNum + "}+(\\.\\d{0," + dNum + "})?$";
        return Pattern.matches(regex, price);
    }

    /**
     * 金额校验
     *
     * @param symbol 标签
     * @param price  金额
     * @param iNum   整数位数
     * @param dNum   小数位数
     * @return
     */
    public static boolean checkPrice(String symbol, String price, int iNum, int dNum) {
//        if (!TextUtils.isEmpty(symbol)){
        String regex = "^" + symbol + "\\d{0," + iNum + "}+(\\.\\d{0," + dNum + "})?$";
        return Pattern.matches(regex, price);
//        } else {
//            return checkPrice(price, iNum , dNum);
//        }
    }

    /**
     * 验证Email
     *
     * @param email email地址，格式：zhangsan@sina.com，zhangsan@xxx.com.cn，xxx代表邮件服务商
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkEmail(String email) {
        String regex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        return Pattern.matches(regex, email);
    }

    /**
     * 验证全数字
     *
     * @param str 待验证字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkAllNum(String str) {
        String regex = "^[0-9]*$";
        return Pattern.matches(regex, str);
    }

    /**
     * 验证身份证号码
     *
     * @param idCard 居民身份证号码15位或18位，最后一位可能是数字或字母
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIdCard(String idCard) {
        String regex = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";
        return Pattern.matches(regex, idCard);
    }

    /**
     * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
     *
     * @param mobile 移动、联通、电信运营商的号码段
     *               <p>
     *               移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）
     *               、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）
     *               </p>
     *               <p>
     *               联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）
     *               </p>
     *               <p>
     *               电信的号段：133、153、180（未启用）、189
     *               </p>
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkMobile(String mobile) {
        String regex = "(\\+\\d+)?1[34578]\\d{9}$";
        return Pattern.matches(regex, mobile);
    }

    /**
     * 验证固定电话号码
     *
     * @param phone 电话号码，格式：国家（地区）电话代码 + 区号（城市代码） + 电话号码，如：+8602085588447
     *              <p>
     *              <b>国家（地区） 代码 ：</b>标识电话号码的国家（地区）的标准国家（地区）代码。它包含从 0 到 9
     *              的一位或多位数字， 数字之后是空格分隔的国家（地区）代码。
     *              </p>
     *              <p>
     *              <b>区号（城市代码）：</b>这可能包含一个或多个从 0 到 9 的数字，地区或城市代码放在圆括号——
     *              对不使用地区或城市代码的国家（地区），则省略该组件。
     *              </p>
     *              <p>
     *              <b>电话号码：</b>这包含从 0 到 9 的一个或多个数字
     *              </p>
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkPhone(String phone) {
        String regex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";
        return Pattern.matches(regex, phone);
    }

    /**
     * 验证整数（正整数和负整数）
     *
     * @param digit 一位或多位0-9之间的整数
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkDigit(String digit) {
        String regex = "\\-?[1-9]\\d+";
        return Pattern.matches(regex, digit);
    }

    /**
     * 验证整数和浮点数（正负整数和正负浮点数）
     *
     * @param decimals 一位或多位0-9之间的浮点数，如：1.23，233.30
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkDecimals(String decimals) {
        String regex = "\\-?[1-9]{9}\\d+(\\.\\d+)?";
        return Pattern.matches(regex, decimals);
    }

    /**
     * 验证整数 0-99的正整数
     *
     * @param decimals nums
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkNums(String decimals) {
        String regex = "^[1-9][0-9]?$";
        return Pattern.matches(regex, decimals);
    }

    /**
     * 验证空白字符
     *
     * @param blankSpace 空白字符，包括：空格、\t、\n、\r、\f、\x0B
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkBlankSpace(String blankSpace) {
        String regex = "\\s+";
        return Pattern.matches(regex, blankSpace);
    }

    /**
     * 验证中文
     *
     * @param chinese 中文字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkChinese(String chinese) {
        String regex = "^[\u4E00-\u9FA5]+$";
        return Pattern.matches(regex, chinese);
    }

    /**
     * 验证日期（年月日）
     *
     * @param birthday 日期，格式：1992-09-03，或1992.09.03
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkBirthday(String birthday) {
        String regex = "[1-9]{4}([-./])\\d{1,2}\\1\\d{1,2}";
        return Pattern.matches(regex, birthday);
    }

    /**
     * 验证URL地址
     *
     * @param url 格式：http://blog.csdn.net:80/xyang81/article/details/7705960? 或
     *            http://www.csdn.net:80
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkURL(String url) {
        String regex = "((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?";
//		String regex = "((http|ftp|https)://)(([a-zA-Z0-9\\\\._-]+\\\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\\\.[0-9]{1,3}\\\\.[0-9]{1,3}\\\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\\\&%_\\\\./-~-]*)?";
        return Pattern.matches(regex, url);
    }

    /**
     * 匹配中国邮政编码
     *
     * @param postcode 邮政编码
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkPostcode(String postcode) {
        String regex = "[1-9]\\d{5}";
        return Pattern.matches(regex, postcode);
    }

    /**
     * 匹配IP地址(简单匹配，格式，如：192.168.1.1，127.0.0.1，没有匹配IP段的大小)
     *
     * @param ipAddress IPv4标准地址
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIpAddress(String ipAddress) {
        String regex = "[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))";
        return Pattern.matches(regex, ipAddress);
    }

    /**
     * 匹配银行卡卡号
     *
     * @param cardId 银行卡号
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            // 如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * 格式化电话号码 (格式化 删除"+86"," ","-")
     *
     * @param phoneNum 原始号码
     * @return phoneNum 返回电话号码
     */
    public static String replacePhoneNum(String phoneNum) {
        phoneNum = phoneNum.replaceAll("\\ ", "");
        phoneNum = phoneNum.replaceAll("\\+86", "");
        phoneNum = phoneNum.replaceAll("\\-", "");
        return phoneNum;
    }

    /**
     * (格式化 时间 去除服务器传递来的时间中带有的毫秒".0")
     *
     * @param time 时间
     * @return phoneNum 返回电话号码
     */
    public static String replaceMillis(String time) {
        if (!TextUtils.isEmpty(time) && time.indexOf(".") > -1) {
            return time.split("\\.")[0];
        }
        return time;
    }

    /**
     * 效验密码
     *
     * @param password 密码
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkPassword(String password) {
        // TODO 检测密码不合法字符 (?!^\\d+$)不为纯数字
        String regex = "^(?!^[_]+$)([0-9_a-zA-Z]){" + 6 + "," + 18 + "}$";
        return Pattern.matches(regex, password);
    }

    /**
     * 替换 位数为start
     *
     * @param cardNo 卡号
     * @return 替换后的卡号
     */
    public static String replaceCardNum(String cardNo) {
        if (TextUtils.isEmpty(cardNo)) {
            return cardNo;
        } else if (!checkNums(cardNo)) {
            return cardNo;
        } else {
            if (cardNo.length() == 12) {
                return cardNo.replaceAll("(\\d{4})\\d{4}(\\d{4})", "$1****$2");
            } else if (cardNo.length() == 16) {
                return cardNo.replaceAll("(\\d{8})\\d{4}(\\d{4})", "$1****$2");
            } else {
                return cardNo;
            }
        }
    }

    /**
     * 替换 手机号码中 中4为为*
     *
     * @param phone 手机号码
     * @return 替换后的手机号码
     */
    public static String replacePhone(String phone) {
        if (checkMobile(phone)) {
            return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        } else {
            return phone;
        }
    }

    /**
     * 校验智能卡
     *
     * @param cardNum 智能卡
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkCardNum(String cardNum) {
        // TODO 995 823 两种开头的 16位长度，400 800 开头的12位长度
        String regex = "(^(995|823)[\\d]{13}$)|(^(400|800)[\\d]{9}$)";
        return Pattern.matches(regex, cardNum);
    }
}
