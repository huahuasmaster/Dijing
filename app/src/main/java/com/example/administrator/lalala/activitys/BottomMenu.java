package com.example.administrator.lalala.activitys;

/**
 * Created by Czq on 2017/5/6.
 */

import android.support.v7.app.AppCompatActivity;

import com.example.administrator.lalala.R;
import com.example.administrator.lalala.widgets.bottompopfragmentmenu.BottomMenuFragment;
import com.example.administrator.lalala.widgets.bottompopfragmentmenu.MenuItem;
import com.example.administrator.lalala.widgets.bottompopfragmentmenu.MenuItemOnClickListener;

        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;

        import java.util.ArrayList;
        import java.util.List;

public class BottomMenu extends AppCompatActivity {

    private final String TAG = "BottomMenu";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btn_bottom_pop_fragment_menu = (Button) this.findViewById(R.id.btnLogin);
        btn_bottom_pop_fragment_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomMenuFragment bottomMenuFragment = new BottomMenuFragment();

                List<MenuItem> menuItemList = new ArrayList<MenuItem>();
                MenuItem menuItem1 = new MenuItem();
                menuItem1.setText("该物品已损坏");
                menuItem1.setStyle(MenuItem.MenuItemStyle.COMMON);
                MenuItem menuItem2 = new MenuItem();
                menuItem2.setText("该物品已遗失");
//                MenuItem menuItem3 = new MenuItem();
//                menuItem3.setText("点击！");
//                menuItem3.setMenuItemOnClickListener(new MenuItemOnClickListener(bottomMenuFragment, menuItem3) {
//                    @Override
//                    public void onClickMenuItem(View v, MenuItem menuItem) {
//                        Log.i("", "onClickMenuItem: ");
//                    }
//                });
                menuItemList.add(menuItem1);
                menuItemList.add(menuItem2);
//                menuItemList.add(menuItem3);

                bottomMenuFragment.setMenuItems(menuItemList);

                bottomMenuFragment.show(getFragmentManager(), "BottomMenuFragment");
            }
        });


    }
}
