package com.brucee.requestor.entity;

public class RequestResponse<T> {
    private boolean success;
    private int code;
    private String msg;
    private String json;
    private T data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ErrorMessage getErrorMessage() {
        ErrorMessage errorMsg = new ErrorMessage();
        errorMsg.setCode(code);
        errorMsg.setMsg(this.msg);
        return errorMsg;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
