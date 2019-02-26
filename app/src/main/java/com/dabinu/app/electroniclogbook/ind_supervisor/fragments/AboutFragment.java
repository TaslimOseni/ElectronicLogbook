package com.dabinu.app.electroniclogbook.ind_supervisor.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dabinu.app.electroniclogbook.R;
import com.dabinu.app.electroniclogbook.dept_supervisor.DeptSupervisorActivity;
import com.dabinu.app.electroniclogbook.ind_supervisor.IndSupervisorActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment implements IndSupervisorActivity.IOnBackPressed {


    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_about2, container, false);

        (view.findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new com.dabinu.app.electroniclogbook.ind_supervisor.fragments.HomeFragment());
                fragmentTransaction.commit();
            }
        });

        return view;
    }


    @Override
    public boolean onBackPressed() {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, new com.dabinu.app.electroniclogbook.ind_supervisor.fragments.HomeFragment());
        fragmentTransaction.commit();
        return true;
    }
}