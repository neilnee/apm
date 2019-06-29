package com.tinycold.apm.ui;

import android.os.Bundle;

import com.tinycold.apm.BaseActivity;
import com.tinycold.apm.R;

/**
 * 使用生成dimens文件的UI适配
 */
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
