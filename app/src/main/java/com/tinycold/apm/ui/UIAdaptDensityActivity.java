package com.tinycold.apm.ui;

import android.os.Bundle;

import com.tinycold.apm.BaseActivity;
import com.tinycold.apm.R;

/**
 * 使用修改density方案的UI适配
 */
public class UIAdaptDensityActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutFit.setCustomDensity(this, getApplication());
        setContentView(R.layout.activity_ui_adapt_density);
    }

    @Override
    protected void onDestroy() {
        LayoutFit.resetDensity(this, getApplication());
        super.onDestroy();
    }

}
