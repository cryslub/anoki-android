package com.anoki;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.anoki.common.GeneralRecyclerViewAdapter;
import com.anoki.common.Global;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.common.ViewHolderBase;
import com.anoki.pojo.Dalant;
import com.anoki.pojo.Notice;
import com.anoki.pojo.Search;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


class DalantViewHolder extends ViewHolderBase<Dalant> {


    public View view;

    @Bind(R.id.text)
    public TextView text;

    @Bind(R.id.date)
    public TextView date;

    @Bind(R.id.amount)
    public TextView amount;


    public DalantViewHolder(View itemView) {
        super(itemView);

        view = itemView;
    }


    @Override
    public void bind(Dalant dalant) {


        date.setText(dalant.time);

        DecimalFormat df = new DecimalFormat("#,###");

        if("C".equals(dalant.type)){
            text.setText(df.format(dalant.amount)+" 달란트");
            amount.setText(df.format(dalant.amount)+" 원");
        }else{
            text.setText(dalant.text);
            amount.setText(df.format(dalant.amount)+" 달란트");
        }

    }
}

public class DalantActivity extends SubActivityBase {

    @Bind(R.id.dalant)
    TextView dalant;

    @Bind(R.id.charge_list)
    RecyclerView chargeList;

    @Bind(R.id.use_list)
    RecyclerView useList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dalant);

        TabHost myTabHost=(TabHost) findViewById(R.id.tabHost); // Before adding tabs, it is imperative to call the method setup()
        myTabHost.setup(); // Adding tabs // tab1 settings
        myTabHost.addTab(myTabHost.newTabSpec("tab_creation").setIndicator("달란트 충전내역").setContent(R.id.charge));
        myTabHost.addTab(myTabHost.newTabSpec("tab_creation").setIndicator("달란트 사용내역").setContent(R.id.use));

        Util.styleTab(getApplicationContext(), myTabHost);

    }

    protected  void load(){

        Global.reloadMe();


        DecimalFormat df = new DecimalFormat("#,###");

        dalant.setText(df.format(Global.me.dalant));

        Type listType = new TypeToken<ArrayList<Dalant>>() {}.getType();

        List<Dalant> list = Util.rest("etc/dalant", "POST", new Search(), listType);

        List<Dalant> charge = new ArrayList<Dalant>();
        List<Dalant> use = new ArrayList<Dalant>();


        for(Dalant dalant : list){
            if("C".equals(dalant.type)){
                charge.add(dalant);
            }else{
                use.add(dalant);
            }
        }


        GeneralRecyclerViewAdapter<Dalant,DalantViewHolder> chgargeAdapter = new  GeneralRecyclerViewAdapter<Dalant,DalantViewHolder> (charge,R.layout.layout_dalant_row, DalantViewHolder.class);
        setRecyclerView(chargeList, chgargeAdapter);

        GeneralRecyclerViewAdapter<Dalant,DalantViewHolder> useAdapter = new  GeneralRecyclerViewAdapter<Dalant,DalantViewHolder> (use,R.layout.layout_dalant_row, DalantViewHolder.class);
        setRecyclerView(useList, useAdapter);

    }


    public void charge(View view){
        Intent intent = new Intent(DalantActivity.this, ChargeActivity.class);
        intent.putExtra("caller","DalantActivity");

        startActivity(intent);
    }
}
