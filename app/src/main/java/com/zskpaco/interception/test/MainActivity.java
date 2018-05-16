package com.zskpaco.interception.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zskpaco.interception.Surround;
import com.zskpaco.interception.TypeVisitor;
import com.zskpaco.interception.test.interceptor.Factory;
import com.zskpaco.interception.test.test.MessageAlerts;
import com.zskpaco.interception.test.test.PermissionUtils;

//@Layout(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

//    @Surround
//    private Views views;

    @Surround
    private Factories factories;

//    @Factory
//    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypeVisitor.initialize();
    }

//    @Logger
//    private void testLogger(){
//
//    }

    @Surround
    static class Factories{
        @Factory
        MessageAlerts alerts;
        @Factory
        PermissionUtils permissionUtils;
    }

//    @Surround
//    static class Views {
//        @BindView(R.id.button)
//        Button button;
//        @BindView(R.id.button2)
//        Button button2;
//    }
}
