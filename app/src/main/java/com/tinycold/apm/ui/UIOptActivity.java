package com.tinycold.apm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tinycold.apm.BaseActivity;
import com.tinycold.apm.MainActivity;
import com.tinycold.apm.R;

public class UIOptActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_opt);

        Button btnAdaptSW = findViewById(R.id.btn_adapt_sw);
        Button btnAdaptDensity = findViewById(R.id.btn_adapt_density);

        btnAdaptSW.setOnClickListener(this);
        btnAdaptDensity.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_adapt_sw: {
                startActivity(new Intent(UIOptActivity.this, UIAdaptSWActivity.class));
                break;
            }
            case R.id.btn_adapt_density: {
                startActivity(new Intent(UIOptActivity.this, UIAdaptDensityActivity.class));
                break;
            }
        }
    }
}