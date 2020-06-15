package com.ashtray.movie360.features;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashtray.movie360.R;
import com.ashtray.movie360.entities.MyFragment;

public class FragmentAdminLogin extends MyFragment {

    public FragmentAdminLogin() {
        // Required empty public constructor
    }

    @Override
    public boolean handleBackButtonPressed() {
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_login, container, false);
    }
}
