package com.example.administrator.lalala.widgets.ImageWithPoints;

/**
 * Created by Administrator on 2017/4/23.
 */

public class PointSimple {
    // 标记点相对于横向的宽度的比例
    public double width_scale;
    // 标记点相对于横向的高度的比例
    public double height_scale;
    //标签
    public String label;
    public PointSimple(double width_scale, double height_scale) {
        this.width_scale = width_scale;
        this.height_scale = height_scale;
    }
    public PointSimple(double width_scale, double height_scale,String label) {
        this(width_scale,height_scale);
        this.label = label;
    }
}
