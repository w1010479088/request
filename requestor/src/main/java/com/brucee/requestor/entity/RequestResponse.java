package com.brucee.requestor.entity;

public class RequestResponse {
    private boolean success;
    private int code;
    private String msg;
    private String mainData;

    public boolean success() {
        return success;
    }

    public int code() {
        return code;
    }

    public String msg() {
        return msg;
    }

    public String mainData() {
        return mainData;
    }
}
