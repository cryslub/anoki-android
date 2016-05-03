package com.anoki1.prayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.anoki1.R;
import com.anoki1.common.GeneralRecyclerViewAdapter;
import com.anoki1.common.SimpleFriendViewHolder;
import com.anoki1.common.SubActivityBase;
import com.anoki1.pojo.Friend;
import com.anoki1.pojo.Prayer;
import com.anoki1.user.UserProfileActivity;

import butterknife.Bind;
import butterknife.OnClick;


class RequestFriendViewHolder extends SimpleFriendViewHolder {

    public RequestFriendViewHolder(View itemView) {
        super(itemView);
    }



    public void bind(Friend friend){
        super.bind(friend);

        if(friend.name == null){
            name.setText(friend.phone);
        }

    }


    @OnClick(R.id.container)
    public void next(){
        if(friend.name !=null) {
            Intent intent = new Intent(view.getContext(), UserProfileActivity.class);
            intent.putExtra("userId", friend.friend);
            view.getContext().startActivity(intent);
        }
    }
}

public class RequestListActivity extends SubActivityBase {


    @Bind(R.id.friend_list)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);

        Intent intent = getIntent();
        Prayer prayer = (Prayer) intent.getSerializableExtra("prayer");

        GeneralRecyclerViewAdapter<Friend,RequestFriendViewHolder> responseAdapter = new  GeneralRecyclerViewAdapter<Friend,RequestFriendViewHolder> (prayer.friends,R.layout.layout_friend_row,RequestFriendViewHolder.class);
        LinearLayoutManager layoutManager = setRecyclerView(recyclerView, responseAdapter);
    }


}
