package com.anoki;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anoki.common.GeneralRecyclerViewAdapter;
import com.anoki.common.Global;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.common.ViewHolderBase;
import com.anoki.pojo.Message;
import com.anoki.pojo.Notice;
import com.anoki.pojo.Search;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


class NoticeViewHolder extends ViewHolderBase<Notice> {


    public View view;

    @Bind(R.id.text)
    public TextView text;

    @Bind(R.id.date)
    public TextView date;

    @Bind(R.id.container)
    LinearLayout container;


    @OnClick(R.id.container)
    void widen(){
        container.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
    }


    public NoticeViewHolder(View itemView) {
        super(itemView);

        view = itemView;
    }


    @Override
    public void bind(Notice notice) {


        date.setText(notice.time);
        text.setText(notice.text);


    }
}


public class NoticeActivity extends SubActivityBase {

    @Bind(R.id.list)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        Type listType = new TypeToken<ArrayList<Notice>>() {}.getType();

        List<Notice> list = Util.rest("etc/notice", "POST", null, listType);

        GeneralRecyclerViewAdapter<Notice,NoticeViewHolder> responseAdapter = new  GeneralRecyclerViewAdapter<Notice,NoticeViewHolder> (list,R.layout.layout_notice_row,NoticeViewHolder.class);
        setRecyclerView(recyclerView, responseAdapter);
    }


}
