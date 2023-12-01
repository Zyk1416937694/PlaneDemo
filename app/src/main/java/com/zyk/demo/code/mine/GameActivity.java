package com.zyk.demo.code.mine;


import static com.zyk.demo.code.mine.view.GameView.STATUS_PAUSE;
import static com.zyk.demo.code.mine.view.GameView.STATUS_RUN;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import com.zyk.demo.code.R;
import com.zyk.demo.code.mine.view.GameView;

import java.util.HashMap;

public class GameActivity extends Activity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameView = (GameView) findViewById(R.id.gameView);

        HashMap<String, Integer> bitmapIds = new HashMap<String, Integer>();
        bitmapIds.put("plane", R.mipmap.plane);
        bitmapIds.put("explosion", R.mipmap.explosion);
        bitmapIds.put("yellow_bullet", R.mipmap.yellow_bullet);
        bitmapIds.put("blue_bullet", R.mipmap.blue_bullet);
        bitmapIds.put("small", R.mipmap.small);
        bitmapIds.put("big", R.mipmap.big);
        bitmapIds.put("bomb_award", R.mipmap.bomb_award);
        bitmapIds.put("bullet_award", R.mipmap.bullet_award);
        bitmapIds.put("pause1", R.mipmap.pause1);
        bitmapIds.put("pause2", R.mipmap.pause2);
        bitmapIds.put("bomb", R.mipmap.bomb);
        bitmapIds.put("gameBg", R.mipmap.game_bg);
        gameView.run(bitmapIds);

        final TextView btn_control = findViewById(R.id.btn_control);
        btn_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameView.getStatus() == STATUS_RUN) {
                    gameView.setStatus(STATUS_PAUSE);
                    btn_control.setText("继续");
                } else if (gameView.getStatus() == STATUS_PAUSE) {
                    gameView.setStatus(STATUS_RUN);
                    btn_control.setText("暂停");
                }
            }
        });
    }

    protected void onPause() {
        super.onPause();
        if (null != gameView) {
            gameView.pause();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (null != gameView) {
            gameView.destroy();
        }
        gameView = null;
    }
}
