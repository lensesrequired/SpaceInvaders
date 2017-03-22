package edu.coe.asmarek.spaceinvaders;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Anna on 3/10/17.
 */

public class AlienShip extends ImageView {

    static final boolean RIGHT = true;
    static final boolean LEFT = false;

    private int maxX;
    private int maxY;
    private int minX;
    private int minY;
    private int x;
    private int y;

    private int size;

    private int delay;

    private int speed;
    private boolean direction;

    private Handler refreshHandler = new Handler();

    public AlienShip(Context context, int wait) {
        super(context);
        init(wait);
    }

    public AlienShip(Context context, AttributeSet attrs, int wait) {
        super(context, attrs);
        init(wait);
    }

    public AlienShip(Context context, AttributeSet attrs, int defStyleAttr, int wait) {
        super(context, attrs, defStyleAttr);
        init(wait);
    }

    public void setMaxX(int max) {
        maxX = max;
    }
    public void setMaxY(int max) {
        maxY = max;
    }
    public void setMinX(int min) {
        minX = min;
    }
    public void setDirection(boolean dir) { direction = dir; }
    public void setSpeed(int s) { speed = s; }

    private void init(int wait){
        maxX= 800;
        maxY = 1200;
        minX = 0;
        minY = 0;

        x = (maxX) / 2;
        y = (maxY) - 10;
        size = 50;
        direction = RIGHT;

        // Adjust the size of the bug
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
        setLayoutParams(layoutParams);

        speed = 50;

        delay = 800;

        setImageResource(R.drawable.widowship);
        refreshHandler.postDelayed(update, wait);
    }

    public void onCollision() {
        setImageBitmap(null);
    }

    private Runnable update = new Runnable(){
        @Override
        public void run(){
            int newy;
            int newx;

            // Where will the ship go.
            if(direction == RIGHT) {
                newx = Math.round(getX()) + speed;
            } else {
                newx = Math.round(getX()) - speed;
            }

            newy = Math.round(getY()) + (3/2*speed);


            if (newx < maxX && newx > minX) {
                x = newx;
                setX(x);
            }
            if (newx >= maxX) {
                //Log.d("Direction", "Left");
                setDirection(LEFT);
                y = newy;
                setY(y);
                delay = Math.round(delay/4*3);
            }
            if (newx <= minX) {
                //Log.d("Direction", "Right");
                setDirection(RIGHT);
                y = newy;
                setY(y);
                delay = Math.round(delay/4*3);
            }

            invalidate();
            //refreshHandler.removeCallbacks(update);
            refreshHandler.postDelayed(update, delay);
            //setWait(800);
        }};
}
