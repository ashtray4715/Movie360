package com.ashtray.movie360.features;

import android.app.Activity;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.ashtray.movie360.R;
import com.ashtray.movie360.entities.MyFragment;
import com.ashtray.movie360.manager.OnlineDBLogin;

public class FragmentAdminLogin extends MyFragment {

    private EditText editTextUserName;
    private EditText editTextPassword;

    private boolean tryingToSignIn;

    public FragmentAdminLogin() {
        // Required empty public constructor
    }

    @Override
    public boolean handleBackButtonPressed() {
        myFragmentCallBacks.showFragment(MyFragmentNames.HOME);
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin_login, container, false);

        editTextUserName = v.findViewById(R.id.EditTextForTakingUserNameInput);
        editTextPassword = v.findViewById(R.id.EditTextForTakingPasswordInput);

        v.findViewById(R.id.ButtonForLoginCancel).setOnClickListener(v1 -> tryToSingIn());

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myFragmentCallBacks.setBackButtonEnabled(true);
        myFragmentCallBacks.setActivityTitle("Admin Login");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            handleBackButtonPressed();
        }
        return true;
    }

    private void tryToSingIn(){

        if(tryingToSignIn) {
            myFragmentCallBacks.showToastMessage("Wait", true);
            return;
        }

        Activity activity = getActivity();
        if(activity == null){
            myFragmentCallBacks.showToastMessage("Restart App", true);
            return;
        }

        String userName = editTextUserName.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(userName.length() == 0 || password.length() == 0) {
            myFragmentCallBacks.showToastMessage("Invalid input", true);
            return;
        }

        OnlineDBLogin onlineDBLogin = new OnlineDBLogin(new OnlineDBLoginHandler());
        tryingToSignIn = true;
        onlineDBLogin.tryToSignIn(activity, userName, password);
    }

    private class OnlineDBLoginHandler implements OnlineDBLogin.LoginCallBack {

        @Override
        public void onLoginSuccess(String adminName) {
            myFragmentCallBacks.showToastMessage("hello " + adminName, true);
            tryingToSignIn = false;
        }

        @Override
        public void onLoginFailed(String errorMessage) {
            myFragmentCallBacks.showToastMessage(errorMessage, true);
            tryingToSignIn = false;
        }
    }

}
