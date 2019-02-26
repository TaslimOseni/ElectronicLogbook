package com.dabinu.app.electroniclogbook.technical_officer.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dabinu.app.electroniclogbook.R;
import com.dabinu.app.electroniclogbook.exit.ExitActivity;
import com.dabinu.app.electroniclogbook.technical_officer.TechnicalOfficerActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements TechnicalOfficerActivity.IOnBackPressed{


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        view.findViewById(R.id.stud).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new StudentSearchFragment());
                fragmentTransaction.commit();
            }
        });

        view.findViewById(R.id.stud).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new SupervisorSearchFragment());
                fragmentTransaction.commit();
            }
        });

        return view;
    }


    @Override
    public boolean onBackPressed(){
        new AlertDialog.Builder(getActivity())
                .setMessage("Are you sure you want to exit?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ExitActivity.exit(getActivity().getApplicationContext());
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();

        return true;
    }
}
