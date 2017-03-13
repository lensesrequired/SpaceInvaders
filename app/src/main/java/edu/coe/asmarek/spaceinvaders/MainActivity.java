package edu.coe.asmarek.spaceinvaders;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.delay;
import static android.R.attr.layout_x;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener{

    private FrameLayout topFrame;
    private FrameLayout bottomFrame;
    private PlayerShip ps;
    private int height;
    private int width;
    private int lastX;
    private int lastY;

    private Handler refreshHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        height = metrics.heightPixels;
        width = metrics.widthPixels;

        topFrame = (FrameLayout) findViewById(R.id.top);
        RelativeLayout.LayoutParams tlp = new RelativeLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height-275);
        topFrame.setLayoutParams(tlp);

        bottomFrame = (FrameLayout) findViewById(R.id.bottom);
        RelativeLayout.LayoutParams blp = new RelativeLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 275);
        bottomFrame.setLayoutParams(blp);
        bottomFrame.setY(height-275);

        ps = new PlayerShip(this);
        ps.setMaxX(width);
        ps.setMaxY(height);
        ps.setX((width / 2)-110);
        ps.setY(25);
        bottomFrame.addView(ps);

        for(int i = 0; i < 4; i++) {
            Shield s = new Shield(this);
            s.setX(200 + (i*((width-250)/4)));
            s.setY(height-450);
            topFrame.addView(s);
        }

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 5; j++) {
                int w = 75 + (7*((width-250)/16));
                int h = 75 + (4*((height-(height/2))/8));

                AlienShip as = new AlienShip(this);

                as.setX(Math.round(75 + (i*((width-250)/16))));
                as.setY(Math.round(75 + (j*((height-(height/2))/8))));
                as.setMaxX(width - 100 - (w - Math.round(as.getX())));
                as.setMaxY(height - Math.round(as.getY()) - h);
                as.setMinX(Math.round(as.getX()));
                as.setSpeed((75 + (7*((width-250)/16)) - (75 + (6*((width-250)/16))))/2);
                as.setWait(1000*j);

                topFrame.addView(as);
            }
        }

        topFrame.setOnTouchListener(this);
        bottomFrame.setOnTouchListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int touchX = (int)event.getX();
        int touchY = (int)event.getY();

        if (v.getId() == R.id.bottom) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    if (Math.abs(lastX - touchX) > 10 || Math.abs(lastY - touchY) > 10) {
                        ps.setDirection(touchX > ps.getX());
                        refreshHandler.removeCallbacks(updatePlayerShip);
                        refreshHandler.post(updatePlayerShip);
                    }
                    lastX = touchX;
                    lastY = touchY;
                    break;

                case MotionEvent.ACTION_DOWN:
                    lastX = touchX;
                    lastY = touchY;

                    ps.setDirection(touchX > ps.getX());
                    refreshHandler.post(updatePlayerShip);
                    break;

                case MotionEvent.ACTION_UP:
                    refreshHandler.removeCallbacks(updatePlayerShip);
                    break;
            }
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = touchX;
                    lastY = touchY;

                    //Log.d("y", ((Integer) Math.round(ps.getY())).toString());
                    PlayBullet b = new PlayBullet(this);
                    b.setX(ps.getX());
                    b.setY(height - 200);
                    topFrame.addView(b);
                    break;
            }
        }
        return true;
    }

    private Runnable updatePlayerShip = new Runnable(){
        @Override
        public void run(){
            ps.move();
            ps.invalidate();
            refreshHandler.postDelayed(updatePlayerShip, 10);
        }};
}
