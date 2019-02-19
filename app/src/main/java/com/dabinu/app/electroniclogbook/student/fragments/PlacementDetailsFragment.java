package com.dabinu.app.electroniclogbook.student.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.dabinu.app.electroniclogbook.R;
import com.dabinu.app.electroniclogbook.student.StudentActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlacementDetailsFragment extends Fragment implements StudentActivity.IOnBackPressed{

    EditText companyName, companyAddress, companyEmail, supervisorName, supervisorEmail;
    Spinner numWeeks;
    TextView done;

    public PlacementDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_placement_details, container, false);
        init(view);

        return view;
    }


    public void init(View view){
        companyName = view.findViewById(R.id.companyName);
        companyAddress = view.findViewById(R.id.companyAddress);
        companyEmail = view.findViewById(R.id.companyEmail);
        supervisorName = view.findViewById(R.id.supervisorName);
        supervisorEmail = view.findViewById(R.id.supervisorEmail);

        numWeeks = view.findViewById(R.id.numberOfWeeks);

        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.period, R.layout.short_spinner);
        numWeeks.setAdapter(arrayAdapter);

        done = view.findViewById(R.id.submit);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    public boolean onBackPressed() {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, new HomeFragment());
        fragmentTransaction.commit();
        return true;
    }

}