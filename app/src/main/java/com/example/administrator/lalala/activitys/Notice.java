package com.example.administrator.lalala.activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.lalala.R;
import com.example.administrator.lalala.assistant.HttpUtil;
import com.example.administrator.lalala.assistant.SwipeBackHelper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Notice extends AppCompatActivity {
    private Toolbar mtoolbar;
    private TextView content;
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView back;
    private FloatingActionButton fab;

    private float xDown;
    private float yDown;
    private float xMove;
    private float yMove;
    //用于计算手指滑动的速度。
    private VelocityTracker mVelocityTracker;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        sp = getPreferences(MODE_PRIVATE);
        mtoolbar = (Toolbar)findViewById(R.id.tasktoolbar);
        back = (ImageView) findViewById(R.id.back);
        content = (TextView)findViewById(R.id.content);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Notice.this,Feedback.class);
                startActivity(intent);
            }
        });

        String bingPic = sp.getString("bing_pic",null);
        if(bingPic != null) {
            Glide.with(this).load(bingPic).into(back);
        }
        else  {
            loadingPic();
        }
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String noticeContent = getIntent().getStringExtra("noticeContent");
        String noticeTitle = getIntent().getStringExtra("noticeTitle");
        collapsingToolbar.setTitle(noticeTitle);
        content.setText(noticeContent);
    }
    private void loadingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(Notice.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(Notice.this).load(bingPic).into(back);
                    }
                });
            }
        });
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        mVelocityTracker = SwipeBackHelper.createVelocityTracker(event,mVelocityTracker);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = event.getRawX();
                yDown = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                xMove = event.getRawX();
                yMove= event.getRawY();
                //滑动的距离
                int distanceX = (int) (xMove - xDown);
                int distanceY= (int) (yMove - yDown);
                //获取顺时速度
                int ySpeed = SwipeBackHelper.getScrollVelocity(mVelocityTracker);
                //关闭Activity需满足以下条件：
                //1.x轴滑动的距离>XDISTANCE_MIN
                //2.y轴滑动的距离在YDISTANCE_MIN范围内
                //3.y轴上（即上下滑动的速度）<XSPEED_MIN，如果大于，则认为用户意图是在上下滑动而非左滑结束Activity
                if(distanceX > SwipeBackHelper.XDISTANCE_MIN
                        &&(distanceY<SwipeBackHelper.YDISTANCE_MIN&&distanceY>-SwipeBackHelper.YDISTANCE_MIN)
                        && ySpeed < SwipeBackHelper.YSPEED_MIN) {
                    finish();
                }
                break;
            case MotionEvent.ACTION_UP:
                SwipeBackHelper.recycleVelocityTracker(mVelocityTracker);
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }



}
