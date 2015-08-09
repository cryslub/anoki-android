package com.anoki.common;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.anoki.MyProfileActivity;
import com.anoki.R;
import com.anoki.UserPrayerListActivity;
import com.anoki.UserProfileActivity;
import com.anoki.pojo.Prayer;

/**
 * Created by joon on 2015-08-09.
 */
public class ActivityBase extends ActionBarActivity {

    protected void myProfile(Prayer prayer){
        if (prayer.userId == Global.me.id) {
            Intent intent = new Intent(this, MyProfileActivity.class);
            startActivityForResult(intent,Global.PROFILE);
        }
    }


    protected void showPopupMenu(final Prayer prayer,View popup){
        PopupMenu popupMenu = new PopupMenu(this, popup);
        //Inflating the Popup using xml file
        popupMenu.getMenuInflater()
                .inflate(R.menu.menu_popup, popupMenu.getMenu());


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.message:
                        break;
                    case R.id.profile: {
                        Intent intent = new Intent(ActivityBase.this, UserProfileActivity.class);
                        intent.putExtra("userId", prayer.userId);
                        startActivity(intent);
                    }
                    break;
                    case R.id.prayer: {
                        Intent intent = new Intent(ActivityBase.this, UserPrayerListActivity.class);
                        intent.putExtra("userId", prayer.userId);
                        startActivity(intent);
                    }
                    break;

                }
                return true;
            }
        });

        popupMenu.show();
    }

}
