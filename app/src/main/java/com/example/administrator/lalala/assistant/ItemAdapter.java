package com.example.administrator.lalala.assistant;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.lalala.R;
import com.example.administrator.lalala.widgets.bottompopfragmentmenu.BottomMenuFragment;
import com.example.administrator.lalala.widgets.bottompopfragmentmenu.MenuItem;
import com.example.administrator.lalala.widgets.bottompopfragmentmenu.MenuItemOnClickListener;

import java.util.ArrayList;
import java.util.List;

import entity.ItemBean;
import entity.TaskBean;

/**
 * Created by Administrator on 2017/4/25.
 */

public class ItemAdapter extends RecyclerView.Adapter {
    private List<ItemBean> items;
    private String taskType;
    private Context context;
    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView itemName;
        TextView itemCode;
        ImageView check;
        ImageView question;
        TextView location;
        TextView target;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            itemName = (TextView)itemView.findViewById(R.id.name_goods);
            itemCode = (TextView)itemView.findViewById(R.id.identifier_goods);
            check = (ImageView)itemView.findViewById(R.id.finished_goods);
            question = (ImageView)itemView.findViewById(R.id.question_goods);
            cardView = (CardView)itemView.findViewById(R.id.back);
            location = (TextView)itemView.findViewById(R.id.location_goods);
            target = (TextView) itemView.findViewById(R.id.target_goods);

        }
    }
    public ItemAdapter(List<ItemBean> items, String taskType) {
        this.items = items;
        this.taskType = taskType;
    }
    public void SetList(List<ItemBean> x )
    {
        this.items = x;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.goods_list_item2,parent,false);//只能显示一行,宽度满
        /**
         * 注意子项布局最外层 必须是wrapcontent，不然会只显示一项
         */
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goods_list_item2,null,false);//只能显示一行

        ViewHolder holder = new ViewHolder(view);
        if(taskType.equals(TaskBean.TASKTYPE_CHECK)) {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final  View vx = v;
                    BottomMenuFragment bottomMenuFragment = new BottomMenuFragment();

                    List<MenuItem> menuItemList = new ArrayList<MenuItem>();
                    MenuItem menuItem1 = new MenuItem();
                    menuItem1.setText("已遗失");
                    menuItem1.setStyle(MenuItem.MenuItemStyle.COMMON);
                    MenuItem menuItem2 = new MenuItem();
                    menuItem2.setText("已损坏");
                    menuItemList.add(menuItem1);
                    menuItemList.add(menuItem2);

                    bottomMenuFragment.setMenuItems(menuItemList);
                    Activity activity = (Activity) context;
                    bottomMenuFragment.show(activity.getFragmentManager(), "BottomMenuFragment");

                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //以下是一定会显示的view
        ItemBean itemBean = items.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.itemName.setText(itemBean.getItem_name());
        viewHolder.itemCode.setText("(" + itemBean.getRFID_code() + ")");
        viewHolder.location.setVisibility(View.VISIBLE);
        viewHolder.location.setText(context.getString(R.string.currentLocation)+
                StringHandler.handlerLocationtoString(
                        itemBean.getLocation()));
        //以下是选择性显示的view
//        if (taskType.equals(TaskBean.TASKTYPE_EXPORT) && itemBean.isItem_finished()) {
//            //如果是出库，则判断当前位置是否为“未知位置”，如果是则显示打勾
//            viewHolder.check.setVisibility(View.VISIBLE);
//        } else
            if (taskType.equals(TaskBean.TASKTYPE_IMPORT)) {//如果是入库则额外显示目标位置
            viewHolder.target.setVisibility(View.VISIBLE);
            viewHolder.target.setText(context.getString(R.string.targetLocation)+
                    StringHandler.handlerLocationtoString(
                            itemBean.getItem_target()));
                if (StringHandler.compareTargetAndLocation(itemBean.getItem_target(), itemBean.getLocation())) {
                    //如果当前位置符合目标位置，则打勾
                    viewHolder.check.setVisibility(View.VISIBLE);
                    viewHolder.question.setVisibility(View.GONE);
                } else if(StringHandler.handlerLocationtoString(itemBean.getLocation()).equals("不在柜内")){
                    //如果不在柜子，则什么图标都不用显示
                    viewHolder.check.setVisibility(View.GONE);
                    viewHolder.question.setVisibility(View.GONE);
                    viewHolder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.itemGray));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        viewHolder.cardView.setElevation(10.0f);
                    }
                }
                else {//如果在柜子,但是与目标位置不符，则显示疑问图标
                    viewHolder.check.setVisibility(View.GONE);
                    viewHolder.question.setVisibility(View.VISIBLE);
                }

        }
        else {
                viewHolder.check.setVisibility(itemBean.isItem_finished() ? View.VISIBLE : View.GONE);
                viewHolder.question.setVisibility(View.GONE);
            }

    }


    @Override
    public int getItemCount() {
        return items.size();
    }


}
