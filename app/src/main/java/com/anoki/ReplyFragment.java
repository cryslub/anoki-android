package com.anoki;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anoki.common.Util;
import com.anoki.pojo.Reply;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReplyFragment extends Fragment {

    private Reply reply;

    public ReplyFragment(){

    }

    public void setReply(Reply reply) {
        this.reply = reply;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemLayoutView = inflater.inflate(R.layout.fragment_reply, container, false);

        ImageView picture;
        TextView name;
        TextView text;

        picture = (ImageView) itemLayoutView.findViewById(R.id.picture);
        name = (TextView) itemLayoutView.findViewById(R.id.name);
        text = (TextView) itemLayoutView.findViewById(R.id.text);

        Util.setPicture(reply.picture, picture, getResources().getDrawable(R.drawable.ic_person_black_36dp));


        name.setText(reply.name);
        text.setText(reply.text);


        return itemLayoutView;
    }


}
