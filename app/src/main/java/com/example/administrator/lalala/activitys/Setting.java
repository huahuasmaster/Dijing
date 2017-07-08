package com.example.administrator.lalala.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import com.example.administrator.lalala.R;
import com.example.administrator.lalala.assistant.SwipeBackHelper;

public class Setting extends AppCompatActivity {
    private Toolbar mtoolbar;
    //以下是滑动返回功能实现的初始化代码
    private float xDown;
    private float yDown;
    private float xMove;
    private float yMove;
    private VelocityTracker mVelocityTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mtoolbar = (Toolbar)findViewById(R.id.tasktoolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setTitle("设置");
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
