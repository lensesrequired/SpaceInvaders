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

    private int speed;
    private boolean direction;
    private int wait;

    private Handler refreshHandler = new Handler();

    public AlienShip(Context context) {
        super(context);
        init();
    }

    public AlienShip(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AlienShip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
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
    public void setWait(int w) { wait = w; }

    private void init(){
        maxX= 800;
        maxY = 1200;
        minX = 0;
        minY = 0;

        x = (maxX) / 2;
        y = (maxY) - 10;
        size = 50;
        direction = RIGHT;

        wait = 0;

        // Adjust the size of the bug
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
        setLayoutParams(layoutParams);

        speed = 50;

        setImageResource(R.drawable.widowship);
        refreshHandler.post(update);
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

            newy = Math.round(getY()) + speed;

            if (newx < maxX && newx > minX) {
                x = newx;
                setX(x);
            }
            if (newx >= maxX) {
                Log.d("Direction", "Left");
                setDirection(LEFT);
                y = newy;
                setY(y);
            }
            if (newx <= minX) {
                Log.d("Direction", "Right");
                setDirection(RIGHT);
                y = newy;
                setY(y);
            }

            invalidate();
            refreshHandler.removeCallbacks(update);
            refreshHandler.postDelayed(update, wait);
            setWait(800);
        }};
}
