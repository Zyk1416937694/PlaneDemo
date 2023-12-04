package com.zyk.demo.code.demo1.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * 精灵类，所有其他用于绘制的类的基类
 */
public class Sprite {
    private boolean visible = true;
    private int x = 0;
    private int y = 0;
    private float collideOffset = 0;
    private Bitmap bitmap = null;
    private boolean destroyed = false;
    private int frame = 0;//绘制的次数

    public Sprite(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setVisibility(boolean visible) {
        this.visible = visible;
    }

    public boolean getVisibility() {
        return visible;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        if (bitmap != null) {
            return bitmap.getWidth();
        }
        return 0;
    }

    public int getHeight() {
        if (bitmap != null) {
            return bitmap.getHeight();
        }
        return 0;
    }

    public void move(float offsetX, float offsetY) {
        x += offsetX;
        y += offsetY;
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void centerTo(int centerX, int centerY) {
        int w = getWidth();
        int h = getHeight();
        x = centerX - w / 2;
        y = centerY - h / 2;
    }

    public RectF getRectF() {
        float left = x;
        float top = y;
        float right = left + getWidth();
        float bottom = top + getHeight();
        RectF rectF = new RectF(left, top, right, bottom);
        return rectF;
    }

    public Rect getBitmapSrcRec() {
        Rect rect = new Rect();
        rect.left = 0;
        rect.top = 0;
        rect.right = (int) getWidth();
        rect.bottom = (int) getHeight();
        return rect;
    }

    public RectF getCollideRectF() {
        RectF rectF = getRectF();
        rectF.left -= collideOffset;
        rectF.right += collideOffset;
        rectF.top -= collideOffset;
        rectF.bottom += collideOffset;
        return rectF;
    }

    public Point getCollidePointWithOther(Sprite s) {
        Point p = null;
        RectF rectF1 = getCollideRectF();
        RectF rectF2 = s.getCollideRectF();
        RectF rectF = new RectF();
        boolean isIntersect = rectF.setIntersect(rectF1, rectF2);
        if (isIntersect) {
            p = new Point(Math.round(rectF.centerX()), Math.round(rectF.centerY()));
        }
        return p;
    }

    public final void draw(Canvas canvas, Paint paint, GameView1 gameView) {
        frame++;
        beforeDraw(canvas, paint, gameView);
        onDraw(canvas, paint, gameView);
        afterDraw(canvas, paint, gameView);
    }

    protected void beforeDraw(Canvas canvas, Paint paint, GameView1 gameView) {
    }

    public void onDraw(Canvas canvas, Paint paint, GameView1 gameView) {
        if (!destroyed && this.bitmap != null && getVisibility()) {
            //将Sprite绘制到Canvas上
            Rect srcRef = getBitmapSrcRec();
            RectF dstRecF = getRectF();
            //canvas.drawBitmap(this.bitmap, x, y, paint);
            canvas.drawBitmap(bitmap, srcRef, dstRecF, paint);
        }
    }

    protected void afterDraw(Canvas canvas, Paint paint, GameView1 gameView) {
    }

    public void destroy() {
        bitmap = null;
        destroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public int getFrame() {
        return frame;
    }

    public Rect getCurrentRect() {
        return new Rect(x,  y, x + getWidth(), y + getHeight());
    }
}