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
            TextView textView = (TextView) view.findViewById(R.id.text1);
            textView.setText(text1);
        }
    }
}
