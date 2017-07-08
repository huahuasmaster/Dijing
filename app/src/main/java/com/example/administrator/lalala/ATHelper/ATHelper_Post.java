package com.example.administrator.lalala.ATHelper;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Czq on 2017/4/22.
 */

public class ATHelper_Post  extends AsyncTask<String,Void,String> {

    private String ip;
    private String SessionID = "-1";
    private String servurl;
    private String staff_id;
    private String staff_pw;
    private String company_code;
    public ATHelper_Post(String ip,String SessionID )
    {
        this.ip=ip;
        this.SessionID = SessionID;
    }
    public ATHelper_Post(String ip,String servurl,String staff_id,String staff_pw,String company_code)
    {
        this.ip = ip;
        this.servurl = servurl;
        this.staff_id = staff_id;
        this.staff_pw = staff_pw;
        this.company_code = company_code;
    }


    @Override
    protected String doInBackground(String... strings) {
        String url ;
        url = "http://"+ip+":8080/DiJing/"+servurl+"?staff_id="+staff_id+"&staff_pw="+staff_pw+
              "&company_code="+company_code+"&from=1";

        HttpURLConnection con;
        InputStream is ;
        String res = null;
        try {
            con = (HttpURLConnection) new URL(url).openConnection();
//            if(getSessionID() != null) {
//                con.setRequestProperty("cookie", getSessionID());
//            }
            con.setDoOutput(true);
            con.setRequestMethod("POST");

            con.setConnectTimeout(3000);

            String cookieval = con.getHeaderField("set-cookie");
            if(cookieval != null) {
                res  = cookieval.substring(0, cookieval.indexOf(";"));
                res += "#";
            }else{
                res = "FFFFFFFF#";
            }
            if(con.getResponseCode()==200)
            {
                is = con.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuffer sb = new StringBuffer();
                String str ;

                while ((str=reader.readLine())!=null){
                    sb.append(str);
                }
                res += sb.toString();
            }


//            res = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            res += "-1";
        }
        return res;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setSessionID(String sessionID) {
        SessionID = sessionID;
    }

    public String getIp() {
        return ip;
    }

    public String getSessionID() {
        return SessionID;
    }
}
