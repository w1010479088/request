package com.brucee.request;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.brucee.requestor.entity.ErrorMessage;
import com.brucee.requestor.entity.OnRequestListener;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.confirm).setOnClickListener(v -> {
            confirm();
        });
    }

    private void confirm() {
        RequestTester.test(new OnRequestListener<RequestTester.TestInfoEntity>() {
            @Override
            public void onStart() {
                log("onStart");
            }

            @Override
            public void onSuccess(RequestTester.TestInfoEntity result) {
                log("onSuccess->" + new Gson().toJson(result));
            }

            @Override
            public void onFail(ErrorMessage errorMsg) {
                log("onFail->" + errorMsg.getMsg());
            }

            @Override
            public void onFinish() {
                log("onFinish");
            }
        });
    }

    private void log(String msg) {
        Log.d("tester", msg);
    }
}