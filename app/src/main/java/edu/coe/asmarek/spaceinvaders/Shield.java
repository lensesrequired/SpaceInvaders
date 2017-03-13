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
}
