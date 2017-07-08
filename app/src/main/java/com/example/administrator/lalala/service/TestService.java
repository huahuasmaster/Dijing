package com.example.administrator.lalala.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import com.example.administrator.lalala.R;
import com.example.administrator.lalala.activitys.Login;
import com.example.administrator.lalala.activitys.TabsForStaff;
import com.example.administrator.lalala.activitys.TaskDetStaff;
import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

import entity.TaskBean;

/**
 * Created by Czq on 2017/4/30.
 */

public class TestService extends Service {
    private Intent intent;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        new Client().start();
    }
    private void notification(String content) {
        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        Intent intent = new Intent(this, TestService.class);
        setIntent();
//        Gson gson = new Gson();
//        TaskBean taskBean = gson.fromJson(content, TaskBean.class);
//        intent.putExtra("currentTask_data",taskBean);
        Notification.Builder builder = new Notification.Builder(this);
        PendingIntent contentIntent = PendingIntent.getActivities(this, 0,
                new Intent[]{intent}, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.logo)//设置状态栏里面的图标（小图标）
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.logo))
                .setWhen(System.currentTimeMillis())        //设置时间发生时间
                .setAutoCancel(true)                        //设置可以清除
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentTitle("您有新的消息")    //设置下拉列表里的标题
                .setContentText(content)    //设置上下文内容
                .setPriority(Notification.PRIORITY_MAX)
        ;

        Notification notification = builder.build();
        manager.notify(1, notification);
    }
    private void setIntent() {
        intent = new Intent(this,TabsForStaff.class);
        intent.putExtra("nthTab",2);
    }
    private class Client extends Thread {
        private Client mClient;
        private String address = "ws://"+Login.Usip+":8080/DiJing/socket/"+ Login.UsrId;

        public void run(){
            try {
                ExampleClient c = new ExampleClient( new URI(address), new Draft_17() );
                c.connectBlocking();

            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class ExampleClient extends WebSocketClient {

        public ExampleClient(URI serverUri , Draft draft ) {
            super( serverUri, draft );
        }

        public ExampleClient( URI serverURI ) {
            super( serverURI );
        }

        @Override
        public void onOpen( ServerHandshake handshakedata ) {
            System.out.println( "opened connection" );
        }

        @Override
        public void onMessage( String message ) {
//            System.out.println( "received: " + message );
            notification(message);
        }

        @Override
        public void onFragment( Framedata fragment ) {
            System.out.println( "received fragment: " + new String( fragment.getPayloadData().array() ) );
        }

        @Override
        public void onClose(int code, String reason, boolean remote ) {
            // The codecodes are documented in class org.java_websocket.framing.CloseFrame
            System.out.println( "Connection closed by " + ( remote ? "remote peer" : "us" ) );
        }

        @Override
        public void onError( Exception ex ) {
            ex.printStackTrace();
        }
    }


}
