package com.anoki1.prayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anoki1.R;
import com.anoki1.common.GeneralRecyclerViewAdapter;
import com.anoki1.common.SubActivityBase;
import com.anoki1.common.Util;
import com.anoki1.common.ViewHolderBase;
import com.anoki1.pojo.Prayer;
import com.anoki1.pojo.Reply;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


class ResponseViewHolder extends ViewHolderBase<Reply>{


    @Bind(R.id.text) TextView text;
    @Bind(R.id.image) ImageView image;
    @Bind(R.id.date) TextView date;

    public ResponseViewHolder(View itemView) {
        super(itemView);

    }

    @Override
    public void bind(Reply reply) {
        text.setText(reply.text);
        date.setText(reply.time);
        Util.setPicture(reply.picture,image);

    }
}


public class ResponseListActivity extends SubActivityBase {

    private Prayer prayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response_list);

        Intent intent = getIntent();
        prayer = (Prayer) intent.getSerializableExtra("prayer");
        Reply focus = (Reply) intent.getSerializableExtra("reply");


        ImageView picture = (ImageView) findViewById(R.id.picture);
        Util.setPicture(prayer.userPicture, picture);

        setText(R.id.name, prayer.userName);



        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);



        int position = 0;
        List<Reply> responseList = new ArrayList<Reply>();
        int i = 0;
        for(Reply reply : prayer.reply){
            if("R".equals(reply.type)){

                responseList.add(reply);

                if(focus!= null && reply.id == focus.id){
                    position = i;
                }
                i++;
            }
        }


        GeneralRecyclerViewAdapter<Reply,ResponseViewHolder> responseAdapter = new  GeneralRecyclerViewAdapter<Reply,ResponseViewHolder> (responseList,R.layout.layout_response_row,ResponseViewHolder.class);
        LinearLayoutManager layoutManager = setRecyclerView(recyclerView,responseAdapter);


        layoutManager.scrollToPosition(position);

    }


}
