package com.dabinu.app.electroniclogbook.student.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dabinu.app.electroniclogbook.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlacementDetailsFragment extends Fragment {


    public PlacementDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_placement_details, container, false);
    }

}
