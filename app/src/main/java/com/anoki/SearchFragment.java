package com.anoki;

import android.app.Activity;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SearchFragment extends Fragment {

    @Bind(R.id.search_key)
    EditText search;

    String hint;
    int searchId;
    int imeOptions;

    private SearchFragment.OnFragmentInteractionListener mListener;

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

//        search.setImeOptions(imeOptions);

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    mListener.onSearch(search.getText().toString());
                    return true;
                }
                return false;
            }
        });

        return ret;

    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        TypedArray a = activity.obtainStyledAttributes(attrs, R.styleable.SearchFragment);

        hint = a.getString(R.styleable.SearchFragment_android_hint);
        searchId = a.getResourceId(R.styleable.SearchFragment_search_id, -1);
        imeOptions = a.getInt(R.styleable.SearchFragment_android_imeOptions,-1);

        System.out.println("imeOptions -" + imeOptions);

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (SearchFragment.OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnFragmentInteractionListener{
        public void onSearch(String key);
    }

}
