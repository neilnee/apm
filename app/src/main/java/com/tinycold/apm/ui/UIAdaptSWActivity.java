package com.tinycold.apm.ui;

import android.os.Bundle;

import com.tinycold.apm.BaseActivity;
import com.tinycold.apm.R;

public class UIAdaptSWActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_adapt_sw);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
