package com.example.administrator.lalala.assistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.lalala.R;

import java.util.List;

import entity.ItemBean;

/**
 * Created by Administrator on 2017/4/21.
 */

//public class GoodsAdapter extends BaseAdapter{
//    private List<ItemBean> listItemBean = null;
//    private Context context = null;
//    private LayoutInflater layoutInflater = null;
//
//    public GoodsAdapter(List<ItemBean> listItemBean, Context context) {
//        this.listItemBean = listItemBean;
//        this.context = context;
//        this.layoutInflater = LayoutInflater.from(context);
//    }
//
//    @Override
//    public int getCount() {
//        return listItemBean.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return listItemBean.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View v = layoutInflater.inflate(R.layout.goods_list_item,null);
//
//        ItemBean mItemBean = listItemBean.get(position);
//        TextView name_goods = (TextView)v.findViewById(R.id.name_goods);
//        TextView identifier_goods = (TextView)v.findViewById(R.id.identifier_goods);
//        ImageView check = (ImageView)v.findViewById(R.id.finished_goods);
//        TextView number_goods = (TextView)v.findViewById(R.id.number_goods);
//
//        String identifier  = mItemBean.getItem_code();
//        int requiredNum = mItemBean.getItem_quantity();
//        int tokenNum = mItemBean.getItem_finishied_quantity();
//
//        identifier_goods.setText("("+identifier+")");//设置编号
//        name_goods.setText(mItemBean.getItem_name());//设置货物名称
//        number_goods.setText(""+tokenNum+"/"+requiredNum);//设置需要数量与已拿取数量
//        if(requiredNum == tokenNum)//如果全部取完，则显示打勾图标
//        {
//            check.setVisibility(View.VISIBLE);
//
//        }
//        return v;
//    }
//}
