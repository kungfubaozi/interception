package com.zskpaco.interception.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.zskpaco.apptest.Logger;
import com.zskpaco.interception.Surround;
import com.zskpaco.interception.VisitorComponent;
import com.zskpaco.interception.test.data.UserInfo;
import com.zskpaco.interception.test.interceptor.BindView;
import com.zskpaco.interception.test.interceptor.Factory;
import com.zskpaco.interception.test.interceptor.Layout;

@Layout(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @Surround
    private Views views;

    @Factory
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VisitorComponent.replaceLine();
    }

    @Logger
    private void testLogger(){

    }

    @Surround
    static class Views {
        @BindView(R.id.button)
        Button button;
        @BindView(R.id.button2)
        Button button2;
    }
}
