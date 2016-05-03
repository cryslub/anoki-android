package com.anoki1.etc;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anoki1.R;
import com.anoki1.common.GeneralRecyclerViewAdapter;
import com.anoki1.common.SubActivityBase;
import com.anoki1.common.Util;
import com.anoki1.common.ViewHolderBase;
import com.anoki1.pojo.Notice;
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
        text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
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
