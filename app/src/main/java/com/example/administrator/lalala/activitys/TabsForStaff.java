package com.example.administrator.lalala.activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.administrator.lalala.ATHelper.ATHelper_get;
import com.example.administrator.lalala.R;
import com.example.administrator.lalala.activiytsHelper.BaseActivity;
import com.example.administrator.lalala.assistant.TaskAdapter;
import com.example.administrator.lalala.assistant.TasksHelper;
import com.example.administrator.lalala.service.TestService;
import com.example.administrator.lalala.widgets.ImageWithPoints.ImageLayout;
import com.example.administrator.lalala.widgets.ImageWithPoints.ImgSimple;
import com.example.administrator.lalala.assistant.ImgSimpleHelper;
import com.example.administrator.lalala.widgets.nicespinner.NiceSpinner;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import entity.Announcement;
import entity.ItemBean;
import entity.TaskBean;
/**
关于SlidingTabLayout的初始化
 ——需先添加适配器，添加标题
 ——需用String[]添加标题
 ——使用小红点时，必须先定义显示的数量，再更改偏移量，否则无效
 */
public class TabsForStaff extends BaseActivity implements View.OnClickListener{
//    private TabLayout mTabLayout;
    private int DEFOULT_TAB = 0;
    private boolean isFirstRun = true;
    private SlidingTabLayout mTabLayout;
    private ViewPager mViewPager;
    private Toolbar tbAuthor;
    private ImageView icon;
    private LayoutInflater mInflater;
    private SharedPreferences sp;

    private List<String> mTitleList = new ArrayList<String>();//页卡标题集合
    private View view1, view2, view3, view4;//页卡视图
    private List<View> mViewList = new ArrayList<>();//页卡视图集合
    //以下是对任务视图的初始化
    private ListView taskList;
    private ListView finishedList;
    private SwipeRefreshLayout swipeRefreshTask;
    public static List<TaskBean> mTaskBean = new ArrayList<>();
    public static List<TaskBean> mFinishedTaskBean = new ArrayList<>();
    public static TaskAdapter mTaskAdapter;
    public static TaskAdapter mFinishedTaskAdapter;
    //以下是对首页视图的初始化
    private SwipeRefreshLayout swipeRefreshHomePage;
    private TextView noticeTitle;
    private TextView noticeContent;
    private TextView text_unreadTask;
    private TextView text_unFinishedTask;
    private CardView noticeCard;
    private CardView taskCard;
    //以下是对地图视图的初始化
    private ImageLayout mImageLayout;
    private NiceSpinner niceSpinner;
    private boolean intentFromTaskDet = false;
    private ImgSimple mapImg;
    //以下是对“我的“页面初始化;
    private CardView setting;
    private CardView feedback;
    private CardView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);
        sp = getPreferences(MODE_PRIVATE);
        Intent intent = new Intent(this, TestService.class);
        startService(intent);

        TasksHelper.handlerTasks();//初始化，获取消息列表，并处理
        mTaskBean = TasksHelper.getTask();
        mFinishedTaskBean = TasksHelper.getFinishedTask();

        int nthTab  = getIntent().getIntExtra("nthTab",DEFOULT_TAB);//获取上一个activity传输的 选中第几个tab
        tbAuthor = (Toolbar) findViewById(R.id.maintb);
        setSupportActionBar(tbAuthor);


        mViewPager = (ViewPager) findViewById(R.id.vp_view);
        mTabLayout = (SlidingTabLayout)findViewById(R.id.tabs);
//        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mInflater = LayoutInflater.from(this);

        view1 = mInflater.inflate(R.layout.home_page, null);
        view2 = mInflater.inflate(R.layout.map, null);
        view3 = mInflater.inflate(R.layout.task, null);
        view4 = mInflater.inflate(R.layout.mine, null);
        //添加页卡视图
        mViewList.add(view1);
        mViewList.add(view2);
        mViewList.add(view3);
        mViewList.add(view4);
        //添加页卡标题
        mTitleList.add("首页");
        mTitleList.add("地图");
        mTitleList.add("任务");
        mTitleList.add("我的");


        MyPagerAdapter mAdapter = new MyPagerAdapter(mViewList);
        //给ViewPager设置适配器
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(nthTab);

        String[] titles = new String[] {"首页","地图","任务","我的"};
        //将TabLayout和ViewPager关联起来
        mTabLayout.setViewPager(mViewPager,titles);
        //设置选择的tab
        mTabLayout.setCurrentTab(nthTab);

        //以下是对各个视图的初始化
        initTask();
        initMap();
        initHomePage();
        initMine();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mTaskAdapter.notifyDataSetChanged();
        mFinishedTaskAdapter.notifyDataSetChanged();
