package com.brucee.request;

import com.brucee.requestor.Requestor;
import com.brucee.requestor.entity.OnRequestListener;
import com.brucee.requestor.entity.RequestBridge;
import com.brucee.requestor.entity.RequestResponse;

import java.util.Map;

public class RequestTester extends Requestor {

    public static void test(OnRequestListener listener) {
        RequestBridge.host = "https://dev3.51zixiu.com/";

        Map<String, String> headers = RequestBridge.headers;
        headers.put("Accept", "application/json");
        headers.put("Accept-Encoding", "gzip");
        headers.put("appid", "8");
        headers.put("appvcode", "1");
        headers.put("appvname", "1.0.0");
        headers.put("City", "shanghai");
        headers.put("osvcode", "27");
        headers.put("osvname", "8.1.0");
        headers.put("TOKEN", "OG1T/dCdNeTxXzXg1eOEjQ9hHcePgMXYvxGDJFri1lXZaqQrWRUanptZJfydla2v");

        asyncWork(() -> {
            RequestResponse<TestInfoEntity> response = doApiRequest("shop/message/read", null, TestInfoEntity.class);
            if (response.isSuccess()) {
                return response.getData();
            } else {
                String json = response.getJson();
            }
            return null;
        }, listener);
    }

    public static class TestInfoEntity {
        public String shop_name;
        public String shop_address;
        public String detailed_address;
    }
}
