package com.dabinu.app.electroniclogbook.technical_officer.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dabinu.app.electroniclogbook.R;
import com.dabinu.app.electroniclogbook.technical_officer.TechnicalOfficerActivity;

/**
 * A simple {@link Fragment} subclass.
 */

public class SupervisorSearchFragment extends Fragment implements TechnicalOfficerActivity.IOnBackPressed{


    public SupervisorSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_supervisor_search, container, false);

        return view;
    }


    @Override
    public boolean onBackPressed(){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, new HomeFragment());
        fragmentTransaction.commit();
        return true;
    }
}