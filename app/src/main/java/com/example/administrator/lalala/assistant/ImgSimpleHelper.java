package com.example.administrator.lalala.assistant;

import com.example.administrator.lalala.R;
import com.example.administrator.lalala.dataBase.Coordinate;
import com.example.administrator.lalala.widgets.ImageWithPoints.ImgSimple;
import com.example.administrator.lalala.widgets.ImageWithPoints.PointSimple;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entity.ItemBean;
import entity.TaskBean;

/**
 * Created by Administrator on 2017/4/23.
 */

public class ImgSimpleHelper {

    public static ImgSimple getImgSimple(List<ItemBean> itemBeanList,String taskType) {
        ImgSimple imgSimple = new ImgSimple();//需要最终返回的图片
        imgSimple.setResourceID(R.drawable.timg);//图标来源
        imgSimple.setScale(1.52505f);//图片比例
        ArrayList<PointSimple> points= new ArrayList<>();//用于储存货物对应的多个坐标
        for(ItemBean itemBean :itemBeanList) {//如果是入库则显示目标位置
//            points.add(turnDetailToCoors(
//                    taskType.equals(TaskBean.TASKTYPE_IMPORT) ? itemBean.getItem_target() : itemBean.getLocation()
//                    ,itemBean.getItem_name()
//            ));
            PointSimple pointSimple = turnDetailToCoors(
                    taskType.equals(TaskBean.TASKTYPE_IMPORT) ? itemBean.getItem_target() : itemBean.getLocation()
                    ,itemBean.getItem_name());
            if(pointSimple != null) {
                points.add(pointSimple);
            }
        }
        imgSimple.setPointSimples(points);
        return imgSimple;
    }
    public static ImgSimple getImgSimple(){//测试用
        List<ItemBean> itemBeanList = new ArrayList<>();
        ImgSimple imgSimple = new ImgSimple();
        imgSimple.setResourceID(R.drawable.timg);//图标来源
        imgSimple.setScale(1.52505f);//图片比例
        ArrayList<PointSimple> points= new ArrayList<>();//用于储存货物对应的多个坐标
        //对img的赋值

        //对points的赋值
        points.add(new PointSimple(0.25f,0.33f));
        points.add(new PointSimple(0.56f,0.76f));
        points.add(new PointSimple(0.56f,0.89f));
        imgSimple.setPointSimples(points);

        return imgSimple;
    }
    public static PointSimple turnDetailToCoors(String location,String itemName){//从一条货物细节（位置）中，获取信息
        PointSimple point;
        String[] loc = StringHandler.handlerLocation(location);
        if(loc == null) {
            return null;
        }
        String area = loc[1];//哪个区域
        String  container;
        if(loc.length>2) {
            container = loc[2];//那个货柜
        }else {
            container = "1柜";
        }
        List<Coordinate> coordinate = DataSupport
                .select("coordinate_X","coordinate_Y")
                .where("area = ? and container = ?",area,container)
                .limit(1)
                .find(Coordinate.class);
        if (coordinate.size() == 0) {
            return null;
        }
        Coordinate coor = coordinate.get(0);//最终映射得到的坐标
        point = new PointSimple(coor.getCoordinate_X(),coor.getCoordinate_Y(),itemName);
        return point;
    }
}
