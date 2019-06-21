package com.tinycold.apm.ui;

import android.os.Bundle;

import com.tinycold.apm.BaseActivity;
import com.tinycold.apm.R;

public class UIAdaptActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIAdapter.setCustomDensity(this, getApplication());
        setContentView(R.layout.activity_ui_adapt);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
