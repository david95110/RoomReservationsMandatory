package com.example.danielwinther.androidroomreservations;


import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.danielwinther.androidroomreservations.R;

public class SelectedItemFragment extends Fragment {

    private String text1 = null;
    private String text2 = null;
    private String text3 = null;
    private String text4 = null;
    public SelectedItemFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_selected_item, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            text1 = arguments.getString("text1");
            text2 = arguments.getString("text2");
            text3 = arguments.getString("text3");
            text4 = arguments.getString("text4");
            TextView textView1 = (TextView) view.findViewById(R.id.text1);
            TextView textView2 = (TextView) view.findViewById(R.id.text2);
            TextView textView3 = (TextView) view.findViewById(R.id.text3);
            TextView textView4 = (TextView) view.findViewById(R.id.text4);
            textView1.setText(text1);
            textView2.setText(text2);
            textView3.setText(text3);
            textView4.setText(text4);
        }
    }
}
