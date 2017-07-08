package com.example.administrator.lalala.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Czq on 2017/4/18.
 */

public class mylistv extends ListView {

    public mylistv(Context context) {
        super(context);
    }

    public mylistv(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public mylistv(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public mylistv(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec
                , MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST));
    }
}
