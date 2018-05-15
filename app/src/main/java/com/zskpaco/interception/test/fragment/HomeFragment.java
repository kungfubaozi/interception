package com.zskpaco.interception.test.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zskpaco.interception.Surround;
import com.zskpaco.interception.VisitorComponent;
import com.zskpaco.interception.test.R;
import com.zskpaco.interception.test.extension.BindView;

/**
 * Author: Richard paco
 * Date: 2018/5/9
 */
public class HomeFragment extends Fragment {

    @Surround
    private Views views;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        VisitorComponent.replaceLine();
        return views.layout;
    }

    @Surround
    static class Views {
        @BindView(value = R.layout.activity_main, inflate = true)
        View layout;
        @BindView(R.id.button)
        Button button;

    }
}
