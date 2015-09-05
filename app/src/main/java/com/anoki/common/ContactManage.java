package com.anoki.common;

import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.anoki.DBManager;
import com.anoki.pojo.Friend;
import com.anoki.pojo.Invite;
import com.anoki.pojo.Phone;
import com.anoki.pojo.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by joon on 2015-08-04.
 */
public class ContactManage {


    public static List<Friend> getUnfriendContact(ContentResolver contentResolver,Context context) {
        final DBManager dbManager = new DBManager(context, "Anoki.db", null, 1);
        List<Friend> addList = new ArrayList<Friend>();

        Map<String, String> contactMap = dbManager.getContactMap();

        Cursor cur = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cur.getCount() > 0) {



            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);


                    while (pCur.moveToNext()) {
                        Friend friend = new Friend();
                        friend.phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("-","");
                        friend.name = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        friend.picture = pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID)) + "";

                        if (contactMap.get(friend.phone) == null) {

                            addList.add(friend);
                        }

                    }


                    pCur.close();
                }
            }
        }


        return addList;
    }

    public static void checkContact(ContentResolver contentResolver,Context context) {

        final DBManager dbManager = new DBManager(context, "Anoki.db", null, 1);

        List<Friend> addList = getUnfriendContact(contentResolver,context);

        System.out.println("add list size - "+addList.size());
        if(addList.size()>0){
            Invite invite = new Invite();
            invite.phone = addList;

            Invite response = Util.rest("friend", "POST", invite, Invite.class);
            int i = 0;
            if(response.friends != null){
                for (Integer friendId : response.friends) {

                    if (friendId != null) {
                        dbManager.insertContactInfo(response.phone.get(i));
                    }
                    i++;
                }
            }
        }

    }


}
