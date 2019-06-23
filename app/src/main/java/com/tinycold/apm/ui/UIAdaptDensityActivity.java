package com.tinycold.apm.ui;

import android.os.Bundle;

import com.tinycold.apm.BaseActivity;
import com.tinycold.apm.R;

public class UIAdaptDensityActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIAdapter.setCustomDensity(this, getApplication());
        setContentView(R.layout.activity_ui_adapt_density);
    }

    @Override
    protected void onDestroy() {
        UIAdapter.resetDensity(this, getApplication());
        super.onDestroy();
    }

}
