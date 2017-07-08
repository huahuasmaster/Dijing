package com.example.administrator.lalala.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.administrator.lalala.ATHelper.ATHelper_Post;
import com.example.administrator.lalala.assistant.SwipeBackHelper;
import com.getbase.floatingactionbutton.FloatingActionButton;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.administrator.lalala.ATHelper.ATHelper_get;
import com.example.administrator.lalala.R;
import com.example.administrator.lalala.activiytsHelper.BaseActivity;
import com.example.administrator.lalala.assistant.ItemAdapter;
import com.example.administrator.lalala.assistant.StringHandler;
import com.example.administrator.lalala.assistant.TaskTypeHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import entity.Inventory;
import entity.ItemBean;
import entity.TaskBean;

/**
 * Created by Administrator on 2017/4/18.
 */

public class TaskDetStaff extends BaseActivity implements View.OnClickListener{
    //对view的初始化
    private Toolbar mtoolbar;
    private Button btn_useRFID;
    private Button check_button;
    private Button gotoMap;
    private FloatingActionButton fab_callAdm;
    private FloatingActionButton fab_feedback;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshTask;
    private ItemAdapter itemAdapter;
    private String title;
    private MaterialDialog dialog;
    private boolean isFromNotify = false;

//    private ExpandableListView mExpandableListView;
    private RecyclerView recyclerView;
    //对数据源的初始化
    private TaskBean currentTask;//当前任务
    private double countRequredNum;
    private double countTokenNum;
    private String phoneOfManager;
    private int currentProg = 0;
    private int status = 0;//中间量
    private int finalProg;
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
    private List<ItemBean> currentItem = new ArrayList<>();//当前任务所包含的货物列表

    //以下是滑动返回功能实现的初始化代码
    private float xDown;
    private float yDown;
    private float xMove;
    private float yMove;
    private VelocityTracker mVelocityTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //获取从上一个页面传递过来的task


        currentTask = (TaskBean) getIntent().getSerializableExtra("currentTask_data");
        if(currentTask.getTask_type().equals("check_task"))
        {
            setContentView(R.layout.task_det_staff_check);
            check_button = (Button) findViewById(R.id.button_check);
        }else{
            setContentView(R.layout.task_det_staff);
        }

        currentItem = currentTask.getItems();
        phoneOfManager = currentTask.getManagerPhone();

        //以下是对views的初始化
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
//        mExpandableListView= (ExpandableListView) findViewById(R.id.list_goods);
        btn_useRFID = (Button)findViewById(R.id.btn_useRFID);
        recyclerView = (RecyclerView)findViewById(R.id.list_goods);
        gotoMap = (Button) findViewById(R.id.btn_showInMap);
        fab_callAdm = (FloatingActionButton)findViewById(R.id.fab_1);
        fab_feedback = (FloatingActionButton)findViewById(R.id.fab_2);

        gotoMap.setOnClickListener(this);
        fab_callAdm.setOnClickListener(this);
        fab_feedback.setOnClickListener(this);


