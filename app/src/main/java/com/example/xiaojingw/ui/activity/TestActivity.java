package com.example.xiaojingw.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.xiaojingw.R;
import com.example.xiaojingw.ui.custom.TextFlowLayout;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private static final String TAG ="TestActivity" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        TextFlowLayout textFlowLayout = findViewById(R.id.abde);
        Log.d(TAG, "onCreate: "+textFlowLayout);
        List<String> a = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            a.add("发射点发射点");
        }
        textFlowLayout.setTextList(a);
    }
}