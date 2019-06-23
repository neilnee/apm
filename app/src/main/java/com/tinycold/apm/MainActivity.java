package com.tinycold.apm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tinycold.apm.ui.UIOptActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnUI = findViewById(R.id.btn_ui);
        Button btnIO = findViewById(R.id.btn_io);

        btnUI.setOnClickListener(this);
        btnIO.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ui: {
                startActivity(new Intent(MainActivity.this, UIOptActivity.class));
                break;
            }
            case R.id.btn_io: {

                break;
            }
        }
    }
}
