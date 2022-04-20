package com.yay.notwhite;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private final int ROW = 4;
    private final int COLUMN = 3;
    private TextView[][] tv = new TextView[ROW][COLUMN];
    /**
     * the number of black block you have to hit
     */
    private static final int STEEP = 50;
    /**
     * the the number of black block you have hit
     */
    private int count;
    private long startTime;
    private long endTime;
    /**
     * the white block you have hit
     */
    private TextView updateTextView;

    /**
     * the handler to update the updateTextView
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            Log.d("color", msg.what + "");
            updateTextView.setBackgroundColor(msg.what);
        }
    };

    /**
     * initialize every block
     */
    public void init() {
        tv[0][0] = (TextView) findViewById(R.id.tv00);
        tv[0][1] = (TextView) findViewById(R.id.tv01);
        tv[0][2] = (TextView) findViewById(R.id.tv02);
        tv[1][0] = (TextView) findViewById(R.id.tv10);
        tv[1][1] = (TextView) findViewById(R.id.tv11);
        tv[1][2] = (TextView) findViewById(R.id.tv12);
        tv[2][0] = (TextView) findViewById(R.id.tv20);
        tv[2][1] = (TextView) findViewById(R.id.tv21);
        tv[2][2] = (TextView) findViewById(R.id.tv22);
        tv[3][0] = (TextView) findViewById(R.id.tv30);
        tv[3][1] = (TextView) findViewById(R.id.tv31);
        tv[3][2] = (TextView) findViewById(R.id.tv32);
    }

    /**
     * make a block black every row randomly and make others white
     */
    public void randomBlack() {
        for (TextView[] ee : tv)
            for (TextView e : ee) {
                e.setBackgroundColor(Color.WHITE);
            }

        for (int i = 0; i < ROW; i++) {
            Random x = new Random();
            int j = x.nextInt(COLUMN);
            tv[i][j].setBackgroundColor(Color.BLACK);
        }
    }

    /**
     * the action of hitting black block
     */
    public void clickBlack() {
        //make 1~ROW row's color same as what above them
        for (int i = ROW - 2; i >= 0; i--)
            for (int j = 0; j < COLUMN; j++) {
                Drawable drawable = ((TextView) tv[i][j]).getBackground();
                ColorDrawable dra = (ColorDrawable) drawable;
                tv[i + 1][j].setBackgroundColor(dra.getColor());
            }

        //make all the blocks in tht first row white
        for (int j = 0; j < COLUMN; j++) {
            tv[0][j].setBackgroundColor(Color.WHITE);
        }

        //make a block black randomly in the first row
        Random x = new Random();
        int j = x.nextInt(COLUMN);
        tv[0][j].setBackgroundColor(Color.BLACK);

        count++;

        //record the beginning time
        if (count == 1) {
            startTime = new Date().getTime();
        }

        //make green when aproaching the end
        if (count >= STEEP - 3) {
            for (int i = 0; i < COLUMN; i++) {
                tv[0][i].setBackgroundColor(Color.GREEN);
            }
        }

        //the action of the success
        if (count == STEEP) {
            endTime = new Date().getTime();
            finish();
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra("result", "win");
            intent.putExtra("time", (endTime - startTime) / 1000.0 + "\"");
            startActivity(intent);
        }
    }

    /**
     * the action of failure
     */
    public void fail() {
        //make all TextView disable
        for (TextView[] ee : tv)
            for (TextView e : ee) {
                e.setEnabled(false);
            }

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean red = false;
                //make the hit white block blink
                for (int i = 0; i < 5; i++) {
                    if (red) {
                        handler.sendEmptyMessage(Color.BLACK);
                        red = false;
                    } else {
                        handler.sendEmptyMessage(Color.RED);
                        red = true;
                    }
                    try {
                        TimeUnit.MILLISECONDS.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //make the activity end and start another activity
                finish();
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("result", "fail");
                startActivity(intent);
            }
        }).start();
    }

    public void setAllListener() {
        for (TextView[] ee : tv) {
            for (TextView e : ee) {
                e.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final TextView textView = (TextView) v;
                        Drawable drawable = textView.getBackground();
                        ColorDrawable dra = (ColorDrawable) drawable;
                        if (dra.getColor() == Color.WHITE) {
                            updateTextView = textView;
                            fail();
                        }
                    }
                });
            }
        }
    }

    public void setLastRowListener() {
        for (int i = 0; i < COLUMN; i++) {
            tv[ROW - 1][i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final TextView textView = (TextView) v;
                    Drawable drawable = textView.getBackground();
                    ColorDrawable dra = (ColorDrawable) drawable;
                    dra.getColor();

                    if (dra.getColor() == Color.BLACK) {
                        clickBlack();
                    } else {
                        updateTextView = textView;
                        fail();
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        randomBlack();
        setAllListener();
        setLastRowListener();
    }
}
