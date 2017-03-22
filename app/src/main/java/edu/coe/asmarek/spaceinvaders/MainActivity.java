package edu.coe.asmarek.spaceinvaders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static android.R.attr.delay;
import static android.R.attr.foregroundGravity;
import static android.R.attr.top;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private FrameLayout topFrame;
    private FrameLayout bottomFrame;
    private PlayerShip ps;
    private int height;
    private int width;
    private int lastX;
    private int lastY;
    private ArrayList<ArrayList<AlienShip>> alienShips;
    private ArrayList<Shield> shields;
    private ArrayList<PlayBullet> playerBullets;
    private ArrayList<AlienBullet> alienBullets;

    TextView currentScore;
    TextView highscore;

    private int score;

    private Handler refreshHandler = new Handler();

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        refreshHandler.post(update);
    }

    private void initView() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        height = metrics.heightPixels;
        width = metrics.widthPixels;

        alienShips = new ArrayList<ArrayList<AlienShip>>();
        shields = new ArrayList<Shield>();
        playerBullets = new ArrayList<PlayBullet>();
        alienBullets = new ArrayList<AlienBullet>();

        topFrame = (FrameLayout) findViewById(R.id.top);
        RelativeLayout.LayoutParams tlp = new RelativeLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height - 225);
        topFrame.setLayoutParams(tlp);

        bottomFrame = (FrameLayout) findViewById(R.id.bottom);
        RelativeLayout.LayoutParams blp = new RelativeLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 225);
        bottomFrame.setLayoutParams(blp);
        bottomFrame.setY(height - 225);

        SharedPreferences sharedPreferences = getSharedPreferences("highscore", 0);
        int Highscore = sharedPreferences.getInt("score", 0);
        score = getIntent().getIntExtra("score", 0);

        LinearLayout ll = new LinearLayout(topFrame.getContext());
        ll.setOrientation(LinearLayout.HORIZONTAL);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        currentScore = new TextView(topFrame.getContext());
        currentScore.setLayoutParams(lp);
        currentScore.setText("Score: " + ((Integer) score).toString() + "\t\t");
        currentScore.setTextColor(Color.WHITE);
        currentScore.setTextSize(20);
        ll.addView(currentScore);

        highscore = new TextView(topFrame.getContext());
        highscore.setLayoutParams(lp);
        highscore.setText("Highscore: " + ((Integer) Highscore).toString());
        highscore.setTextColor(Color.WHITE);
        highscore.setTextSize(20);
        ll.addView(highscore);

        topFrame.addView(ll);

        ps = new PlayerShip(this);
        ps.setMaxX(width);
        ps.setMaxY(height);
        ps.setX((width / 2) - 110);
        ps.setY(0);
        bottomFrame.addView(ps);

        for (int i = 0; i < 4; i++) {
            Shield s = new Shield(this);
            s.setX(200 + (i * ((width - 250) / 4)));
            s.setY(height - 450);
            shields.add(s);
            topFrame.addView(s);
        }

        for (int i = 0; i < 7; i++) {
            alienShips.add(new ArrayList<AlienShip>());
            for (int j = 0; j < 4; j++) {
                int w = 150 + (6 * ((width - 250) / 16));
                int h = 100 + (3 * ((height - (height / 2)) / 8));
                int wait = (150 * (7 - j)) + ((4 - i) * 20);

                AlienShip as = new AlienShip(this, wait);

                as.setX(Math.round(150 + (i * ((width - 250) / 16))));
                as.setY(Math.round(100 + (j * ((height - (height / 2)) / 8))));
                as.setMaxX(width - 275 - (w - Math.round(as.getX())));
                as.setMaxY(height - Math.round(as.getY()) - h);
                as.setMinX(Math.round(as.getX()));
                as.setSpeed((width - 250) / 32);

                alienShips.get(alienShips.size() - 1).add(as);

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
        int touchX = (int) event.getX();
        int touchY = (int) event.getY();

        if (v.getId() == R.id.bottom) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    if (Math.abs(lastX - touchX) > 10 || Math.abs(lastY - touchY) > 10) {
                        ps.setDirection(touchX > ps.getX());
                        ps.stop();
                        ps.move();
                    }
                    lastX = touchX;
                    lastY = touchY;
                    break;

                case MotionEvent.ACTION_DOWN:
                    lastX = touchX;
                    lastY = touchY;

                    ps.setDirection(touchX > ps.getX());
                    ps.move();
                    break;

                case MotionEvent.ACTION_UP:
                    ps.stop();
                    break;
            }
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = touchX;
                    lastY = touchY;

                    //Log.d("y", ((Integer) Math.round(ps.getY())).toString());
                    if (playerBullets.size() == 0) {
                        PlayBullet b = new PlayBullet(this);
                        b.setX(ps.getX() + 25);
                        b.setY(height - 225);
                        playerBullets.add(b);
                        topFrame.addView(b);
                    }
                    break;
            }
        }
        return true;
    }

    private Runnable update = new Runnable() {
        @Override
        public void run() {
            boolean hit = false;
            double alienHeight = alienShips.get(0).get(0).getHeight();
            double alienWidth = alienShips.get(0).get(0).getWidth();
            double shieldHeight = shields.get(0).getHeight();
            double shieldWidth = shields.get(0).getWidth();
            double playerWidth = ps.getWidth();
            if (playerBullets.size() > 0) {
                for (int i = playerBullets.size() - 1; i >= 0; i--) {
                    PlayBullet b = playerBullets.get(i);
                    if (b.getActiveStatus() == false) {
                        playerBullets.remove(b);
                    } else {
                        for (Shield s :
                                shields) {
                            int buffer;
                            switch (s.getState()) {
                                case 3:
                                    buffer = 10;
                                    break;
                                case 5:
                                    buffer = 40;
                                    break;
                                case 7:
                                    buffer = 50;
                                    break;
                                case 9:
                                    buffer = 60;
                                    break;
                                case 10:
                                    buffer = 100;
                                    break;
                                default:
                                    buffer = 0;
                                    break;
                            }
                            Log.d("Buffer", ((Integer) buffer).toString());
                            if (s.getX() + buffer - 30 < b.getX() && b.getX() < (s.getX() - buffer + shieldWidth - 40) && s.getY() < b.getY() && b.getY() < (s.getY() + shieldHeight)) {
                                b.onCollision();
                                s.onCollision();
                            }
                        }

                        if (b.getActiveStatus() == false) {
                            playerBullets.remove(b);
                        } else {
                            for (int n = alienShips.size() - 1; n >= 0; n--) {
                                ArrayList<AlienShip> aList = alienShips.get(n);
                                if (Math.abs(aList.get(0).getX() - b.getX()) < alienWidth) {
                                    for (int j = aList.size() - 1; j >= 0; j--) {
                                        AlienShip a = aList.get(j);
                                        if (a.getX() - 3 < b.getX() && b.getX() < (a.getX() + alienWidth + 3) && a.getY() - 3 < b.getY()
                                                && b.getY() < (a.getY() + alienHeight + 3)) {
                                            a.onCollision();
                                            b.onCollision();
                                            aList.remove(a);
                                            score += 15;
                                            currentScore.setText("Score: " + ((Integer) score).toString() + " \t\t");
                                        }
                                        if (aList.size() == 0) {
                                            alienShips.remove(n);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if ((int) Math.floor(Math.random() * 100) == 0) {
                ArrayList<AlienShip> aList = alienShips.get((int) Math.floor(Math.random() * alienShips.size()));
                AlienShip a = aList.get((int) Math.floor(Math.random() * aList.size()));
                AlienBullet ab = new AlienBullet(context);
                ab.setX(a.getX());
                ab.setY(a.getY());
                ab.setMaxY(height);
                alienBullets.add(ab);
                topFrame.addView(ab);
            }

            if (alienBullets.size() > 0) {
                for (int i = alienBullets.size() - 1; i >= 0; i--) {
                    AlienBullet b = alienBullets.get(i);
                    if (b.getActiveStatus() == false) {
                        alienBullets.remove(b);
                    } else {
                        for (Shield s :
                                shields) {
                            int buffer;
                            switch (s.getState()) {
                                case 3:
                                    buffer = 10;
                                    break;
                                case 5:
                                    buffer = 40;
                                    break;
                                case 7:
                                    buffer = 50;
                                    break;
                                case 9:
                                    buffer = 60;
                                    break;
                                case 10:
                                    buffer = 100;
                                    break;
                                default:
                                    buffer = 0;
                                    break;
                            }
                            Log.d("Buffer", ((Integer) buffer).toString());
                            if (s.getX() + buffer - 30 < b.getX() && b.getX() < (s.getX() - buffer + shieldWidth - 40) && s.getY() < b.getY() + b.getHeight() && b.getY() + b.getHeight() < (s.getY() + shieldHeight)) {
                                b.onCollision();
                                s.onCollision();
                            }
                        }

                        if (b.getActiveStatus() == false) {
                            alienBullets.remove(b);
                        } else {
                            if (ps.getX() < b.getX() && b.getX() < (ps.getX() + playerWidth) && b.getY() + b.getHeight() > b.getMaxY()) {
                                hit = true;
                            }
                        }
                    }
                }
            }

            for (int n = alienShips.size() - 1; n >= 0; n--) {
                ArrayList<AlienShip> aList = alienShips.get(n);
                if (aList.size() > 0) {
                    if (aList.get(aList.size() - 1).getY() + alienHeight > height - 450) {
                        hit = true;
                    }
                }
            }

            if (alienShips.size() == 0) {
                startActivity(new Intent("edu.coe.asmarek.spaceinvaders.Main").putExtra("score", score));

            } else if (hit) {
                refreshHandler.removeCallbacks(update);
                freezeScreen();
                TextView gameOver = new TextView(topFrame.getContext());
                gameOver.setText("GAME OVER");
                gameOver.setTextSize(100);
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                gameOver.setLayoutParams(lp);
                gameOver.setTextColor(Color.WHITE);
                gameOver.setGravity(Gravity.CENTER);
                topFrame.addView(gameOver);

                Button newGame = new Button(bottomFrame.getContext());
                newGame.setText("Play Again");
                newGame.setTextSize(25);
                FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                newGame.setLayoutParams(lp2);
                bottomFrame.addView(newGame);
                newGame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent("edu.coe.asmarek.spaceinvaders.Main").putExtra("score", 0));
                    }
                });

                SharedPreferences sharedPreference = getSharedPreferences("highscore", 0);
                SharedPreferences.Editor e = sharedPreference.edit();
                if (score > sharedPreference.getInt("score", 0)) {
                    e.putInt("score", score);
                }
                e.commit();

            } else {
                refreshHandler.post(update);
            }
        }
    };

    private void freezeScreen() {
        for (int n = alienShips.size() - 1; n >= 0; n--) {
            ArrayList<AlienShip> aList = alienShips.get(n);
            for (int j = aList.size() - 1; j >= 0; j--) {
                AlienShip a = aList.get(j);
                a.onCollision();
            }

        }
    }
}
