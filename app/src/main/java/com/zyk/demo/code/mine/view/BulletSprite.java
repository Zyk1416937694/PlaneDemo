package com.zyk.demo.code.mine.view;

import android.graphics.Bitmap;
import android.graphics.Paint;

public class BulletSprite extends Sprite {

    public BulletSprite(Bitmap bitmap, int density, Paint paint)
    {
        super(bitmap);
        //修改属性
        this.paint = paint;
        speed = -10;
    }

}
