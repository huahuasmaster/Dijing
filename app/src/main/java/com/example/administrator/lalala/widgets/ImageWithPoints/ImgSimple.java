package com.example.administrator.lalala.widgets.ImageWithPoints;

import java.util.ArrayList;

public class ImgSimple {
    public int resourceID;
    public String url;
    public float scale;
    public ArrayList<PointSimple> pointSimples;

    public ImgSimple(int resourceID, float scale, ArrayList<PointSimple> pointSimples) {
        this.resourceID = resourceID;
        this.scale = scale;
        this.pointSimples = pointSimples;
    }

    public ImgSimple() {
    }

    public int getResourceID() {
        return resourceID;
    }

    public void setResourceID(int resourceID) {
        this.resourceID = resourceID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public ArrayList<PointSimple> getPointSimples() {
        return pointSimples;
    }

    public void setPointSimples(ArrayList<PointSimple> pointSimples) {
        this.pointSimples = pointSimples;
    }
}
