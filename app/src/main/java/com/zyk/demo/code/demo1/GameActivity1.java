package com.zyk.demo.code.demo1;

import android.app.Activity;
import android.os.Bundle;

import com.zyk.demo.code.R;
import com.zyk.demo.code.demo1.view.GameView1;


public class GameActivity1 extends Activity {

    private GameView1 gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_1);
        gameView = (GameView1)findViewById(R.id.gameView);
        int[] bitmapIds = {
                R.mipmap.plane,
                R.mipmap.explosion,
                R.mipmap.yellow_bullet,
                R.mipmap.blue_bullet,
                R.mipmap.small,
                R.mipmap.middle,
                R.mipmap.big,
                R.mipmap.bomb_award,
                R.mipmap.bullet_award,
                R.mipmap.pause1,
                R.mipmap.pause2,
                R.mipmap.bomb
        };
        gameView.start(bitmapIds);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(gameView != null){
            gameView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(gameView != null){
            gameView.destroy();
        }
        gameView = null;
    }
}