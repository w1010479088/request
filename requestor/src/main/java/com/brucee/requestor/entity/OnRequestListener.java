package com.brucee.requestor.entity;

public abstract class OnRequestListener<T> {
    public void onStart() {
    }

    public void onSuccess(T result) {
    }

    public void onFail(ErrorMessage errorMsg) {
    }

    ;

    public void onFinish() {
    }
}
