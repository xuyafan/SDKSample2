package com.okokkid.net;

/**
 * author： xuyafan
 * description:
 */
public class Response<T> {
    private String status; //状态
    private String errorCode; //错误信息
    private T data; //数据

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
