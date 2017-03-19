package edu.coe.asmarek.spaceinvaders;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import static android.R.attr.delay;

/**
 * Created by Anna on 3/4/17.
 */

public class PlayerShip extends ImageView {

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

    private Handler refreshHandler = new Handler();

    public PlayerShip(Context context) {
        super(context);
        init();
    }

    public PlayerShip(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlayerShip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setMaxX(int max) {
        maxX = max;
    }
    public void setMaxY(int max) {
        maxY = max;
    }
    public void setDirection(boolean dir) { direction = dir; }
    //public void setSpeed(int s) { speed = s; }

    private void init(){
        maxX= 800;
        maxY = 1200;
        minX = 0;
        minY = 0;

        x = (maxX) / 2;
        y = (maxY) - 10;
        size = 125;

        // Adjust the size of the bug
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
        setLayoutParams(layoutParams);

        speed = 5;

        setImageResource(R.drawable.hawkship);
    }

    public void move(){
        refreshHandler.post(updatePlayerShip);
    }

    public void stop() {
        refreshHandler.removeCallbacks(updatePlayerShip);
    }

    private Runnable updatePlayerShip = new Runnable(){
        @Override
        public void run(){
            int newx;

            // Where will the bug go.
            if (direction) {
                newx = Math.round(getX()) + speed;
            } else {
                newx = Math.round(getX()) - speed;
            }

            //Log.d("Speed", ((Integer) newx).toString() );

            // Did you hit a boundary?
            if ((newx > maxX - speed - 185) || (newx < minX + speed + 28)){

            } else {
                x = newx;
                setX(x);
            }

            invalidate();
            refreshHandler.postDelayed(updatePlayerShip, 10);
        }};
}
