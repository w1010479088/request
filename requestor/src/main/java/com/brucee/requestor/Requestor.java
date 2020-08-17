package com.brucee.requestor;

import android.text.TextUtils;

import com.brucee.requestor.entity.AsyncWork;
import com.brucee.requestor.entity.ErrorMessage;
import com.brucee.requestor.entity.OnRequestListener;
import com.brucee.requestor.entity.RequestBridge;
import com.brucee.requestor.entity.RequestResponse;
import com.brucee.requestor.utils.ThreadPool;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Requestor {
    private static final ThreadLocal<ErrorMessage> local = new ThreadLocal();

    public static void asyncWork(final AsyncWork action, final OnRequestListener listener) {
        work(() -> {
            try {
                main(listener::onStart);
                final Object result = action.doWork();
                main(() -> {
                    if (local.get() == null) {
                        listener.onSuccess(result);
                    } else {
                        listener.onFail(local.get());
                    }
                });
            } catch (Exception ex) {
                RequestBridge.logger.log(ex.getMessage());
                final ErrorMessage errorMsg = new ErrorMessage();
                errorMsg.setCode(RequestBridge.CODE_ERROR);
                errorMsg.setMsg(ex.getMessage());
                main(() -> listener.onFail(errorMsg));
            } finally {
                local.remove();
                main(listener::onFinish);
            }
        });
    }

    public static RequestResponse doApiRequest(String path, Map<String, String> params, Class clazz) {
        RequestResponse result = new RequestResponse();
        try {

            Request.Builder builder = new Request
                    .Builder()
                    .url(RequestBridge.host + path);
            builder.post(configParams(params));
            configHeaders(builder);

            Call call = RequestClient.client().newCall(builder.build());
            Response response = call.execute();
            String body = response.body().string();

            result.setSuccess(response.isSuccessful());
            result.setCode(response.code());
            result.setMsg(response.message());
            result.setJson(body);
            if (response.isSuccessful()) {
                Object data = parseJson(body, clazz);
                result.setData(data);
            }
            return result;
        } catch (Exception ex) {
            result.setSuccess(false);
            result.setCode(RequestBridge.CODE_ERROR);
            result.setMsg(ex.getMessage());
            return result;
        }
    }

    public static void sendError(ErrorMessage msg) {
        local.set(msg);
    }

    private static Object parseJson(String body, Class clazz) throws Exception {
        JsonElement jsonElement = JsonParser.parseString(body);
        JsonObject jsonObj = jsonElement.getAsJsonObject();
        JsonElement jsonData = jsonObj.get("data");
        if (jsonData.isJsonArray()) {
            List result = new ArrayList();
            JsonArray jsonArray = jsonData.getAsJsonArray();
            Iterator<JsonElement> iterator = jsonArray.iterator();
            while (iterator.hasNext()) {
                String item = iterator.next().toString();
                result.add(new Gson().fromJson(item, clazz));
            }
            return result;
        } else {
            String item = jsonData.toString();
            return new Gson().fromJson(item, clazz);
        }
    }

    private static RequestBody configParams(Map<String, String> params) {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8");
        StringBuilder paramsBuilder = new StringBuilder("");
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, String>> entries = params.entrySet();
            for (Map.Entry<String, String> item : entries) {
                String value = item.getValue();
                if (!TextUtils.isEmpty(value)) {
                    paramsBuilder.append(item.getKey().trim());
                    paramsBuilder.append("=");
                    paramsBuilder.append(value);
                    paramsBuilder.append("&");
                }
            }
            paramsBuilder.setLength(paramsBuilder.length() - 1);
        }
        return RequestBody.create(mediaType, paramsBuilder.toString());
    }

    private static void configHeaders(Request.Builder builder) {
        Map<String, String> headers = RequestBridge.headers;
        Set<Map.Entry<String, String>> entries = headers.entrySet();
        for (Map.Entry<String, String> item : entries) {
            String value = item.getValue();
            if (!TextUtils.isEmpty(value)) {
                builder.addHeader(item.getKey(), value);
            }
        }
    }

    private static void main(Runnable action) {
        ThreadPool.main(action);
    }

    private static void work(Runnable action) {
        ThreadPool.work(action);
    }
}

class RequestClient {
    private OkHttpClient client;

    private RequestClient() {
        client = new OkHttpClient
                .Builder()
                .callTimeout(RequestBridge.TIMEOUT_CALL, TimeUnit.SECONDS)
                .connectTimeout(RequestBridge.TIMEOUT_CONNECT, TimeUnit.SECONDS)
                .readTimeout(RequestBridge.TIMEOUT_READ, TimeUnit.SECONDS)
                .writeTimeout(RequestBridge.TIMEOUT_WRITE, TimeUnit.SECONDS)
                .build();
    }

    private static class HOLDER {
        private static final RequestClient INSTANCE = new RequestClient();
    }

    public static OkHttpClient client() {
        return HOLDER.INSTANCE.client;
    }
}