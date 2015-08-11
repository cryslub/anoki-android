package com.anoki;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.Settings;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anoki.common.Util;
import com.anoki.common.Global;
import com.anoki.pojo.Reply;

import org.w3c.dom.Text;



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
        ImageView image;

        TextView name;
        TextView text;
        TextView time;

        picture = (ImageView) itemLayoutView.findViewById(R.id.picture);
        image = (ImageView) itemLayoutView.findViewById(R.id.image);

        name = (TextView) itemLayoutView.findViewById(R.id.name);
        text = (TextView) itemLayoutView.findViewById(R.id.text);
        time = (TextView) itemLayoutView.findViewById(R.id.time);

        Util.setPicture(reply.userPicture, picture, getResources().getDrawable(R.drawable.ic_person_black_36dp));
        Util.setPicture(reply.picture, image);


        name.setText(reply.name);
        text.setText(reply.text);
        time.setText(reply.time);

        if(!"R".equals(reply.type)) {
            //LinearLayout con = (LinearLayout) itemLayoutView.findViewById(R.id.container);
            TextView response = (TextView) itemLayoutView.findViewById(R.id.response);
            response.setVisibility(View.GONE);
            //con.removeView(response);
        }


        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(reply.userId == Global.me.id){
                    intent = new Intent(getActivity(),MyProfileActivity.class);
                }else{
                    intent = new Intent(getActivity(),UserProfileActivity.class);
                    intent.putExtra("userId",reply.userId);
                }
                startActivity(intent);

            }
        });

        itemLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("R".equals(reply.type)){
                    ((OnFragmentInteractionListener) getActivity()).responseList(reply);
                }

            }
        });
        return itemLayoutView;
    }

    public interface OnFragmentInteractionListener{
        public void responseList(Reply reply);
    }
}
