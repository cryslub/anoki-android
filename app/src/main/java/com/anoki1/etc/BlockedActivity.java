package com.anoki1.etc;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.anoki1.R;
import com.anoki1.common.CallBack;
import com.anoki1.common.Global;
import com.anoki1.common.FriendViewHolder;
import com.anoki1.common.GeneralRecyclerViewAdapter;
import com.anoki1.common.RestService;
import com.anoki1.common.SubActivityBase;
import com.anoki1.common.Util;
import com.anoki1.fragment.SearchFragment;
import com.anoki1.pojo.Friend;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


class BlockedViewHolder extends FriendViewHolder {



    protected Friend friend;

    @OnClick(R.id.release)
    void release(){

        friend.apiKey = Global.apiKey;
        friend.state ="A";
        Util.rest("friend", "PUT", friend);

        RecyclerView recyclerView = (RecyclerView) view.getParent();
        final GeneralRecyclerViewAdapter<Friend,BlockedViewHolder> recyclerviewAdapter
                = (GeneralRecyclerViewAdapter<Friend,BlockedViewHolder>) recyclerView.getAdapter();

        RestService.getFriendList("B", new CallBack<ArrayList<Friend>>() {
            @Override
            public void success(ArrayList<Friend> result) {

                recyclerviewAdapter.updateList(result);
            }
        });

    }


    @Override
    public void bind(Friend friend) {
        super.bind(friend);

        this.friend = friend;

    }

    public BlockedViewHolder(View itemView) {
        super(itemView);
    }


}


public class BlockedActivity extends SubActivityBase implements SearchFragment.OnFragmentInteractionListener{

    @Bind(R.id.friend_list)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked);


       RestService.getFriendList("B", new CallBack<ArrayList<Friend>>() {
            @Override
            public void success(ArrayList<Friend> friendList) {
                GeneralRecyclerViewAdapter<Friend, BlockedViewHolder> responseAdapter = new GeneralRecyclerViewAdapter<Friend, BlockedViewHolder>(friendList, R.layout.layout_blocked_row, BlockedViewHolder.class);
                LinearLayoutManager layoutManager = setRecyclerView(recyclerView, responseAdapter);

            }
        });


    }

    @Override
    public void onSearch(String key) {

    }
}
