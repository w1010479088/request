package com.brucee.requestor.entity;

import com.brucee.requestor.utils.RequestLogger;

import java.util.HashMap;
import java.util.Map;

public class RequestBridge {
    public static String host = "";
    public static int CODE_SUCCESS = 1;
    public static int CODE_ERROR = 20;
    public static int TIMEOUT_CALL = 6;
    public static int TIMEOUT_CONNECT = 6;
    public static int TIMEOUT_READ = 20;
    public static int TIMEOUT_WRITE = 20;
    public static RequestLogger logger = new RequestLogger();
    public static final Map<String, String> headers = new HashMap<>();
}
