package com.example.administrator.lalala.assistant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;

import entity.ItemBean;
import entity.TaskBean;

/**
 * Created by Administrator on 2017/4/23.
 */

public class StringHandler {
    public static String handlerTaskType(String taskType) {
        if(taskType.equals(TaskBean.TASKTYPE_CHECK) || taskType.equals("盘点")) {
            return "盘点";
        }
        else  if(taskType.equals(TaskBean.TASKTYPE_EXPORT) || taskType.equals("出库")) {
            return "出库";
        }
        else if(taskType.equals(TaskBean.TASKTYPE_IMPORT)|| taskType.equals("入库")) {
            return "入库";
        }
        else return "未知任务";
    }
    public static String handlerLocationtoString(String location) { //20号仓库#A区#20柜#3层
        if (location != null) {
            String[] loc = location.split("#");
            StringBuffer sb = new StringBuffer();
            for (String l : loc) {
                sb.append(l);
            }
            return sb.toString();
        }
        else return "";
    }
    public static String[] handlerLocation(String location) {
        if(location != null) {
            String[] loc = location.split("#");
            return loc;
        }
        else {
            return null;
        }
    }
    public static boolean compareTargetAndLocation(String target,String location) {
        if (handlerLocationtoString(location)
                .contains(handlerLocationtoString(target))) {//如果当前位置的字符串中包含目标任务 则表示放入了目标包含的区域中
            return true;
        }
        else return false;
    }
    public static String handlerTime(String time){ //2017-04-17 22:22:22.0

        String time1 = time.split("\\.")[0];
        Calendar calendar = Calendar.getInstance();
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(time1);
            calendar.setTime(date);
            int month = calendar.get(Calendar.MONTH) + 1; //获取月份
            int day = calendar.get(Calendar.DAY_OF_MONTH);//获取月中的第几天
            int hour = calendar.get(Calendar.HOUR_OF_DAY);//获取小时
            int minute = calendar.get(Calendar.MINUTE);
            String finalTime = ""+month+"月"+day+"日 "+(hour < 10 ? "0":"")+hour+":"+(minute < 10 ? "0":"")+minute;
            return finalTime;
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
//        return sb.toString();
        return time;
    }

}
