package com.anoki;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.anoki.common.Global;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.pojo.Friend;
import com.anoki.pojo.Prayer;
import com.anoki.pojo.Search;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BoxActivity extends SubActivityBase {

    static final int START_DIALOG_ID = 1000;
    static final int END_DIALOG_ID = 1001;
    static final int FRIEND_START_DIALOG_ID = 1002;
    static final int FRIEND_END_DIALOG_ID = 1003;


    private BoxAdapter meAdapter;
    private BoxAdapter friendAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box);

        setTab();
        setMeList();
        setFriendList();

        oneMonth(null);
        friendOneMonth(null);
    }


    private void setTab(){
        TabHost myTabHost =(TabHost) findViewById(R.id.tabHost); // Before adding tabs, it is imperative to call the method setup()
        myTabHost.setup(); // Adding tabs // tab1 settings
        myTabHost.addTab(myTabHost.newTabSpec("tab_creation").setIndicator("나").setContent(R.id.me));
        myTabHost.addTab(myTabHost.newTabSpec("tab_creation").setIndicator("친구").setContent(R.id.friend));

        Util.styleTab(getApplicationContext(), myTabHost);


    }


    private void setMeList(){
        Type listType = new TypeToken<ArrayList<Prayer>>() {}.getType();


        final Search search = new Search();
        search.searchId = Global.me.id;
        search.searchKey = "Y";

        List<Prayer> prayerList = Util.rest("user/prayer", "POST", search, listType);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.completed_list);
        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 3. create an adapter
        meAdapter = new BoxAdapter(prayerList);
        // 4. set adapter
        recyclerView.setAdapter(meAdapter);

    }

    private void setFriendList(){
        Type listType = new TypeToken<ArrayList<Prayer>>() {}.getType();


        final Search search = new Search();
        search.searchKey = "Y";

        List<Prayer> prayerList = Util.rest("prayer/scraped", "POST", search, listType);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.scraped_list);
        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 3. create an adapter
        friendAdapter = new BoxAdapter(prayerList);
        // 4. set adapter
        recyclerView.setAdapter(friendAdapter);

    }


    public void showPeriod(View view){
        LinearLayout period = (LinearLayout) findViewById(R.id.period);
        if(period.isShown()){
            period.setVisibility(View.GONE);
        }else {
            period.setVisibility(View.VISIBLE);
        }
    }


    public void showFriendPeriod(View view){
        LinearLayout period = (LinearLayout) findViewById(R.id.friend_period);
        if(period.isShown()){
            period.setVisibility(View.GONE);
        }else {
            period.setVisibility(View.VISIBLE);
        }

    }

    public void oneMonth(View view){
        setPeriod(-1);
    }


    public void threeMonth(View view){
        setPeriod(-3);

    }

    public void sixMonth(View view){
        setPeriod(-6);

    }

    public void oneYear(View view){
        setPeriod(-12);

    }


    public void friendOneMonth(View view){
        setFriendPeriod(-1);
    }


    public void friendThreeMonth(View view){
        setFriendPeriod(-3);

    }

    public void friendSixMonth(View view){
        setFriendPeriod(-6);

    }

    public void friendOneYear(View view){
        setFriendPeriod(-12);

    }

    private void setPeriod(int monthDiff){

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        TextView end = (TextView) findViewById(R.id.end);

        end.setText(year + "-" + (month + 1) + "-" + day);

        c.add(Calendar.MONTH, monthDiff);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);


        TextView start = (TextView) findViewById(R.id.start);

        start.setText(year + "-" + (month+1) + "-" + day);

        setFilter();
    }


    private void setFriendPeriod(int monthDiff){

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        TextView friendEnd = (TextView) findViewById(R.id.friend_end);

        friendEnd.setText(year+"-"+(month+1)+"-"+day);

        c.add(Calendar.MONTH, monthDiff);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);


        TextView friendStart = (TextView) findViewById(R.id.friend_start);

        friendStart.setText(year+"-"+(month+1)+"-"+day);

        setFilter();

    }

    public void start(View view){
        showDialog(START_DIALOG_ID);
    }

    public void end(View view){
        showDialog(END_DIALOG_ID);
    }

    public void friendStart(View view){
        showDialog(FRIEND_START_DIALOG_ID);
    }

    public void friendEnd(View view){
        showDialog(FRIEND_END_DIALOG_ID);
    }


    @Override
    protected Dialog onCreateDialog(int id) {

        switch (id) {
           case START_DIALOG_ID:
            // set date picker as current date
               return makeDateDialog(R.id.start) ;
            case END_DIALOG_ID:
                // set date picker as current date
                return makeDateDialog(R.id.end) ;
            case FRIEND_START_DIALOG_ID:
                // set date picker as current date
                return  makeDateDialog(R.id.friend_start) ;
            case FRIEND_END_DIALOG_ID:
                // set date picker as current date
                return makeDateDialog(R.id.friend_end) ;
        }

        return null;
    }

    private Dialog makeDateDialog(final int id){

        final TextView dateText = (TextView) findViewById(id);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(dateText.getText().toString()));// all done
            return new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            // set selected date into textview
                            dateText.setText(new StringBuilder().append(year)
                                    .append("-").append(monthOfYear + 1).append("-").append(dayOfMonth)
                                    .append(" "));

                            setFilter();

                        }
                    },
                    cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    private Calendar idToCalendar(int id) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        TextView dateText = (TextView) findViewById(id);

        Calendar calendar = Calendar.getInstance();

        try {

            calendar.setTime(sdf.parse(dateText.getText().toString()));// all done

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return calendar;
    }

    private void setFilter(){


        Calendar start = idToCalendar(R.id.start);
        Calendar end = idToCalendar(R.id.end);
        Calendar friendStart = idToCalendar(R.id.friend_start);
        Calendar friendEnd = idToCalendar(R.id.friend_end);

        meAdapter.setFilter(start, end);
        friendAdapter.setFilter(friendStart,friendEnd);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView time;
        TextView text;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            text = (TextView) itemLayoutView.findViewById(R.id.text);
            time = (TextView) itemLayoutView.findViewById(R.id.time);

        }

        public void bind(Prayer prayer) {

            if(prayer.text.length() > 0)
                text.setText(prayer.text);
            else     text.setText(prayer.back);

            time.setText(prayer.long_time);
        }
    }

    private class BoxAdapter extends RecyclerView.Adapter<ViewHolder> {
        protected List<Prayer> visibleObjects;
        protected List<Prayer> allObjects;

        public BoxAdapter(List<Prayer> itemsData)
        {

            this.allObjects = itemsData;
            this.visibleObjects = itemsData;
        }

        public void updateList(List<Prayer> itemsData){
            this.allObjects = itemsData;
        }

        public void flushFilter(){
            visibleObjects=new ArrayList<>();
            visibleObjects.addAll(allObjects);
            notifyDataSetChanged();
        }

        public void setFilter(Calendar start, Calendar end) {

            long s = start.getTimeInMillis();
            long e = end.getTimeInMillis();


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            visibleObjects=new ArrayList<>();
            for(Prayer prayer : allObjects){
                String date  = prayer.raw_time.substring(0,10);
                Calendar cal = Calendar.getInstance();
                try {
                    cal.setTime(sdf.parse(date));// all done
                    long c = cal.getTimeInMillis();
                    if(s <= c && c <= e){
                        visibleObjects.add(prayer);
                    }

                } catch (ParseException ex) {
                    ex.printStackTrace();
                }

            }

            notifyDataSetChanged();

        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            // create a new view
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_box_row, null);

            // create ViewHolder

            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {

            // - get data from your itemsData at this position
            // - replace the contents of the view with that itemsData

            final Prayer prayer = visibleObjects.get(position);

            viewHolder.bind(prayer);

            

        }


        // Return the size of your itemsData (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return visibleObjects.size();
        }

    }


}
