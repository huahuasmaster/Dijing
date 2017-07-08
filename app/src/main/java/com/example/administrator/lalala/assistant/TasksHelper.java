package com.example.administrator.lalala.assistant;

import java.util.ArrayList;
import java.util.List;

import entity.ItemBean;
import entity.TaskBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public class TasksHelper {
    public static List<TaskBean> tasks = new ArrayList<>();//储存所有的任务
    public static List<ItemBean> goodsForImport = new ArrayList<>();
    public static List<ItemBean> goodsForImport2 = new ArrayList<>();
    public static List<ItemBean> goodsForExport = new ArrayList<>();
    public static List<TaskBean> tempTasks = new ArrayList<>();//储存未完成的任务
    public static List<TaskBean> tempTasksFin = new ArrayList<>();//储存已完成的任务
    public static int unreadCount = 0;
    public static int unFinishedCount = 0;
    public static void getTasksFromInternet() { //从网络获取信息
        tasks.clear();
        goodsForImport.clear();
        goodsForExport.clear();

        goodsForImport.add(new ItemBean("香肠","3721",false,"2号仓库#B区","2号仓库#B区#3柜#2层"));
        goodsForImport.add(new ItemBean("肉肠","37213",true,"2号仓库#A区","2号仓库#A区#4柜#1层"));
        goodsForImport.add(new ItemBean("火腿肠","3271",false,"1号仓库#B区","2号仓库#B区#4柜#2层"));
        goodsForImport.add(new ItemBean("腊肠","321389",false,"2号仓库#A区","不#在#柜#内"));

        goodsForImport2.add(new ItemBean("香肠","3721",false,"2号仓库#B区#1柜#1层","不#在#柜#内"));
        goodsForImport2.add(new ItemBean("肉肠","37213",true,"2号仓库#A区#4柜#2层","不#在#柜#内"));
        goodsForImport2.add(new ItemBean("火腿肠","3271",false,"2号仓库#B区#1柜#3层","不#在#柜#内"));
        goodsForImport2.add(new ItemBean("腊肠","321389",false,"2号仓库#A区#5柜#4层","不#在#柜#内"));

        goodsForExport.add(new ItemBean("香肠","3721",false,ItemBean.UNKONWN_TARGET,"2号仓库#B区#3柜#2层"));
        goodsForExport.add(new ItemBean("肉肠","37213",false,ItemBean.UNKONWN_TARGET,"2号仓库#A区#4柜#1层"));
        goodsForExport.add(new ItemBean("火腿肠","3271",false,ItemBean.UNKONWN_TARGET,"2号仓库#B区#4柜#2层"));
        goodsForExport.add(new ItemBean("腊肠","321389",false,ItemBean.UNKONWN_TARGET,"2号仓库#A区#5柜#4层"));

        tasks.add(new TaskBean("13dsaf2",TaskBean.TASKTYPE_EXPORT,"2017-4-27 6:26:07.0", goodsForExport,false,false,"10086"));//未被查看的任务
        tasks.add(new TaskBean("13dsaf1",TaskBean.TASKTYPE_IMPORT,"2017-4-27 6:10:07.0", goodsForImport,false,false,"10086"));
        tasks.add(new TaskBean("13dsaf1",TaskBean.TASKTYPE_IMPORT,"2017-4-26 5:10:46.0", goodsForImport2,false,false,"10086"));
        tasks.add(new TaskBean("13dsaf3",TaskBean.TASKTYPE_CHECK,"2017-4-26 1:23:52.0", goodsForImport,true,true,"10086"));//已被查看，已被员工完成，待审核的任务
        tasks.add(new TaskBean("13dsaf4",TaskBean.TASKTYPE_EXPORT,"2017-4-26 5:23:32.0", goodsForExport,true,true,"10086"));//已被最终审核通过的任务

    }
    public static void setTasks(List<TaskBean> mTaskBean)
    {
        tasks = mTaskBean;
    }


    public static void handlerTasks() { //对信息进行处理，统计
        //清空原来的信息
        tempTasks.clear();
        tempTasksFin.clear();
        unreadCount = 0;
        unFinishedCount = 0;
        if (tasks != null) {
            for (TaskBean task : tasks) {
                if (!task.isManager_task_finished())//如果是未完成的任务
                {
                    tempTasks.add(task);
                    unFinishedCount++;
                } else {//如果是已完成的任务
                    tempTasksFin.add(task);
                }
                //如果是一条未查看的任务
                if (!task.isWorker_task_checked()) {
                    unreadCount++;
                }

            }
        }
    }
    public static int getUnreadCount(){
        return unreadCount;
    }
    public static int getUnFinishedCount(){
        return unFinishedCount;
    }
    public static List<TaskBean> getTask(){
        return tempTasks;
    }
    public static List<TaskBean> getFinishedTask() {
        return tempTasksFin;
    }
}
