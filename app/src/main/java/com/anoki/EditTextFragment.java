package com.anoki;


import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditTextFragment extends Fragment implements View.OnClickListener{

    private int inputType;
    private int maxLength;

    public EditTextFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_edit_text, container, false);

        EditText editText = (EditText) view.findViewById(R.id.calc_txt_Prise);
        ImageButton b = (ImageButton) view.findViewById(R.id.calc_clear_txt_Prise);
        b.setOnClickListener(this);

        if(this.inputType != -1){
                editText.setInputType(this.inputType );
        }

        if(this.maxLength != -1){
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(maxLength);
            editText.setFilters(fArray);
        }

        return view;
    }

    public void clear(View view){
        EditText editText = (EditText) getView().findViewById(R.id.calc_txt_Prise);
        editText.setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.calc_clear_txt_Prise:
                clear(v);
                break;
        }
    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        TypedArray a = activity.obtainStyledAttributes(attrs,R.styleable.EditTextFragment);

        this.inputType = a.getInt(R.styleable.EditTextFragment_android_inputType, -1);

        this.maxLength = a.getInt(R.styleable.EditTextFragment_android_maxLength, -1);

    }
}