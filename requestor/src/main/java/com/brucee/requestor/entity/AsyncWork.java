package com.brucee.requestor.entity;

import java.util.Map;

public interface AsyncWork<T> {
    RequestResponse doWork(String path, Map<String, String> params, Class<T> clazz);
}
