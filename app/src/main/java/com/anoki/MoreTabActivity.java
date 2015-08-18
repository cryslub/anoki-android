package com.anoki;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.anoki.common.Global;
import com.anoki.common.TabActivityBase;
import com.anoki.common.Util;
import com.anoki.pojo.User;

public class MoreTabActivity extends TabActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_tab);

        setUserInfo(Global.me);
    }


    @Override
    protected void onStart() {
        super.onStart();

        setUserInfo(Global.me);
    }

    protected void setUserInfo(User user){
        ImageView profileImage = (ImageView) findViewById(R.id.picture);
        Util.setPicture(user.picture + "", profileImage);


        setText(R.id.name, user.name);
        setText(R.id.text, user.text);
        setText(R.id.account, user.account);

    }



    public void profile(View view){
        Intent intent = new Intent(MoreTabActivity.this, MyProfileActivity.class);
        startActivityForResult(intent, Global.PROFILE);
    }

    public void box(View view){
        Intent intent = new Intent(MoreTabActivity.this, BoxActivity.class);
        startActivity(intent);
    }

    public void notice(View view){
        Intent intent = new Intent(MoreTabActivity.this, NoticeActivity.class);
        startActivity(intent);
    }

    public void dalant(View view){
        Intent intent = new Intent(MoreTabActivity.this, DalantActivity.class);
        startActivity(intent);
    }

    public void friend(View view){
        Intent intent = new Intent(MoreTabActivity.this, ChooseContactsActivity.class);
        intent.putExtra("caller","MoreTabActivity");
        startActivity(intent);

    }

    public void request(View view){
        Intent intentsms = new Intent( Intent.ACTION_VIEW, Uri.parse("sms:" + "") );
        intentsms.putExtra( "sms_body", Global.me.name+"님이 기도SNS 아노키로 중보기도를 요청하였습니다. 아래 링크를 눌러 들어오세요.\n anoki.co.kr/anoki/invite.jsp\nFrom "+ Global.me.name);
        startActivity(intentsms);
    }
}
