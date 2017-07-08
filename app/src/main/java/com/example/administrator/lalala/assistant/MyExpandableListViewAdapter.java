package com.example.administrator.lalala.assistant;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.lalala.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.ItemBean;
import entity.TaskBean;

/**
 * Created by Administrator on 2017/4/22.
 */

//public class MyExpandableListViewAdapter implements ExpandableListAdapter {
//    private Map<ItemBean,List<ItemDetailBean>> goodsWithDet = new HashMap<>();
//    private List<ItemBean> itemBeanList;
//    private String taskType;
//    private Context context;
//    private LayoutInflater layoutInflater;
//    public MyExpandableListViewAdapter(List<ItemBean> itemBeanList, Map<ItemBean,List<ItemDetailBean>> goodsWithDet, String taskType,Context context) {
//        this.itemBeanList = itemBeanList;
//        this.goodsWithDet = goodsWithDet;
//        this.context =context;
//        this.taskType = taskType;
//        this.layoutInflater = LayoutInflater.from(context);
//    }
//    @Override
//    public void registerDataSetObserver(DataSetObserver observer) {
//
//    }
//
//    @Override
//    public void unregisterDataSetObserver(DataSetObserver observer) {
//
//    }
////获得父项的数量
//    @Override
//    public int getGroupCount() {
//        return goodsWithDet.size();
//    }
////获取groupPosition父项上的子项数量
//    @Override
//    public int getChildrenCount(int groupPosition) {
//        return goodsWithDet.get(itemBeanList.get(groupPosition)).size();
//    }
////获取某个父项
//    @Override
//    public Object getGroup(int groupPosition) {
//        return goodsWithDet.get(itemBeanList.get(groupPosition));
//    }
////获取某个父项的子项
//    @Override
//    public Object getChild(int groupPosition, int childPosition) {
//        return goodsWithDet.get(itemBeanList.get(groupPosition)).get(childPosition);
//    }
//
//    @Override
//    public long getGroupId(int groupPosition) {
//        return groupPosition;
//    }
//
//    @Override
//    public long getChildId(int groupPosition, int childPosition) {
//        return childPosition;
//    }
//
//    @Override
//    public boolean hasStableIds() {
//        return false;
//    }
//
//    @Override
//    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
//        if(convertView == null) {
//            convertView = layoutInflater.inflate(R.layout.goods_list_item,null);
//        }
//        //获取对应的taskbean
//        ItemBean mItemBean = itemBeanList.get(groupPosition);
//        TextView name_goods = (TextView)convertView.findViewById(R.id.name_goods);
//        TextView identifier_goods = (TextView)convertView.findViewById(R.id.identifier_goods);
//        ImageView check = (ImageView)convertView.findViewById(R.id.finished_goods);
//        TextView number_goods = (TextView)convertView.findViewById(R.id.number_goods);
//
//        String identifier  = mItemBean.getItem_code();
//        int requiredNum = mItemBean.getItem_quantity();
//        int tokenNum = mItemBean.getItem_finishied_quantity();
//        check.setVisibility(View.INVISIBLE);
//        identifier_goods.setText("("+identifier+")");//设置编号
//        name_goods.setText(mItemBean.getItem_name());//设置货物名称
//        number_goods.setText(""+tokenNum+"/"+requiredNum);//设置需要数量与已拿取数量
//        if(requiredNum == tokenNum)//如果全部取完，则显示打勾图标
//        {
//            check.setVisibility(View.VISIBLE);
//
//        }
//        return convertView;
//    }
//
//    @Override
//    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//        if(convertView == null) {
//            convertView = layoutInflater.inflate(R.layout.goods_det_list_item,null);
//        }
//        //获取对应的GoodsDetBean
//        ItemDetailBean currentDet = goodsWithDet.get(itemBeanList.get(groupPosition)).get(childPosition);
//        //对layout中的各个view初始化
//        TextView location = (TextView)convertView.findViewById(R.id.location_goods);
//        TextView count = (TextView)convertView.findViewById(R.id.count_goods);
//        //对各个view进行赋值
//        location.setText(StringHandler.handlerLocationtoString(currentDet.getItem_location_code()));
//        count.setText(""+currentDet.getItem_quantity());
//        if(taskType.equals(TaskBean.TASKTYPE_IMPORT))//如果是入库任务，则只显示位置
//        {
//            count.setVisibility(View.INVISIBLE);
//        }
//        return convertView;
//    }
//
//    @Override
//    public boolean isChildSelectable(int groupPosition, int childPosition) {
//        return false;
//    }
//
//    @Override
//    public boolean areAllItemsEnabled() {
//        return false;
//    }
//
//    @Override
//    public boolean isEmpty() {
//        return false;
//    }
//
//    @Override
//    public void onGroupExpanded(int groupPosition) {
//
//    }
//
//    @Override
//    public void onGroupCollapsed(int groupPosition) {
//
//    }
//
//    @Override
//    public long getCombinedChildId(long groupId, long childId) {
//        return 0;
//    }
//
//    @Override
//    public long getCombinedGroupId(long groupId) {
//        return 0;
//    }
//}
