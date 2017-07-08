package com.example.administrator.lalala.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.administrator.lalala.ATHelper.ATHelper_Post;
import com.example.administrator.lalala.R;
import com.example.administrator.lalala.activiytsHelper.BaseActivity;
import com.example.administrator.lalala.assistant.ItemAdapter;
import com.example.administrator.lalala.assistant.StringHandler;
import com.example.administrator.lalala.assistant.SwipeBackHelper;
import com.example.administrator.lalala.assistant.TaskTypeHandler;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import entity.ItemBean;
import entity.TaskBean;

/**
 * Created by Administrator on 2017/4/18.
 */

public class TaskDetAdm extends BaseActivity implements View.OnClickListener {
    //对view的初始化
    private Toolbar mtoolbar;
    private Button btn_finishTask;
    private ProgressBar progressBar;
    //    private ExpandableListView mExpandableListView;
    private RecyclerView recyclerView;
    private FloatingActionButton fab_callAdm;
    private FloatingActionButton fab_feedback;

    private MaterialDialog dialog;

    //对数据源的初始化
    private TaskBean currentTask;//当前任务
    private String title;
    private double countRequredNum;
    private double countTokenNum;
    private int currentProg = 0;
    private int status = 0;//中间量
    private int finalProg;
    private List<ItemBean> currentItem = new ArrayList<>();//当前任务所包含的货物列表
    //以下是对进度条的处理
    Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if(msg.what==0x11)
            {
                progressBar.setProgress(status);
            }
        }
    };
    //以下是滑动返回功能实现的初始化代码
    private float xDown;
    private float yDown;
    private float xMove;
    private float yMove;
    private VelocityTracker mVelocityTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_det_adm);

        //获取从上一个页面传递过来的task
        currentTask = (TaskBean) getIntent().getSerializableExtra("currentTask_data");
        currentItem = currentTask.getItems();

        //以下是对views的初始化
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
//        mExpandableListView= (ExpandableListView) findViewById(R.id.list_goods);
        btn_finishTask = (Button) findViewById(R.id.btn_finish);
        recyclerView = (RecyclerView) findViewById(R.id.list_goods);
        fab_callAdm = (FloatingActionButton)findViewById(R.id.fab_1);
        fab_feedback = (FloatingActionButton)findViewById(R.id.fab_2);

        fab_callAdm.setOnClickListener(this);
        fab_feedback.setOnClickListener(this);

        //以下是对toolbar的初始化
        mtoolbar = (Toolbar) findViewById(R.id.tasktoolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title = TaskTypeHandler.handler(currentTask.getTask_type());
        if (currentTask.isManager_task_finished())//已经由管理员确认完成
        {
            turnToFinishedTask();
        }
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setSubtitle("发布时间：" + currentTask.getTask_date());

        //以下是对各个按钮的监听事件初始化
        btn_finishTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentProg < 100) {
                    showConfirm();
                } else {
                    finishTask();
                }
            }
        });

        //以下是对任务详情的填充
        countRequredNum = currentTask.getItems().size();
        countTokenNum = 0;
        //计算出已完成的货物数量
        for (ItemBean itemBean : currentTask.getItems()) {
            if (currentTask.getTask_type().equals(TaskBean.TASKTYPE_IMPORT)) {
                if (StringHandler.compareTargetAndLocation(itemBean.getItem_target(), itemBean.getLocation())) {
                    countTokenNum++;
                }
            }
            else if(itemBean.isItem_finished()) {
                countTokenNum++;
            }
        }
        initProgressBar(countRequredNum,countTokenNum);
        //以下是对recyclerview的初始化
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        ItemAdapter itemAdapter = new ItemAdapter(currentItem, currentTask.getTask_type());
        recyclerView.setAdapter(itemAdapter);

    }

    private void initProgressBar(double countRequredNum,double countTokenNum) {
        finalProg = (int) (countTokenNum / countRequredNum * 100);
        final boolean isIncreasing = progressBar.getProgress() <= finalProg;//如果当前的进度小于最终的进度,则认为其增长
        new Thread()
        {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if(isIncreasing) {
                    while (status < finalProg) {
                        status = doWork(isIncreasing);
                        handler.sendEmptyMessage(0x11);
                    }
                } else {
                    while (status > finalProg) {
                        status = doWork(isIncreasing);
                        handler.sendEmptyMessage(0x11);
                    }
                }
            }
        }.start();
    }
    private int doWork(boolean isIncreasing) {
        if(isIncreasing) {
            status++;
        }
        else {
            status--;
        }
        try {
            Thread.sleep((int)(625/(finalProg - currentProg)));
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return status;
    }
    private void showConfirm(){
        MaterialDialog confirm = new  MaterialDialog.Builder(this)
                .title("完成任务？")
                .content("当前任务完成了"+ progressBar.getProgress() +"%,是否直接完成？工人将不会继续任务。")
                .positiveText("继续")
                .negativeText("取消")
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if(which == DialogAction.POSITIVE) {
                            finishTask();
                        }
                    }
                })
                .show();
    }
    private  void turnToFinishedTask() {
        title +=getResources().getString(R.string.taskHasFinished);
        getSupportActionBar().setTitle(title);
        btn_finishTask.setText("已完成");
        btn_finishTask.setEnabled(false);
        fab_callAdm.setVisibility(View.GONE);
        fab_callAdm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    private void finishTask() {

        //显示等待界面
        dialog = new MaterialDialog.Builder(this)
                .title("提交中")
                .content("请稍等...")
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .show();
        new ATHelper_SendFinished(Login.Usip,Login.sessionID,"setTaskFinished",currentTask.getTask_code(),currentTask.getTask_type()).execute();


    }
    private class ATHelper_SendFinished extends ATHelper_Post{
        private String urlx;
        private String task_code;
        private String task_type;
        public ATHelper_SendFinished(String ip, String SessionID,String urlx,String task_code,String task_type) {
            super(ip, SessionID);
            this.urlx = urlx;
            this.task_code = task_code;
            this.task_type = task_type;
        }

        @Override
        protected String doInBackground(String... strings) {
            String url ;
            url = "http://"+getIp()+":8080/DiJing/"+urlx+"?from=1&task_code="+task_code+"&task_type="+task_type;

            HttpURLConnection con;
            InputStream is ;

            try {
                con = (HttpURLConnection) new URL(url).openConnection();
                con.setRequestProperty("Cookie",getSessionID());
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setConnectTimeout(3000);

                if(con.getResponseCode()==200)
                {
                    is = con.getInputStream();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return "1";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            currentTask.setManager_task_finished(true);
            dialog.dismiss();
            turnToFinishedTask();
            new MaterialDialog.Builder(TaskDetAdm.this)
                    .title("提交成功")
                    .content("当前任务已为完成状态")
                    .positiveText("好的")
                    .show();
        }
    }



    private void notifiAll() {
        Toast.makeText(this,"已提醒所有员工",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {


            case R.id.fab_1:
                notifiAll();
                break;
            case R.id.fab_2:
                intent.setClass(TaskDetAdm.this,Feedback.class);
                intent.putExtra("project",title+"("+currentTask.getTask_code()+")");
                intent.putExtra("currentTask",currentTask);
                startActivity(intent);
                break;
            default:break;
        }
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
