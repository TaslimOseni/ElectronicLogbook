package com.dabinu.app.electroniclogbook.auth.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dabinu.app.electroniclogbook.R;
import com.dabinu.app.electroniclogbook.landing.LandingActivity;
import com.dabinu.app.electroniclogbook.splash.fragments.WelcomeFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    EditText email, password;
    TextView signup;
    CardView login;
    ImageButton back;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        back = view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new WelcomeFragment());
                fragmentTransaction.commit();
            }
        });

        signup = view.findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new SignUpAsFragment());
                fragmentTransaction.commit();
            }
        });

        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);

        login = view.findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo: validate credentials, display ProgressBar and log-in
                startActivity(new Intent(getActivity().getApplicationContext(), LandingActivity.class));
            }
        });


        return view;
    }

}