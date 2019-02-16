package com.dabinu.app.electroniclogbook.splash.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dabinu.app.electroniclogbook.R;
import com.dabinu.app.electroniclogbook.auth.fragments.LoginFragment;
import com.dabinu.app.electroniclogbook.auth.fragments.SignUpAsFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomeFragment extends Fragment{

    CardView loginCard, signupCard;

    public WelcomeFragment(){
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        loginCard = view.findViewById(R.id.loginCard);
        loginCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new LoginFragment());
                fragmentTransaction.commit();
            }
        });

        signupCard = view.findViewById(R.id.signupCard);
        signupCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new SignUpAsFragment());
                fragmentTransaction.commit();
            }
        });

        return view;
    }

}
