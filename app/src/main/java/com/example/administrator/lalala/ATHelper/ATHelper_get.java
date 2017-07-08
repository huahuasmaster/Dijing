package com.example.administrator.lalala.ATHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Czq on 2017/4/22.
 */

public class ATHelper_get extends ATHelper_Post {
    private String urlx;
    private String sessionx;
    public ATHelper_get(String ip, String SessionID, String urlx) {
        super(ip, SessionID);
        sessionx = SessionID;
        this.urlx = urlx;
    }

    public String getUrlx() {
        return urlx;
    }

    public String getSessionx() {
        return sessionx;
    }

    @Override

    protected String doInBackground(String... strings) {
        String url;
        url = "http://"+getIp()+":8080/DiJing/"+urlx+"?from=1";
        HttpURLConnection con;
        InputStream is ;
        String res = null;
                try {
                    con = (HttpURLConnection) new URL(url).openConnection();
                    con.setRequestProperty("Cookie",sessionx);

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

}
