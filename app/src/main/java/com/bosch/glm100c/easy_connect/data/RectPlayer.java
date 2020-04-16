package com.bosch.glm100c.easy_connect.data;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class RectPlayer implements GameObject {
    private Rect rectangle;
    private int color;
    public RectPlayer(Rect rectangle, int color){
        this.color =color;
        this.rectangle = rectangle;
    }
    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle,paint);
    }
    @Override
    public void update() {

    }

    public void update(Point p) {
        //l,t,r,b
       rectangle.set(p.x-rectangle.width()/2, p.y-rectangle.height()/2, p.x+rectangle.width()/2, p.y+rectangle.height()/2);
    }
}
