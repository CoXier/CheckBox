package com.uniquestudio.materialcheckbox;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.uniquestudio.library.CircleCheckBox;

public class MainActivity extends AppCompatActivity {
    CircleCheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_main);
        checkBox = (CircleCheckBox) findViewById(R.id.circle_check_box);
        checkBox.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                // do something
            }
        });
        if (Build.VERSION.SDK_INT >= 16){
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
