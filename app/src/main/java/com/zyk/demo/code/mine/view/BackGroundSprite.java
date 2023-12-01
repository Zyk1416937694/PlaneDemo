package com.zyk.demo.code.mine.view;


import static com.zyk.demo.code.mine.view.GameView.STATUS_RUN;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class BackGroundSprite {
    public Bitmap background01, background02;
    private Paint paint;
    public int screenW, screenH;
    public Rect rect01, rect02;

    private int speed = 5;// 背景滚动速度

    private int top;

    public BackGroundSprite(Bitmap bitmap, Paint paint) {
        //修改属性
        this.paint = paint;
        this.background01 = bitmap;
        this.background02 = bitmap;

    }

    public void setSize(int screenW, int screenH) {
        this.screenW = screenW;
        this.screenH = screenH;

        this.top = screenH;

        rect01 = new Rect(0, 0, screenW, screenH);
        rect02 = new Rect(0, -screenH, screenW, 0);
    }

    // 游戏时的背景逻辑就是屏幕滚动
    public void draw(Canvas canvas, int status) {
        if (status == STATUS_RUN) {
            // 滚动的实质就是背景上移
            rect01.top += speed;
            rect01.bottom += speed;
            rect02.top += speed;
            rect02.bottom += speed;
            // 使其循环
            if (rect01.top >= top) {
                rect01.top = -screenH;
                rect01.bottom = 40;
            }

            if (rect02.top >= top) {
                rect02.top = - screenH;
                rect02.bottom = 40;
            }
        }

        canvas.drawBitmap(background01, null, rect01, paint);
        canvas.drawBitmap(background02, null, rect02, paint);
    }

}
