package com.zyk.demo.code.mine.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GameView extends View {

    //Game 状态
    public static final int STATUS_RUN = 1; //运行中
    public static final int STATUS_PAUSE = 2; //暂停
    public static final int STATUS_OVER = 3; //结束
    public int status = STATUS_OVER; //默认为结束状态

    //bitmap KEY R.ID
    public HashMap<String, Bitmap> drawableBitmap = new HashMap<String, Bitmap>();

    //精灵
    public List<Sprite> sprites = new ArrayList<Sprite>();

    //字体大小
    public int font_score_size = 12;
    public int font_draw_size = 20;
    public int border_size = 2;

    //帧数
    public int frame = 0;

    //得分
    public int scor = 0;

    //画笔
    public Paint paint = null;
    public Paint fontPaint = null;

    //屏幕密度
    public int density = (int) getResources().getDisplayMetrics().density;

    //主要战机
    public PlaneSprite planeSprite = null;
    public BackGroundSprite backGroundSprite = null;

    public void init(Context context) {
        //初始化属性
        //画笔
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        //字体画笔
        fontPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);
        fontPaint.setColor(0xff000000);
        font_score_size = (int) fontPaint.getTextSize();
        font_score_size *= density;
        font_draw_size *= density;
        fontPaint.setTextSize(font_score_size);
        border_size *= density;
    }

    public GameView(Context context) {
        super(context);
        init(context);
    }

    //必须提供否则会无法启动
    public GameView(Context context, AttributeSet addrs) {
        super(context, addrs);
        init(context);
    }

    public void run(HashMap<String, Integer> bitmapIds) {
        //状态 为 运行中
        status = STATUS_RUN;
        //生成 Bitmap hasMap
        Iterator iterator = bitmapIds.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            drawableBitmap.put(entry.getKey().toString(), BitmapFactory.decodeResource(getResources(), (int) entry.getValue()));
        }
        //生成战机对象
        planeSprite = new PlaneSprite(drawableBitmap.get("plane"), density, paint);
        backGroundSprite = new BackGroundSprite(drawableBitmap.get("gameBg"), paint);
        //绘制UI
        postInvalidate();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (STATUS_RUN == status) {
            drawGameRun(canvas);
        } else if (STATUS_PAUSE == status) {
            drawGamePause(canvas);
        } else if (STATUS_OVER == status) {
            drawGameOver(canvas);
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void drawGameRun(Canvas canvas) {
        //将战机移动到屏幕下方中间 最开始时
        if (0 == frame) {
            planeSprite.moveTo((canvas.getWidth() - planeSprite.width) / 2, (canvas.getHeight() - planeSprite.height - 50));
            backGroundSprite.setSize(canvas.getWidth(), canvas.getHeight());
        }
        frame++;
        backGroundSprite.draw(canvas, status);
        //添加精灵 每隔 30 帧
        if (0 != frame % 30) {
            addSprites(canvas);
        }
        drawSprites(canvas);
        destroySprites();
        planeSprite.draw(canvas, status);
        //重绘UI1
        postInvalidate();
    }

    public void addSprites(Canvas canvas) {
        Sprite sprite;
        if (0 == frame % 5) {
            int rand = (int) (Math.random() * 100);
            //发放子弹
            sprite = new BulletSprite(drawableBitmap.get("yellow_bullet"), density, paint);
            //移动到战机中间
            sprite.moveTo(planeSprite.x + planeSprite.width / 2, planeSprite.y);
            sprites.add(sprite);
            //放大 boss
            if (2 > rand) {
                sprite = new BossSprite(drawableBitmap.get("big"), density, paint);
                //位置于顶部随机
                sprite.moveTo((int) (Math.random() * (canvas.getWidth() - sprite.width)), 0);
                sprites.add(sprite);
            }
            //放小兵
            if (10 > rand) {
                sprite = new SmallSprite(drawableBitmap.get("small"), density, paint);
                //位置于顶部随机
                sprite.moveTo((int) (Math.random() * (canvas.getWidth() - sprite.width)), 0);
                sprites.add(sprite);
            }
        }
    }

    public void drawSprites(Canvas canvas) {
        List<Sprite> newSprite = new ArrayList<Sprite>();
        Iterator<Sprite> iterator = sprites.iterator();
        Iterator<Sprite> bulletIterator = null;
        //双重循环检测是否打中
        while (iterator.hasNext()) {
            Sprite sprite = iterator.next();
            //检测不是爆炸精灵的碰撞
            if (!(sprite instanceof ExplosionSprite)) {
                //所有炮弹精灵的 iterator
                bulletIterator = sprites.iterator();
                while (bulletIterator.hasNext()) {
                    Sprite bulletSprite = bulletIterator.next();
                    if (bulletSprite instanceof BulletSprite) {
                        //当2个 iterator 的对象不同时检测
                        if (sprite != bulletSprite) {
                            //比较两者的矩形区域是否重叠
                            if (null != bulletSprite.bitmap && null != sprite.bitmap && Rect.intersects(sprite.getDescRect(status), bulletSprite.getDescRect(status))) {
                                //生命值 减1 当为0 时销毁对象
                                sprite.life--;
                                if (0 >= sprite.life) {
                                    //销毁物体
                                    sprite.bitmap = null;
                                    //创建爆炸效果图
                                    Sprite expSprite = new ExplosionSprite(drawableBitmap.get("explosion"), density, paint);
                                    //调整位置
                                    expSprite.moveTo(sprite.x, sprite.y);
                                    newSprite.add(expSprite);
                                }
                                //销毁炮弹
                                bulletSprite.bitmap = null;
                            }
                        }
                    }
                }
            }
            sprite.draw(canvas, status);
        }
        //添加爆炸效果图 到 sprites 下次就会绘制出来
        sprites.addAll(newSprite);
    }


    public void drawPauseSprites(Canvas canvas) {
        Iterator<Sprite> iterator = sprites.iterator();
        //双重循环检测是否打中
        while (iterator.hasNext()) {
            Sprite sprite = iterator.next();
            //检测不是爆炸精灵的碰撞
            sprite.draw(canvas, status);
        }
    }

    public void destroySprites() {
        Iterator<Sprite> iterator = sprites.iterator();
        while (iterator.hasNext()) {
            Sprite entry = iterator.next();
            //当 bitmap 为 null 说明需要清理
            if (null == entry.bitmap) {
                iterator.remove();
            }
        }
    }

    private final int touchWith = 30;// 手指的半径

    public boolean onTouchEvent(MotionEvent event) {
        if (status != STATUS_RUN) {
            return true;
        }

        int touchX = (int) event.getX();
        int touchY = (int) event.getY();
        Rect touchRect = new Rect(touchX - touchWith, touchY - touchWith, touchX + touchWith, touchY + touchWith);
        if (Rect.intersects(touchRect, planeSprite.getCurrentRect())) {
            //移动玩家战机
            int x = touchX - planeSprite.width / 2;
            int y = touchY - planeSprite.height / 2;
            planeSprite.moveTo(x, y);
        }

        return true;
    }

    public void drawGamePause(Canvas canvas) {
        backGroundSprite.draw(canvas, status);
        //将战机移动到屏幕下方中间 最开始时
        drawPauseSprites(canvas);
        destroySprites();
        planeSprite.draw(canvas, status);
        //重绘UI1
        postInvalidate();
    }

    public void drawGameOver(Canvas canvas) {

    }

    public void destroy() {

    }

    public void pause() {

    }
}
