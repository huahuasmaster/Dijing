package com.example.administrator.lalala.activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.administrator.lalala.ATHelper.ATHelper_Post;
import com.example.administrator.lalala.R;
import com.example.administrator.lalala.activiytsHelper.BaseActivity;
import com.example.administrator.lalala.assistant.TasksHelper;
import com.example.administrator.lalala.dataBase.Coordinate;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class Login extends BaseActivity {
    public static String Usip;
    public static String sessionID;
    public static  String UsrId;
    private Button login_btn;
    private ImageView logo;
    private EditText account;
    private EditText password;
    private static SharedPreferences sp;
    private TextInputLayout account_layout;
    private TextInputLayout password_layout;
    private MaterialDialog dialog;
    private boolean isHidePwd = true;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg)
        {
            if(msg.what==0x11)
            {
                password_layout.setVisibility(View.VISIBLE);
                account_layout.setVisibility(View.VISIBLE);
                login_btn.setVisibility(View.VISIBLE);
                Animation animation = AnimationUtils.loadAnimation(Login.this,R.anim.translate_editext);
                LayoutAnimationController lac = new LayoutAnimationController(animation);

                password_layout.setAnimation(animation);
                account_layout.setAnimation(animation);
                login_btn.setAnimation(animation);
                account.requestFocus();

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TasksHelper.getTasksFromInternet();
        judgeIsFirstRun();
        login_btn = (Button) findViewById(R.id.btnLogin);
        logo = (ImageView) findViewById(R.id.logo);
        account = (EditText) findViewById(R.id.edit_text_email);
        password = (EditText)findViewById(R.id.edit_text_password);
        account_layout = (TextInputLayout)findViewById(R.id.textInputEmail);
        password_layout = (TextInputLayout)findViewById(R.id.textInputLayout2);
        account_layout.setVisibility(View.INVISIBLE);
        password_layout.setVisibility(View.INVISIBLE);
        login_btn.setVisibility(View.INVISIBLE);
        logo.requestFocus();

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText userName = (EditText) findViewById(R.id.edit_text_email);
                EditText passWord = (EditText) findViewById(R.id.edit_text_password);
//
                dialog = new MaterialDialog.Builder(Login.this)
                        .title("登录中")
                        .content("请稍等...")
                        .progress(true, 0)
                        .progressIndeterminateStyle(true)
                        .show();
                UsrId = userName.getText().toString();
                new AscLogin(Usip,"login", UsrId,passWord.getText().toString(),"20001").execute();
            }

        });
        login_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Login.this,TabsForAdm.class);
                startActivity(intent);
                return false;
            }
        });
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Login.this,ChangeIP.class);
                startActivity(intent);
            }
        });

        sp = this.getPreferences(MODE_PRIVATE);
        Usip = sp.getString("ip","192.168.0.100");
        beginAnim();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Snackbar.make(login_btn,"当前IP为"+Usip,Snackbar.LENGTH_SHORT).show();
    }

    public static void changeIP(String newIP) {
        Usip = newIP;
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("ip",newIP);
        editor.apply();
    }
    private void beginAnim() {
        new Thread()
        {
            public void run() {
                // TODO Auto-generated method stub
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0x11);

            }
        }.start();
    }
    private void judgeIsFirstRun() {
        SharedPreferences sp = this.getPreferences(MODE_PRIVATE);

        boolean isFirstRun = sp.getBoolean("isFirstRun13", true);
        SharedPreferences.Editor editor = sp.edit();
        if (isFirstRun) {
            LitePal.getDatabase();
            Toast.makeText(this, "第一次启动，更新数据中...", Toast.LENGTH_SHORT).show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Coordinate coor1 = new Coordinate(2000, "C", "1", 20, 59);
                    List<Coordinate> coors = new ArrayList<Coordinate>();
                    coors.add(new Coordinate(2000, "A区", "1柜", 0.20f, 0.27f));
                    coors.add(new Coordinate(2000, "A区", "2柜", 0.20f, 0.62f));
                    coors.add(new Coordinate(2000, "A区", "3柜", 0.20f, 0.68f));
                    coors.add(new Coordinate(2000, "A区", "4柜", 0.20f, 0.74f));
                    coors.add(new Coordinate(2000, "A区", "5柜", 0.20f, 0.80f));
                    coors.add(new Coordinate(2000, "B区", "1柜", 0.20f, 0.03f));
                    coors.add(new Coordinate(2000, "B区", "2柜", 0.20f, 0.09F));
                    coors.add(new Coordinate(2000, "B区", "3柜", 0.20f, 0.12f));
                    coors.add(new Coordinate(2000, "B区", "4柜", 0.20f, 0.18f));
                    coors.add(new Coordinate(2000, "B区", "5柜", 0.20f, 0.24f));
                    coors.add(new Coordinate(2000, "B区", "6柜", 0.20f, 0.3f));
                    coors.add(new Coordinate(2000, "C区", "1柜", 0.20f, 0.90f));
                    for (Coordinate mcoor : coors) {
                        mcoor.save();
                    }
                }
            }).start();
            editor.putBoolean("isFirstRun13", false);
        }
        editor.apply();
    }

    class AscLogin extends ATHelper_Post {


        public AscLogin(String ip, String staff_id, String servurl, String staff_pw, String company_code) {
            super(ip, staff_id, servurl, staff_pw, company_code);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            Toast.makeText(Login.this, s, Toast.LENGTH_SHORT).show();
            String[] x = s.split("#");
            sessionID = x[0];
            if(x[1].equals("-1"))
            {
                Toast.makeText(Login.this, "服务器未开启/网络连接失败", Toast.LENGTH_SHORT).show();
            }else if(x[1].equals("-2")){
                Toast.makeText(Login.this, "帐号/密码错误，请检测", Toast.LENGTH_SHORT).show();
            }else if(x[1].equals("1")){
                Toast.makeText(Login.this, "你好，工人", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.setClass(Login.this, TabsForStaff.class);
                startActivity(intent);

            }else if(x[1].equals("2")){
                Toast.makeText(Login.this, "你好，管理员", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(Login.this, TabsForAdm.class);
                startActivity(intent);

            }
            dialog.dismiss();
            return;
            

            
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
}
