package com.anoki1.team;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.anoki1.R;
import com.anoki1.common.AnokiDialog;
import com.anoki1.common.CallBack;
import com.anoki1.common.GeneralRecyclerViewAdapter;
import com.anoki1.common.MemberViewHolderBase;
import com.anoki1.common.SubActivityBase;
import com.anoki1.pojo.DialogData;
import com.anoki1.pojo.Member;
import com.anoki1.pojo.Response;
import com.anoki1.pojo.Search;
import com.google.gson.reflect.TypeToken;
import com.anoki1.common.Global;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


class TransferViewHolder extends MemberViewHolderBase {



    public TransferViewHolder(View itemView) {
        super(itemView);
    }

    @OnClick(R.id.container)
    void transfer(){

        ((TeamTransferActivity)activity).setLeader(member);
    }

}


public class TeamTransferActivity extends SubActivityBase {


    int teamId;

    Member selected;

    @Bind(R.id.leader_list)
    RecyclerView leaderList;

    GeneralRecyclerViewAdapter<Member,TransferViewHolder> leaderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_transfer);


        Intent intent = getIntent();
        teamId = intent.getIntExtra("teamId",-1);

        setLeaderList();
    }



    private void setLeaderList(){

        Type listType = new TypeToken<ArrayList<Member>>() {}.getType();

        Search search = new Search();
        search.searchId =teamId;
        search.searchKey = "J";
        search.searchType = "transfer";

       backGroundRest("team/members", "POST", search, listType, new CallBack<List<Member>>() {
           @Override
           public void success(List<Member> memberList) {
               leaderAdapter = new GeneralRecyclerViewAdapter<Member, TransferViewHolder>(memberList, R.layout.layout_simple_friend_row, TransferViewHolder.class, TeamTransferActivity.this);

               setRecyclerView(TeamTransferActivity.this.leaderList, leaderAdapter);
           }
       });




    }

    public void setLeader(Member member){
        selected = member;
        showDialog(100);
    }

    public Dialog onCreateDialog(int id) {


        Dialog dialog = null;
        DialogData data = new DialogData();


        data.title="책임 청지기 양도";
        data.text = selected.name + "에게 책임 청지기를 양도하겠습니까?";
        data.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Member member = new Member();
                member.team = teamId;
                member.user = Global.me.id;
                member.role = 2;

                backGroundRest("team/member", "PUT", member,Response.class, new CallBack<Response>(
                ) {
                    @Override
                    public void success(Response result) {
                        rest("team/member", "PUT", member);

                        selected.role = 3;

                        backGroundRest("team/member", "PUT", selected, Response.class, new CallBack<Response>() {

                            @Override
                            public void success(Response response) {

                                succeed();
                                succeed();
                            }
                        });

                    }
                });

             //   rest("team/member", "PUT", selected);


            }
        };


        return new AnokiDialog(this,data);
    }
}
