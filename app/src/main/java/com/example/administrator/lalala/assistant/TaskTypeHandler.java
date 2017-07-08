package com.example.administrator.lalala.assistant;

import entity.TaskBean;

/**
 * Created by Administrator on 2017/4/23.
 */

public class TaskTypeHandler {
    public static String handler(String taskType) {
        if(taskType.equals(TaskBean.TASKTYPE_CHECK)) {
            return "盘点";
        }
        else  if(taskType.equals(TaskBean.TASKTYPE_EXPORT)) {
            return "出库";
        }
        else if(taskType.equals(TaskBean.TASKTYPE_IMPORT)) {
            return "入库";
        }
        else return "未知任务";
    }
}
