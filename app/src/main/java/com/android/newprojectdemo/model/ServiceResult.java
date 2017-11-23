package com.android.newprojectdemo.model;

/**
 * 接口参数
 */

public class ServiceResult<T> {

    private String resultCode;
    private String resultMsg;
    private T resultData;
    private String resultThirdCode;

    private String code;//融云返回码

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public T getResultData() {
        return resultData;
    }

    public void setResultData(T resultData) {
        this.resultData = resultData;
    }

    public String getResultThirdCode() {
        return resultThirdCode;
    }

    public void setResultThirdCode(String resultThirdCode) {
        this.resultThirdCode = resultThirdCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
