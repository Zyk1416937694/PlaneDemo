package com.zyk.demo.code;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.zyk.demo.code.mine.GameActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_mine_plane).setOnClickListener(v -> {
            startGame(GameActivity.class);
        });
    }

    private void startGame(Class pointAct) {
        Intent intent = new Intent(this, pointAct);
        startActivity(intent);
    }
}