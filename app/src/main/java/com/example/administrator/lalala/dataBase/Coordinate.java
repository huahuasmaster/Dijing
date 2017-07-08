package com.example.administrator.lalala.dataBase;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/4/23.
 */

public class Coordinate extends DataSupport{
    private int warehouseNumber;
    private String area;
    private String container;
    private float coordinate_X;
    private float coordinate_Y;
    public Coordinate(){

    }
    public Coordinate(int warehouseNumber, String area, String container, float coordinate_X, float coordinate_Y) {
        this.warehouseNumber = warehouseNumber;
        this.area = area;
        this.container = container;
        this.coordinate_X = coordinate_X;
        this.coordinate_Y = coordinate_Y;
    }

    public int getWarehouseNumber() {
        return warehouseNumber;
    }

    public void setWarehouseNumber(int warehouseNumber) {
        this.warehouseNumber = warehouseNumber;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public float getCoordinate_X() {
        return coordinate_X;
    }

    public void setCoordinate_X(float coordinate_X) {
        this.coordinate_X = coordinate_X;
    }

    public float getCoordinate_Y() {
        return coordinate_Y;
    }

    public void setCoordinate_Y(float coordinate_Y) {
        this.coordinate_Y = coordinate_Y;
    }
}
