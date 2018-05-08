package com.zskpaco.interception.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zskpaco.interception.Surround;

@Surround
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
