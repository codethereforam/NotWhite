package com.yay.notwhite;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String result = bundle.getString("result");

        Button b = (Button) findViewById(R.id.bt_restart);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        TextView tv = (TextView) findViewById(R.id.tv_result);
        if(result.equals("win")) {
            tv.setBackgroundColor(Color.GREEN);
            b.setBackgroundColor(Color.GREEN);


            String time = bundle.getString("time");
            tv.setText("成功\n" + time);
        } else {
            tv.setBackgroundColor(Color.RED);
            b.setBackgroundColor(Color.RED);

            tv.setText("失败");
        }
    }
}
