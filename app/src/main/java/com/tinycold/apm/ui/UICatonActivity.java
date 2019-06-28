package com.tinycold.apm.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tinycold.apm.BaseActivity;
import com.tinycold.apm.R;
import com.tinycold.apm.tool.APMLog;

import java.util.Locale;
import java.util.Random;

public class UICatonActivity extends BaseActivity implements View.OnClickListener {

    private final Random mRandom = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_caton);

        Button btnStart = findViewById(R.id.btn_ui_caton_start);
        Button btnEnd = findViewById(R.id.btn_ui_caton_end);
        Button btnFake = findViewById(R.id.btn_ui_caton_fake);
        Button btnSummary = findViewById(R.id.btn_ui_caton_summary);

        btnStart.setOnClickListener(this);
        btnEnd.setOnClickListener(this);
        btnFake.setOnClickListener(this);
        btnSummary.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ui_caton_start: {
                CatonMonitor.start(CatonMonitor.MONITOR_LOG);
                APMLog.debug("监控启动");
                break;
            }
            case R.id.btn_ui_caton_end: {
                CatonMonitor.end();
                APMLog.debug("监控停止");
                break;
            }
            case R.id.btn_ui_caton_fake: {
                try {
                    int interval = mRandom.nextInt(3000);
                    APMLog.debug(String.format(Locale.getDefault(), "卡顿模拟开始[%d][%d]",
                            interval, System.currentTimeMillis()));
                    Thread.sleep(interval);
                    APMLog.debug(String.format(Locale.getDefault(), "卡顿模拟结束[%d][%d]",
                            interval, System.currentTimeMillis()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
            case R.id.btn_ui_caton_summary: {
                CatonSummary summary = CatonMonitor.summary();
                APMLog.debug(String.format(Locale.getDefault(), "卡顿次数: %d; 卡顿时长: %d",
                        summary.mCatonTimes, summary.mCatonDuration));
                break;
            }
        }
    }
}
