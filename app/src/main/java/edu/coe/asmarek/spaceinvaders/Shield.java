package edu.coe.asmarek.spaceinvaders;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Anna on 3/10/17.
 */

public class Shield extends ImageView {

    private int x;
    private int y;

    private int size;

    private int state;


    public Shield(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public Shield(Context context) {
        super(context);
        init();
    }

    public Shield(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        x = 0;
        y = 0;
        size = 175;
        state = 1;

        // Adjust the size of the bug
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
        setLayoutParams(layoutParams);

        setImageResource(R.drawable.shield1);
    }

    public void onCollision() {
        switch (state) {
            case 1:
                setImageResource(R.drawable.shield2);
                break;
            case 2:
                setImageResource(R.drawable.shield3);
                break;
            case 3:
                setImageResource(R.drawable.shield4);
                break;
            case 4:
                setImageResource(R.drawable.shield5);
                break;
            case 5:
                setImageResource(R.drawable.shield6);
                break;
            case 6:
                setImageResource(R.drawable.shield7);
                break;
            case 7:
                setImageResource(R.drawable.shield8);
                break;
            case 8:
                setImageResource(R.drawable.shield9);
                break;
            default:
                setImageBitmap(null);
                break;
        }

        state++;
    }
}
