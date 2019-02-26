package com.dabinu.app.electroniclogbook.auth.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.dabinu.app.electroniclogbook.R;
import com.dabinu.app.electroniclogbook.auth.AuthActivity;
import com.dabinu.app.electroniclogbook.technical_officer.TechnicalOfficerActivity;
import com.dabinu.app.electroniclogbook.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class TechOfficerAuthFragment extends Fragment implements AuthActivity.IOnBackPressed{


    EditText password;
    CardView login;
    ImageButton back;


    public TechOfficerAuthFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_tech_officer_auth, container, false);

        init(view);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new LoginFragment());
                fragmentTransaction.commit();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password.getText().toString().trim().equals("123456789")){
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("auth", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString("login", Constants.LOGGED_IN);
                    editor.putString("type", Constants.TECHNICAL_OFFICER);
                    editor.apply();

                    startActivity(new Intent(getActivity().getApplicationContext(), TechnicalOfficerActivity.class));
                }
                else{
                    password.setError("Incorrect password");
                }
            }
        });
        return view;
    }


    public void init(View view){
        password = view.findViewById(R.id.password);
        login = view.findViewById(R.id.login);
        back = view.findViewById(R.id.back);
    }


    @Override
    public boolean onBackPressed(){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, new LoginFragment());
        fragmentTransaction.commit();
        return true;
    }
}