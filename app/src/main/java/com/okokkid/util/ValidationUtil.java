package com.okokkid.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author： xuyafan
 * description:
 */
public class ValidationUtil {

    public static boolean isPhoneValid(String phone) {
        return phone.length() == 11 && isNumber(phone);
    }

    public static boolean isUsernameValid(String username) {
        return username.length() >= 3 && !containsChinese(username);
    }

    public static boolean isPasswordValid(String password) {
        return password.length() >= 6 && !containsChinese(password);
    }

    public static boolean isValidationCodeValid(String validationCode) {
        return validationCode.length() == 6 && isNumber(validationCode);
    }

    public static boolean isNumber(String numberStr) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(numberStr);
        return isNum.matches();
    }

    public static boolean containsChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
    }
}
