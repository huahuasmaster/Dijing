package com.example.administrator.lalala.activitys;

import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.EditText;

import com.example.administrator.lalala.R;
import com.example.administrator.lalala.assistant.SwipeBackHelper;

import org.w3c.dom.Text;

import entity.TaskBean;

public class Feedback extends AppCompatActivity {
    private Toolbar mtoolbar;
    private FloatingActionButton fab;
    private EditText project;
    private EditText main;
    private TaskBean currentTask;
    private boolean isFromTaskDet = false;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        mtoolbar = (Toolbar)findViewById(R.id.tasktoolbar);
        fab = (FloatingActionButton)findViewById(R.id.handin);
        project = (EditText)findViewById(R.id.project_feedback);
        main = (EditText)findViewById(R.id.main_feedback);
        sp = getPreferences(MODE_PRIVATE);
        editor = sp.edit();
        String content = "";
        String text = getIntent().getStringExtra("project");
        if(text!=null) //如果是从任务详情界面跳转过来，则将主题设置为任务名，且不可更改
        {
            isFromTaskDet = true;
            project.setText("关于"+text+"的任务反馈");
            project.setEnabled(false);
            currentTask = (TaskBean) getIntent().getSerializableExtra("currentTask");
            boolean isRemember = sp.getBoolean(currentTask.getTask_code()+"isRemember",false);
            if(isRemember) {
                content = sp.getString(currentTask.getTask_code()+"centent","");
            }
        }
        else {
            boolean isRemember = sp.getBoolean("isRemember",false);
            if(isRemember) {
                content = sp.getString("centent","");
                String pro = sp.getString("project","");
                project.setText(pro);
            }
        }

        main.setText(content);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });//返回按钮
        getSupportActionBar().setTitle(R.string.feedback);//标题
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(project.getText().toString())) {
                    Snackbar.make(v,"请填写主题！",Snackbar.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(main.getText().toString())) {
                    Snackbar.make(v,"请填写内容！",Snackbar.LENGTH_SHORT).show();
                }
                else {
                    Snackbar.make(v, "提交成功！", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFromTaskDet) {
            editor.putBoolean(currentTask.getTask_code()+"isRemember",true);
            editor.putString(currentTask.getTask_code()+"centent",main.getText().toString());
        }
        else {
            editor.putBoolean("isRemember",true);
            editor.putString("centent",main.getText().toString());
            editor.putString("project",project.getText().toString());
        }
        editor.apply();
    }
}