//        swipeRefreshTask.setRefreshing(true);
//        new Asc_GetTask(Login.Usip,Login.sessionID,"tasksForAndroid").execute();
    }

    private void initHomePage() {
        noticeTitle = (TextView)view1.findViewById(R.id.noticeTitle);
        noticeContent = (TextView)view1.findViewById(R.id.noticeContent);
        text_unFinishedTask = (TextView)view1.findViewById(R.id.unfinishedTask);
        text_unreadTask = (TextView)view1.findViewById(R.id.unreadTask);
        noticeCard = (CardView)view1.findViewById(R.id.noticeCard);
        taskCard = (CardView)view1.findViewById(R.id.taskCard);
        boolean noticeRem = sp.getBoolean("noticeRem",false);

        if(noticeRem) {
            noticeTitle.setText(sp.getString("noticeTitle","暂无通知"));
            noticeContent.setText(sp.getString("noticeContent",""));
        }
        noticeCard.setOnClickListener(this);

        initUnreadAndSoOn();
        //以下是对下拉刷新的初始化及刷新处理
        swipeRefreshHomePage = (SwipeRefreshLayout)view1.findViewById(R.id.swipe_homepage);
        swipeRefreshHomePage.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshHomePage.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new ATHelper_getAnn(Login.Usip,Login.sessionID,"checkAnn").execute();
            }
        });
    }
    private void initTask() {
        mTaskAdapter = new TaskAdapter(mTaskBean,this,false);
        mFinishedTaskAdapter = new TaskAdapter(mFinishedTaskBean,this,false);
        taskList = (ListView)view3.findViewById(R.id.listView_task);
        taskList.setAdapter(mTaskAdapter);
        mTaskAdapter.notifyDataSetChanged();
        //以下是对未完成任务列表子项的监听
        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTaskBean.get(position).setWorker_task_checked(true);//将被点击的子项设为已读
                TasksHelper.handlerTasks();//重新处理一遍，刷新未读消息数量
                initUnreadAndSoOn();
                Intent intent = new Intent();
                intent.setClass(TabsForStaff.this, TaskDetStaff.class);
                TaskBean currentTask = mTaskBean.get(position);
                new Asc_setTChe(Login.Usip,Login.sessionID,"workerCheck",currentTask.getTask_type(),currentTask.getTask_code()).execute();
                intent.putExtra("currentTask_data",currentTask);//将当前的task直接作为对象传递入，供任务详情直接显示
                startActivity(intent);
            }
        });
        finishedList = (ListView)view3.findViewById(R.id.listView_finishedTask);
        finishedList.setAdapter(mFinishedTaskAdapter);
        mFinishedTaskAdapter.notifyDataSetChanged();
        //以下是对已完成任务列表子项的监听
        finishedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(TabsForStaff.this, TaskDetStaff.class);
                TaskBean currentTask = mFinishedTaskBean.get(position);
                new Asc_setTChe(Login.Usip,Login.sessionID,"workerCheck",currentTask.getTask_type(),currentTask.getTask_code()).execute();

                intent.putExtra("currentTask_data",currentTask);
                startActivity(intent);
            }
        });

        swipeRefreshTask = (SwipeRefreshLayout)view3.findViewById(R.id.swipe_task);
        swipeRefreshTask.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshTask.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Asc_GetTask(Login.Usip,Login.sessionID,"tasksForAndroid").execute();
            }
        });
    }

    private void initUnreadAndSoOn() { //遍历待完成任务列表，计算其中未查看的量，用小圆点标识
//        Toast.makeText(getApplicationContext(),"unread",Toast.LENGTH_SHORT).show();
        int count = TasksHelper.unreadCount;
        int unFinishedCount = TasksHelper.unFinishedCount;
        //对主页的任务提醒进行更改
        text_unFinishedTask.setText("您有"+unFinishedCount+"项未完成任务");
        text_unreadTask.setText("您有"+count+"项未查看任务");
        //对小红点进行更改
        if(count > 1) { //未读数大于1，显示数字
            mTabLayout.showMsg(2,count);
        }
        else if(count == 1) {//未读数等于1，显示小红点
            mTabLayout.showDot(2);
        }
        else {
            mTabLayout.hideMsg(2);//未读数为零，不显示
        }
        mTabLayout.setMsgMargin(2,25,6);

        //对下拉框的修改
        if (mTaskBean.size() == 0) {
            List<String> noTask = new ArrayList<>();
            noTask.add("暂无待完成任务");
            niceSpinner.attachDataSource(noTask);
            mImageLayout.removeAll();
        }else {

            intentFromTaskDet = getIntent().getBooleanExtra("intentFromTaskDet",false);
            if (!intentFromTaskDet) {
                niceSpinner.attachDataSource(mTaskBean);
                showPointsOnMap(mTaskBean.get(0));
            }
        }
    }
    private void initMap() {
        niceSpinner = (NiceSpinner) view2.findViewById(R.id.nicespinner);
        mImageLayout = (ImageLayout)view2.findViewById(R.id.layoutContent);
        niceSpinner.setTextColor(getResources().getColor(R.color.unclickableText));
        intentFromTaskDet = getIntent().getBooleanExtra("intentFromTaskDet",false);//判断是否为任务详情跳转来的
        if (mTaskBean.size() == 0) {
            List<String> noTask = new ArrayList<>();
            noTask.add("暂无待完成任务");
            niceSpinner.attachDataSource(noTask);
        }else {
            niceSpinner.attachDataSource(mTaskBean);}
        //超出需要显示的任务 在列表中的位置 并将其选中
        if(intentFromTaskDet) {
            TaskBean taskShowOnMap = (TaskBean)getIntent().getExtras().getSerializable("taskShowOnMap");
            //以下是对下拉列表的更改
            int position = -1;
            for(TaskBean taskBean :mTaskBean) {
                if (taskBean.getTask_code().equals(taskShowOnMap.getTask_code())) {
                    position = mTaskBean.indexOf(taskBean);
                }
            }
            if(position != -1) {//如果找到了对应的列表项位置
                niceSpinner.setSelectedIndex(position);//将列表选择为对应的项
            }

        }
        niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(mTaskBean.size()!=0) {
                    showPointsOnMap(mTaskBean.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //以下是对地图的更改
        if(intentFromTaskDet) {
            TaskBean taskShowOnMap = (TaskBean)getIntent().getExtras().getSerializable("taskShowOnMap");
            showPointsOnMap(taskShowOnMap);
        }
        else if(mTaskBean.size() > 0){ //如果不是从任务详情界面跳转过来，且任务列表不为空,则默认显示第一个任务的地图
            showPointsOnMap(mTaskBean.get(0));
        }
    }
    private  void showPointsOnMap(TaskBean taskShowOnMap) {
        List<ItemBean> itemShowOnMap = taskShowOnMap.getItems(); // 获取其中的货物信息
        mapImg = ImgSimpleHelper.getImgSimple(itemShowOnMap,taskShowOnMap.getTask_type());//根据货物信息以及任务类型，最终得到地图以及小点
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = (int)(width*mapImg.getScale());
        mImageLayout.setPoints(mapImg.getPointSimples());
        mImageLayout.setImgBg(width,height,mapImg.getResourceID());
    }
    private void initMine() {
        setting = (CardView) view4.findViewById(R.id.card_setting);
        setting.setOnClickListener(this);
        feedback = (CardView)view4.findViewById(R.id.card_feedBack);
        feedback.setOnClickListener(this);
        logout = (CardView)view4.findViewById(R.id.card_quit);
        logout.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        final Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.card_setting:
                intent.setClass(TabsForStaff.this, Setting.class);
                startActivity(intent);
                break;
            case R.id.card_feedBack:
                intent.setClass(TabsForStaff.this, Feedback.class);
                startActivity(intent);
                break;
            case R.id.noticeCard:
                intent.setClass(TabsForStaff.this,Notice.class);
                intent.putExtra("noticeTitle",noticeTitle.getText().toString());
                intent.putExtra("noticeContent",noticeContent.getText().toString());
                startActivity(intent);
                break;
            case R.id.card_quit:
                new  MaterialDialog.Builder(this)
                        .title("退出")
                        .content("是否退出登录")
                        .positiveText("继续")
                        .negativeText("取消")
                        .onAny(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                if(which == DialogAction.POSITIVE) {
                                    //关闭服务
                                    Intent stopIntent = new Intent();
                                    stopIntent.setClass(TabsForStaff.this,TestService.class);
                                    stopService(stopIntent);
                                    //转向登录页面
                                    Intent intent = new Intent();
                                    intent.setClass(TabsForStaff.this,Login.class);
                                    startActivity(intent);
                                }
                            }
                        })
                        .show();
            default:break;
        }

    }

    private class Asc_GetTask extends ATHelper_get{

        public Asc_GetTask(String ip, String SessionID, String urlx) {
            super(ip, SessionID, urlx);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("-1"))
            {
                swipeRefreshTask.setRefreshing(false);
                Toast.makeText(TabsForStaff.this, "服务器未开启/网络连接失败", Toast.LENGTH_SHORT).show();
                return;
            }
            Gson gson = new Gson();
            mTaskBean = gson.fromJson(s,new TypeToken<List<TaskBean>>(){}.getType());
            TasksHelper.setTasks(mTaskBean);
            TasksHelper.handlerTasks();
            mTaskBean = TasksHelper.getTask();
            mFinishedTaskBean = TasksHelper.getFinishedTask();
            mTaskAdapter.setList(TasksHelper.getTask());
            mFinishedTaskAdapter.setList(TasksHelper.getFinishedTask());
            mTaskAdapter.notifyDataSetChanged();
            mFinishedTaskAdapter.notifyDataSetChanged();
            initUnreadAndSoOn();
            swipeRefreshTask.setRefreshing(false);
        }
    }
    private class Asc_setTChe extends ATHelper_get{
        private String type;
        private String code;
        public Asc_setTChe(String ip, String SessionID, String urlx,String type,String code) {
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
//                    con.setConnectTimeout(5000);
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
                }

            } catch (IOException e) {
                e.printStackTrace();
                res = "-1";
            }
            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("-1"))
            {
                Toast.makeText(TabsForStaff.this,"服务器设置失败",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ATHelper_getAnn extends ATHelper_get {

        public ATHelper_getAnn(String ip, String SessionID, String urlx) {
            super(ip, SessionID, urlx);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("-1"))
            {
                Toast.makeText(TabsForStaff.this, "服务器未开启/网络连接失败", Toast.LENGTH_SHORT).show();
                swipeRefreshHomePage.setRefreshing(false);
                return;
            }
            Gson gson = new Gson();
            Announcement ann = gson.fromJson(s,Announcement.class);
            noticeTitle.setText(ann.getTitle());
            noticeContent.setText(ann.getContent());
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("noticeTitle",ann.getTitle());
            editor.putString("noticeContent",ann.getContent());
            editor.putBoolean("noticeRem",true);
            editor.apply();
            swipeRefreshHomePage.setRefreshing(false);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //如果用户点击了返回键，则执行返回桌面功能 而不是退出程序
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i= new Intent(Intent.ACTION_MAIN);

            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            i.addCategory(Intent.CATEGORY_HOME);

            startActivity(i);
        }
        return false;
    }

    //ViewPager适配器
    class MyPagerAdapter extends PagerAdapter {
        private List<View> mViewList;

        public MyPagerAdapter(List<View> mViewList) {
            this.mViewList = mViewList;
        }

        @Override
        public int getCount() {
            return mViewList.size();//页卡数
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;//官方推荐写法
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));//添加页卡
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));//删除页卡
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return mTitleList.get(position);//页卡标题
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
//        getMenuInflater().inflate(R.menu.menu_search,menu);
        return  true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_first:
                Toast.makeText(this,"first",Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_senond:
                Toast.makeText(this,"second",Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
}}

