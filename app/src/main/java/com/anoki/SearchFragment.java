package com.anoki;

import android.app.Activity;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SearchFragment extends Fragment {

    @Bind(R.id.search_key)
    EditText search;

    String hint;
    int searchId;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ret =  inflater.inflate(R.layout.fragment_search, container, false);

        ButterKnife.bind(this, ret);

        search.setHint(hint);
        if(searchId!=-1)
            search.setId(searchId);
        return ret;
    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        TypedArray a = activity.obtainStyledAttributes(attrs,R.styleable.SearchFragment);

        hint = a.getString(R.styleable.SearchFragment_android_hint);
        searchId = a.getResourceId(R.styleable.SearchFragment_search_id, -1);

        System.out.println("searchId -" + searchId);

    }
}
