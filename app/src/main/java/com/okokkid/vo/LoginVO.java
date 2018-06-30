package com.okokkid.vo;

/**
 * author： xuyafan
 * description:
 */
public class LoginVO {
    /**
     * password : string
     * phone : string
     * token : string
     * verifyCode : string
     */

    private String password;
    private String phone;
    private String token;
    private String verifyCode;

    public LoginVO() {
    }

    public LoginVO(String password, String phone, String token, String verifyCode) {
        this.password = password;
        this.phone = phone;
        this.token = token;
        this.verifyCode = verifyCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
