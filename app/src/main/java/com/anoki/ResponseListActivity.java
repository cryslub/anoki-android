package com.anoki;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anoki.common.GeneralRecyclerViewAdapter;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.common.ViewHolderBase;
import com.anoki.pojo.Prayer;
import com.anoki.pojo.Reply;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


class ViewHolder extends ViewHolderBase<Reply>{


    TextView text;
    ImageView image;
    TextView date;

    public ViewHolder(View itemView) {
        super(itemView);

        text = (TextView) itemView.findViewById(R.id.text);
        date = (TextView) itemView.findViewById(R.id.date);
        image = (ImageView) itemView.findViewById(R.id.image);

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


        ImageView picture = (ImageView) findViewById(R.id.picture);
        Util.setPicture(prayer.userPicture, picture, getResources().getDrawable(R.drawable.ic_person_black_36dp));

        setText(R.id.name, prayer.userName);



        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);



        List<Reply> responseList = new ArrayList<Reply>();
        for(Reply reply : prayer.reply){
            if("R".equals(reply.type)){
                responseList.add(reply);
            }
        }

        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 3. create an adapter
        GeneralRecyclerViewAdapter<Reply,ViewHolder> responseAdapter = new  GeneralRecyclerViewAdapter<Reply,ViewHolder> (responseList,R.layout.layout_response_row,ViewHolder.class);
        // 4. set adapter
        recyclerView.setAdapter(responseAdapter);
    }


}
