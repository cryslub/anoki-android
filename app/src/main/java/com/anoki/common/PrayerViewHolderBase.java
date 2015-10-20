package com.anoki.common;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anoki.R;
import com.anoki.pojo.Prayer;
import com.anoki.pojo.Response;
import com.makeramen.dragsortadapter.DragSortAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015-08-12.
 */

public class PrayerViewHolderBase extends  DragSortAdapter.ViewHolder {

    protected Prayer prayer;
    View view;

    protected Activity parentActivity;

    @Bind(R.id.picture) protected ImageView picture;
    @Bind(R.id.name)
    protected TextView name;

    TextView text;
    TextView date;

    TextView prayCount;
    TextView replyCount;

    @Nullable
    @Bind(R.id.response_count)
    TextView responseCount;


    @Bind(R.id.pray) protected ImageView pray;
    @Bind(R.id.scrap) protected  ImageView scrap;

    @Nullable @Bind(R.id.friend_count) TextView friendCount;
    @Nullable @Bind(R.id.scope) TextView scope;
    @Nullable @Bind(R.id.scope_image) ImageView scopeImage;


    LinearLayout container;
    PrayerAdapter adapter;


    @OnClick({R.id.container,R.id.text})
    void showDetail(){
        Common.showPrayerDetail(parentActivity, prayer);
    }


    public PrayerViewHolderBase(DragSortAdapter adapter, View itemLayoutView,Activity parentActivity) {
        super(adapter, itemLayoutView);

        view = itemLayoutView;

        ButterKnife.bind(this, view);


        text = (TextView) itemLayoutView.findViewById(R.id.text);
        date = (TextView) itemLayoutView.findViewById(R.id.date);
        prayCount = (TextView) itemLayoutView.findViewById(R.id.pray_count);
        replyCount = (TextView) itemLayoutView.findViewById(R.id.reply_count);

        this.parentActivity = parentActivity;

    }

    public void bind(final Prayer prayer){


        this.prayer = prayer;

        scrap.setVisibility(View.VISIBLE);

        //view.setId(prayer.id);

        text.setText(prayer.simpleText());

        date.setText(prayer.long_time);
        prayCount.setText(prayer.prayCount);
        replyCount.setText(prayer.replyCount);
        if(responseCount !=null)
            responseCount.setText("응답 " + prayer.responseCount);



        if(prayer.checkPrayable()){
            pray.setImageResource(R.drawable.btn_pray_mint);
        }else{
            pray.setImageResource(R.drawable.btn_pray_gray);
        }

        if(!(prayer.userId != Global.me.id) ||! ("null".equals(prayer.scrapd) || prayer.scrapd == null)){

            scrap.setVisibility(View.GONE);
        }

        if(scope != null) {
            switch (prayer.pub) {
                case "P":
                    scope.setText("전체공개");
                    friendCount.setText("All");
                    scopeImage.setImageResource(R.drawable.ic_friend);
                    break;
                case "F":
                    scope.setText("친구공개");
                    friendCount.setText("+" + prayer.friends.size());
                    scopeImage.setImageResource(R.drawable.ic_friend);
                    break;
                case "S":
                    scope.setText("나만보기");
                    friendCount.setText("");
                    scopeImage.setImageResource(R.drawable.ic_lock);
                    break;
            }
        }

    }

}