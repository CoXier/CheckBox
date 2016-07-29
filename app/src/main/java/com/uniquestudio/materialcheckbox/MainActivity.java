package com.uniquestudio.materialcheckbox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.uniquestudio.library.CircleCheckBox;

public class MainActivity extends AppCompatActivity {
    CircleCheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkBox = (CircleCheckBox) findViewById(R.id.circle_check_box);
        checkBox.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                // do something
            }
        });
    }
}
