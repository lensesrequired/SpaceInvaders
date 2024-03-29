package edu.coe.asmarek.spaceinvaders;

import android.content.Context;
import android.media.Image;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import static android.R.attr.delay;

/**
 * Created by Anna on 3/10/17.
 */

public class PlayBullet extends ImageView {

    private int minY;
    private int x;
    private int y;

    private int size;

    private int speed;

    private boolean active;

    private Handler refreshHandler = new Handler();

    public PlayBullet(Context context) {
        super(context);
        init();
    }

    public PlayBullet(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlayBullet(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

        minY = 0;

        //Log.d("xC", ((Integer) xCoord).toString());
        //Log.d("yC", ((Integer) yCoord).toString());

        x = 0;
        y = 0;
        size = 75;

        // Adjust the size of the bug
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
        setLayoutParams(layoutParams);

        speed = 15;
        active = true;

        setImageResource(R.drawable.hawkarrow);
        refreshHandler.post(update);

        //Log.d("newBullet", "newBullet");
    }

    private void removeImage() {
        setImageBitmap(null);
        active = false;
    }

    public boolean getActiveStatus() { return active; }

    public void onCollision() {
        setImageBitmap(null);
        active = false;
    }

    private Runnable update = new Runnable(){
        @Override
        public void run(){
            int newy;

            // Where will the bug go.
            newy = Math.round(getY()) - speed;

            y = newy;
            setY(y);

            if (y > 0) {
                invalidate();
                refreshHandler.postDelayed(update, 1);
            } else {
                //Log.d("Else", "else");
                removeImage();
            }
        }};
}
