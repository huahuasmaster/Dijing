package com.example.administrator.lalala.assistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.lalala.R;
import com.flyco.tablayout.utils.UnreadMsgUtils;
import com.flyco.tablayout.widget.MsgView;

import java.util.List;

import entity.TaskBean;

/**
 * Created by Administrator on 2017/3/18.
 */

public class TaskAdapter extends BaseAdapter {
    private List<TaskBean> listTaskBean = null;
    private TaskBean mTaskBean = new TaskBean();
    private Context context = null;
    private boolean showPoint = true;
    private LayoutInflater layoutInflater = null;

    public TaskAdapter(List<TaskBean> listMsgBean, Context context,boolean showPoint) {
    this.listTaskBean = listMsgBean;
        this.context = context;
        this.showPoint = showPoint;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setList(List<TaskBean> x)
    {
        listTaskBean = x;
    }

    @Override
    public int getCount() {
        return listTaskBean.size();
    }

    @Override
    public Object getItem(int position) {
        return listTaskBean.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = layoutInflater.inflate(R.layout.task_item, null);
        mTaskBean = listTaskBean.get(position);
        boolean isChecked = mTaskBean.isWorker_task_checked();
        TextView taskStartTime = (TextView)v.findViewById(R.id.startTime_task);
        TextView taskTitle = (TextView)v.findViewById(R.id.tv_name);
        taskTitle.setText(StringHandler.handlerTaskType(listTaskBean.get(position).getTask_type()));
        taskStartTime.setText(
                StringHandler.handlerTime(
                listTaskBean.get(position).getTask_date()));
        ImageView point = (ImageView)v.findViewById(R.id.point);
        if(!isChecked && showPoint) {
            point.setVisibility(View.VISIBLE);
        }

        return v;
    }
}