        //以下是对toolbar的初始化
        mtoolbar = (Toolbar)findViewById(R.id.tasktoolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title = TaskTypeHandler.handler(currentTask.getTask_type());
         if(currentTask.isManager_task_finished())//已经由管理员确认完成
        {
            title +=getResources().getString(R.string.taskHasFinished);
            btn_useRFID.setEnabled(false);
            gotoMap.setVisibility(View.INVISIBLE);
        }
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setSubtitle("发布时间："+ StringHandler.handlerTime(currentTask.getTask_date()));

        //以下是对各个按钮的监听事件初始化



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



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        if(currentTask.getTask_type().equals("check_task"))
        {
            TextView listName = (TextView) findViewById(R.id.food_x);
            listName.setText("附近货物");

                            check_button.setOnClickListener(new View.OnClickListener(){

                                @Override
                                public void onClick(View v) {
                                    final MaterialDialog dialog = new MaterialDialog.Builder(TaskDetStaff.this)
                                            .title("请扫描单层的第一个货物的RFID")
                                            .content("等待操作中...")
                                            .progress(true, 0)
                                            .progressIndeterminateStyle(true)
                                            .show();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                //在这里让刷新动作持续1秒钟，方便看出刷新动作，后续应该删除
                                                Thread.sleep(5000);
                                            }
                            catch(InterruptedException e) {
                                e.printStackTrace();
                            }
                            //这里扫描获取到周围的RFID货物并new一个list<ItemBean> ，送至服务器并查询其相应的当前位置，返回给客户端，更新视图
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //将UI更新放到主线程中 执行新增一条任务 并通知listviiew适配器刷新
                                    dialog.dismiss();
                                    new MaterialDialog.Builder(TaskDetStaff.this)
                                            .title("扫描成功！请移至下一个货物...")
//                                .customView(R.layout.dialog_customview,true)
//                .content("一个简单的dialog,高度会随着内容改变，同时还可以嵌套RecyleView")
                                            .positiveText("确定")
                                            .widgetColor(Color.BLUE)
                                            .show();
                                }
                            });

                        }
                    }).start();

                }
            });

            btn_useRFID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scanX();
                }
            });
            btn_useRFID.setText("扫描附近货物" );

        }else{
            btn_useRFID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openRFID();
                }
            });
            itemAdapter = new ItemAdapter(currentItem,currentTask.getTask_type());
            recyclerView.setAdapter(itemAdapter);

        }

        swipeRefreshTask = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        swipeRefreshTask.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshTask.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            new Async_itemGet(Login.Usip, Login.sessionID,"itemForAndroid",currentTask.getTask_type(),currentTask.getTask_code()).execute();
            }
        });

    }
    private void initProgressBar(double countRequredNum,double countTokenNum) {
        finalProg = (int) (countTokenNum / countRequredNum * 100);
        final boolean isIncreasing = progressBar.getProgress() <= finalProg;//如果当前的进度小于最终的进度,则认为其增长
        new Thread()
        {

            @Override
            public void run() {
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
            e.printStackTrace();
        }
        return status;
    }
    public void openRFID(){
        //显示等待界面
        dialog = new MaterialDialog.Builder(this)
                .title("识别中")
                .content("请稍等...")
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //在这里让刷新动作持续1秒钟，方便看出刷新动作，后续应该删除
                    Thread.sleep(1500);
                }
                catch(InterruptedException e) {
                    e.printStackTrace();
                }

                List<String> code = new ArrayList<String>();
                code.add(0,"10200");
                Gson g = new Gson();
                String str = g.toJson(code);
                new Async_RFIDItemGet(Login.Usip,Login.sessionID,"getInfoByRFID",str).execute();


            }
        }).start();
    }

    public void scanX(){
        //显示等待界面
        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("扫描附近货物中")
                .content("请稍等...")
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //在这里让刷新动作持续1秒钟，方便看出刷新动作，后续应该删除
                    Thread.sleep(1500);
                }
                catch(InterruptedException e) {
                    e.printStackTrace();
                }
                //这里扫描获取到周围的RFID货物并new一个list<ItemBean> ，送至服务器并查询其相应的当前位置，返回给客户端，更新视图


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //将UI更新放到主线程中 执行新增一条任务 并通知listviiew适配器刷新
                        dialog.dismiss();
                        List<String> code = new ArrayList<String>();
                        code.add(0,"10200");
                        code.add(1,"10201");
                        Gson g = new Gson();
                        String str = g.toJson(code);
                        new Async_RFIDListGet(Login.Usip,Login.sessionID,"getInfoByRFID",str).execute();
                        new MaterialDialog.Builder(TaskDetStaff.this)
                                .title("扫描成功！")
                                .positiveText("确定")
                                .widgetColor(Color.BLUE)
                                .show();
                    }
                });
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_showInMap:
                intent.setClass(TaskDetStaff.this,TabsForStaff.class);
                intent.putExtra("nthTab",1);
                intent.putExtra("intentFromTaskDet",true);
                intent.putExtra("taskShowOnMap",currentTask);

                break;
            case R.id.fab_1:
                intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+phoneOfManager));

                break;
            case R.id.fab_2:
                intent.setClass(TaskDetStaff.this,Feedback.class);
                intent.putExtra("project",title+"("+currentTask.getTask_code()+")");
                intent.putExtra("currentTask",currentTask);
                break;
            default:break;
        }
        startActivity(intent);
    }

    private class Async_RFIDItemGet extends ATHelper_get {
        private String jsn;
            public Async_RFIDItemGet(String ip, String SessionID, String urlx,String jsn) {
                super(ip, SessionID, urlx);
                this.jsn = jsn;
            }

            @Override
            protected String doInBackground(String... strings) {
                String url = null;
                try {
                    url = "http://"+getIp()+":8080/DiJing/"+getUrlx()+"?RFID_code="+java.net.URLEncoder.encode(jsn,"utf-8");
                } catch (Exception e) {
                    e.printStackTrace();
                };
                HttpURLConnection con;
                InputStream is ;
                String res = url;
                try {
                    con = (HttpURLConnection) new URL(url).openConnection();
                    con.setRequestProperty("Cookie",getSessionID());
                    con.setConnectTimeout(3000);
                    if(con.getResponseCode()==200)
                    {
                        is = con.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                        StringBuffer sb = new StringBuffer();
                        String str ;

                        while ((str=reader.readLine())!=null){
                            sb.append(str);
                        }
                        res = sb.toString();
                    }else{
                        res = "Error :"+con.getResponseCode();
                    }

                } catch (IOException e) {
                    res = "-1";
                    e.printStackTrace();
                }
                return res;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                if(s.equals("-1"))
                {
                    Toast.makeText(TaskDetStaff.this, "服务器未开启/网络连接失败", Toast.LENGTH_SHORT).show();
                    return;
                }else if(s.indexOf("Error")>0){
                    Toast.makeText(TaskDetStaff.this, s, Toast.LENGTH_SHORT).show();
                }
                Gson gson = new Gson();
                List<Inventory> RFID_list = gson.fromJson(s,new TypeToken<List<Inventory>>() {}.getType());
                Inventory y =RFID_list.get(0);
                MaterialDialog result = new MaterialDialog.Builder(TaskDetStaff.this)
                    .title("识别结果")
                    .customView(R.layout.dialog_customview,true)
                    .positiveText("确定")
                    .widgetColor(Color.BLUE)
                    .show();
                TextView name = (TextView)result.findViewById(R.id.markedItem);
                name.setText(y.getItem_name());
                TextView RFid = (TextView) result.findViewById(R.id.RFID_Num);
                RFid.setText(y.getRFID_code());

            }
        }


    private class Async_RFIDListGet extends ATHelper_get {
        private String jsn;
            public Async_RFIDListGet(String ip, String SessionID, String urlx,String jsn) {
                super(ip, SessionID, urlx);
                this.jsn = jsn;
            }

            @Override
            protected String doInBackground(String... strings) {
                String url = null;
                try {
                    url = "http://"+getIp()+":8080/DiJing/"+getUrlx()+"?RFID_code="+java.net.URLEncoder.encode(jsn,"utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                HttpURLConnection con;
                InputStream is ;
                String res = null;
                try {
                    con = (HttpURLConnection) new URL(url).openConnection();
                    con.setRequestProperty("Cookie",getSessionID());
                    con.setConnectTimeout(3000);
                    if(con.getResponseCode()==200)
                    {
                        is = con.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                        StringBuffer sb = new StringBuffer();
                        String str ;

                        while ((str=reader.readLine())!=null){
                            sb.append(str);
                        }
                        res = sb.toString();
                    }else{
                        res = "Error :"+con.getResponseCode();
                    }

                } catch (IOException e) {
                    res = "-1";
                    e.printStackTrace();
                }
                return res;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s.equals("-1")) {
                    swipeRefreshTask.setRefreshing(false);
                    Toast.makeText(TaskDetStaff.this, "服务器未开启/网络连接失败", Toast.LENGTH_SHORT).show();
                    return;
                }else if(s.indexOf("Error")>0){
                    Toast.makeText(TaskDetStaff.this, s, Toast.LENGTH_SHORT).show();
                }
                    Gson gson = new Gson();
                    List<Inventory> RFID_list = gson.fromJson(s,new TypeToken<List<Inventory>>() {}.getType());
                    List<ItemBean> lis = new ArrayList<>();
                    ItemBean x ;
                    Inventory y;
                    for(int i = 0;i<RFID_list.size();i++){
                        x = new ItemBean();
                        y = RFID_list.get(i);
                        x.setItem_name(y.getItem_name());
                        x.setLocation(y.getItem_location());
                        x.setRFID_code(y.getRFID_code());
                    lis.add(x);
                }

                itemAdapter = new ItemAdapter(lis,currentTask.getTask_type());
                recyclerView.setAdapter(itemAdapter);

            }
        }


    private class Async_itemGet extends ATHelper_get {
        private String type;
        private String code;
        public Async_itemGet(String ip, String SessionID, String urlx,String type,String code) {
            super(ip, SessionID, urlx);
            this.type = type;
            this.code = code;
        }

        @Override
        protected String doInBackground(String... strings) {
            String url;
            url = "http://"+getIp()+":8080/DiJing/"+getUrlx()+"?task_type="+type+"&task_code="+code;
            HttpURLConnection con;
            InputStream is ;
            String res = null;
            try {
                con = (HttpURLConnection) new URL(url).openConnection();
                con.setRequestProperty("Cookie",getSessionID());
                con.setConnectTimeout(3000);
                if(con.getResponseCode()==200)
                {
                    is = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuffer sb = new StringBuffer();
                    String str ;

                    while ((str=reader.readLine())!=null){
                        sb.append(str);
                    }
                    res = sb.toString();
                }else{
                    res = "-1";
                }

            } catch (IOException e) {
                res = "-1";
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("-1"))
            {
                swipeRefreshTask.setRefreshing(false);
                Toast.makeText(TaskDetStaff.this, "服务器未开启/网络连接失败", Toast.LENGTH_SHORT).show();
                return;
            }
            Gson gson = new Gson();

            List<ItemBean> getItem = gson.fromJson(s,new TypeToken<List<ItemBean>>(){}.getType());
            countTokenNum = 0;
            for(int i = 0;i<getItem.size();i++)
            {
                if(currentTask.getTask_type().equals(TaskBean.TASKTYPE_IMPORT)){
                    String location = getItem.get(i).getLocation();
                    currentItem.get(i).setLocation(location);
                    if (StringHandler.compareTargetAndLocation(currentItem.get(i).getItem_target(),location)) {
                        countTokenNum ++;
                    }
                }//如果是入库任务，则需要额外更改位置信息
                else {
                    boolean finished = getItem.get(i).isItem_finished();
                    currentItem.get(i).setItem_finished(finished);
                    if (finished) {
                        countTokenNum++;
                    }
                }
            }
            itemAdapter.SetList(currentItem);
            initProgressBar(countRequredNum,countTokenNum);
            itemAdapter.notifyDataSetChanged();
            swipeRefreshTask.setRefreshing(false);
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
