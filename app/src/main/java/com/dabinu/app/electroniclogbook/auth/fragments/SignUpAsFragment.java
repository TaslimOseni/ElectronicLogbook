package com.dabinu.app.electroniclogbook.auth.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dabinu.app.electroniclogbook.R;
import com.dabinu.app.electroniclogbook.splash.fragments.WelcomeFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpAsFragment extends Fragment{


    Spinner signupas;
    CardView proceed;
    TextView login;
    ImageButton back;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SignUpAsFragment(){
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_as, container, false);

        back = view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new WelcomeFragment());
                fragmentTransaction.commit();
            }
        });

        login = view.findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new LoginFragment());
                fragmentTransaction.commit();
            }
        });

        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.sign_up_as, R.layout.spinner_layout);
        signupas = view.findViewById(R.id.signupas);
        signupas.setAdapter(arrayAdapter);


        proceed = view.findViewById(R.id.proceed);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(((String) signupas.getSelectedItem()).equals("Sign up as")){
                    Toast.makeText(getActivity().getApplicationContext(), "You must select a category", Toast.LENGTH_SHORT).show();
                }
                else{
                    sharedPreferences = getActivity().getSharedPreferences("auth", Context.MODE_PRIVATE);
                    editor = sharedPreferences.edit();

                    editor.putString("type_of_user", (String) signupas.getSelectedItem());
                    editor.apply();

                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container, new SignUpInitialFragment());
                    fragmentTransaction.commit();
                }
            }
        });

        return view;
    }

}
