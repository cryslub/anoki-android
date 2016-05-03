package com.anoki1.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.anoki1.R;
import com.anoki1.user.UserProfileActivity;
import com.anoki1.common.Common;
import com.anoki1.common.Util;
import com.anoki1.common.Global;
import com.anoki1.etc.MessageActivity;
import com.anoki1.pojo.Friend;
import com.anoki1.pojo.Prayer;
import com.anoki1.pojo.Reply;
import com.anoki1.pojo.Search;
import com.anoki1.user.MyProfileActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReplyFragment extends Fragment {


    @Bind(R.id.cover)
    ImageView cover;

    @Bind(R.id.popup)
    ImageView popup;


    @Bind(R.id.image)
    ImageView image;

    private Reply reply;



    public interface OnFragmentInteractionListener{
        public void responseList(Reply reply);
        public Prayer getPrayer();
    }

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

        ButterKnife.bind(this, itemLayoutView);


        ImageView picture;

        TextView name;
        TextView text;
        TextView time;

        picture = (ImageView) itemLayoutView.findViewById(R.id.picture);

        name = (TextView) itemLayoutView.findViewById(R.id.name);
        text = (TextView) itemLayoutView.findViewById(R.id.text);
        time = (TextView) itemLayoutView.findViewById(R.id.time);

        final LinearLayout c = (LinearLayout) itemLayoutView.findViewById(R.id.container);

        if(reply != null) {
            Util.setPicture(reply.userPicture, picture);




            name.setText(reply.name);
            text.setText(reply.text);
            time.setText(reply.time);

            if (!"R".equals(reply.type)) {
                //LinearLayout con = (LinearLayout) itemLayoutView.findViewById(R.id.container);
                TextView response = (TextView) itemLayoutView.findViewById(R.id.response);
                response.setVisibility(View.GONE);
                //con.removeView(response);
                cover.setImageResource(R.drawable.profile_cover_gray_small);
            }


            picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    if (reply.userId == Global.me.id) {
                        intent = new Intent(getActivity(), MyProfileActivity.class);
                    } else {
                        intent = new Intent(getActivity(), UserProfileActivity.class);
                        intent.putExtra("userId", reply.userId);
                    }
                    startActivity(intent);

                }
            });

            itemLayoutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("R".equals(reply.type)) {
                        ((OnFragmentInteractionListener) getActivity()).responseList(reply);
                    }

                }
            });


            if ("null".equals(reply.picture) || reply.picture == null || "0".equals(reply.picture)) {
                image.setVisibility(View.GONE);
            } else {
                Bitmap bmp = Util.setPicture(reply.picture, image);

                if(bmp!=null) {
                    int width = container.getMeasuredWidth();

                    int height = (int) ((float) width / (float) bmp.getWidth()) * bmp.getHeight();
                    //Log.i("media",imageView.\getWidth()+", " +bmp.getWidth()+","+  bmp.getHeight());

                    image.getLayoutParams().height = height;
                    image.requestLayout();
                    image.getLayoutParams().height = height;
                    image.requestLayout();

                }
               // int height = (int) ((float) c.getWidth() / (float) bmp.getWidth()) * bmp.getHeight();
               // Log.i("media", c.getWidth() + ", " + bmp.getWidth() + "," + bmp.getHeight());

                //  image.getLayoutParams().height = height;
                // image.requestLayout();
            }


        }
        return itemLayoutView;
    }




    @OnClick(R.id.popup)
    void popup(){
        showPopupMenu();
    }




    protected void showPopupMenu(){
        PopupMenu popupMenu = new PopupMenu(getActivity(), popup);
        //Inflating the Popup using xml file
        popupMenu.getMenuInflater()
                .inflate(R.menu.menu_reply_popup, popupMenu.getMenu());


        Prayer prayer = ((OnFragmentInteractionListener) getActivity()).getPrayer();
        if(reply.userId == Global.me.id){
            popupMenu.getMenu().findItem(R.id.message).setVisible(false);
            popupMenu.getMenu().findItem(R.id.inform).setVisible(false);
        }else {
            if (prayer.userId != Global.me.id ) {
                popupMenu.getMenu().findItem(R.id.delete).setVisible(false);
            }
        }



        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.message: {
                        Intent intent = new Intent(getActivity(), MessageActivity.class);
                        Friend friend = new Friend();
                        friend.name = reply.name;
                        friend.picture = reply.userPicture;
                        friend.friend = reply.userId;
                        intent.putExtra("friend", friend);
                        startActivityForResult(intent, Global.MESSAGE);
                    }
                    break;
                    case R.id.copy: {
                        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                            clipboard.setText(reply.text);
                        } else {
                            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", reply.text);
                            clipboard.setPrimaryClip(clip);
                        }
                    }
                    break;
                    case R.id.delete: {
                        Util.rest("prayer/reply","DELETE",reply);
                        getActivity().getFragmentManager().beginTransaction().remove(ReplyFragment.this).commit();


                    }
                    break;
                    case R.id.inform: {

                        Search search = new Search();
                        search.searchKey = reply.text;
                        search.searchId = reply.userId;
                        Util.rest("etc/inform","POST",search);

                        Common.toast(getActivity(),"신고되었습니다.");

                    }
                    break;

                }
                return true;
            }
        });

        popupMenu.show();
    }

}
